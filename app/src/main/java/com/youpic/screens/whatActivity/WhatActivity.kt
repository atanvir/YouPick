package com.youpic.screens.whatActivity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.youpic.R
import com.youpic.adapter.WhatAdapter
import com.youpic.apiConnection.Body.AddFavoritePickBody
import com.youpic.apiConnection.Body.UserSignupBody
import com.youpic.apiConnection.DataModel.GetCuisineListDataModel
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.giveSecActivity.GiveSecActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.screens.nearActivity.NearActivity
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.ANDROID
import com.youpic.utils.Constants.Companion.MSG_DIALOG
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_DEVICE_TOKEN
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Location
import com.youpic.utils.Location2
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_what.*
import kotlinx.android.synthetic.main.common_titlebar.*
import kotlinx.android.synthetic.main.what_layout.*
import kotlinx.android.synthetic.main.what_layout.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WhatActivity : BaseActivity(),WhatAdapter.ItemClickListener
{

    lateinit var whatViewModel:WhatViewModel
    var selectedItem: MutableMap<Int,String>?= HashMap()
    var list=ArrayList<GetCuisineListDataModel.Data>()

    private var LOCATION_PERMISSION_CODE:Int=1
    private var GOOGLE_SERVICE_CODE:Int=2
    private var PERMISSION_SETTING_CODE:Int=3

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_what)

        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }

        Location2.startLocation(this,LOCATION_PERMISSION_CODE,GOOGLE_SERVICE_CODE,PERMISSION_SETTING_CODE)

        init()
        initControl()
        myObserver()

        /*if(Utils.getUserToken(this).isNotEmpty()){
            whatViewModel.getCuisineListApi(this)
        }else{
            setAdapter()
        }*/

        whatViewModel.getCuisineListApi(this)

        Utils.printAllExtras(intent.extras!!)

    }

    override fun init()
    {
        if(intent.getBooleanExtra("fromHome",false)){
            (findViewById<View>(R.id.titleTextView) as TextView).text = "Pick Your Favorites!"
            backTextView.visibility=View.INVISIBLE
        }else if (intent.getBooleanExtra("fromSignup", false)) {
            (findViewById<View>(R.id.titleTextView) as TextView).text = "Pick Your Favorites!"
        } else {
            (findViewById<View>(R.id.titleTextView) as TextView).text = "What Sounds Good?"
        }

        if (intent.getBooleanExtra("fromInvites", false)) {
            cuisineTextView.visibility = View.VISIBLE
        } else
            cuisineTextView.visibility = View.GONE

        whatViewModel=ViewModelProvider(this).get(WhatViewModel::class.java)

    }

    override fun initControl()
    {
        doneButton.setOnClickListener {

            val selectedFav:ArrayList<String> = ArrayList(selectedItem?.values)

            if(selectedFav.isNotEmpty()){

                if (intent.getBooleanExtra("fromInvites", false))
                {
                    startActivity(Intent(this@WhatActivity, GiveSecActivity::class.java)
                            .putExtras(intent.extras!!)
                            .putStringArrayListExtra("cuisineList",selectedFav))
                } else if (intent.getBooleanExtra("fromSignup", false))
                {

                    val body=UserSignupBody()
                    body.countryCode= intent.getStringExtra("countryCode")!!
                    body.deviceToken=getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_DEVICE_TOKEN,"")!!
                    body.deviceType= ANDROID
                    body.latitude= Location2.getLat().toString()
                    body.longitude=Location2.getLong().toString()
                    body.mobileNumber=intent.getStringExtra("phoneNumber")!!
                    body.name=intent.getStringExtra("name")!!
                    body.password=intent.getStringExtra("password")!!
                    body.timezone=TimeZone.getDefault().id
                    body.pickFavorites=selectedFav
                    body.country=intent.getStringExtra("country")!!
                    Log.w("testCountryNameCode",intent.getStringExtra("country")!!)

                    whatViewModel.userSignUpApi(this,body)
                }else if(intent.getBooleanExtra("fromDone", false))
                {
                    startActivity(Intent(this, GiveSecActivity::class.java)
                            .putExtras(intent.extras!!)
                            .putStringArrayListExtra("cuisineList",selectedFav))
                }else if(intent.getBooleanExtra("fromHome",false)){
                    whatViewModel.addFavoritePickApi(this,AddFavoritePickBody(selectedFav))
                }else
                    startActivity(Intent(this@WhatActivity, NearActivity::class.java))

            }else{

                Utils.showMessage(this,"Please select some cuisines!", MSG_TOAST)

            }


        }
    }

    override fun myObserver()
    {
        whatViewModel.signUpResponse.observe(this,{
            if(it.status== SUCCESS){
                startActivity(Intent(this@WhatActivity, HomeActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }else{
                Utils.showMessage(this,it.message, MSG_DIALOG)
            }
        })

        whatViewModel.getCuisineListResponse.observe(this, {
            list=it.data
            whatRecyclerView.layoutManager = LinearLayoutManager(this)
            whatRecyclerView.adapter = WhatAdapter(this,this,it.data,intent.getBooleanExtra("fromSignup", false))
        })

        whatViewModel.addFavoritePickResponse.observe(this,{
            if(it.status== SUCCESS){
                startActivity(Intent(this@WhatActivity, HomeActivity::class.java))
            }else{
                Utils.showMessage(this,it.message, MSG_DIALOG)
            }
        })
    }

    override fun itemClick(isCheck: Boolean, pos: Int, selectedItem: String) {
        if(isCheck){
            this.selectedItem?.put(pos,selectedItem)
            /*if(this.selectedItem!!.containsKey(0)){
                this.selectedItem?.remove(0)
                list.get(0).isSelected=false
                whatRecyclerView.adapter?.notifyDataSetChanged()
                (whatRecyclerView.adapter as WhatAdapter).setTotalCount(this.selectedItem!!.size)
            }*/
        }
        else{
            this.selectedItem?.remove(pos)
        }


        Log.w("test","Values: "+this.selectedItem?.values)
    }

    override fun onBackPressed() {
        if(!intent.getBooleanExtra("fromHome",false)){
            super.onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==GOOGLE_SERVICE_CODE || requestCode==PERMISSION_SETTING_CODE){
            if(resultCode== Activity.RESULT_OK){
                Location2.startLocation(this,LOCATION_PERMISSION_CODE,GOOGLE_SERVICE_CODE,PERMISSION_SETTING_CODE)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            LOCATION_PERMISSION_CODE -> {
                var allAccepted = true
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        allAccepted = false
                        break
                    }
                }
                if (allAccepted) {
                    Location2.startLocation(this,LOCATION_PERMISSION_CODE,GOOGLE_SERVICE_CODE,PERMISSION_SETTING_CODE)
                }else {
                    Utils.showMessage(this,"Please Allow permission for the security purpose", Constants.MSG_TOAST)
                }

            }
        }
    }

    fun setAdapter(){


        list.add(GetCuisineListDataModel.Data("",false,"","I'm Not Picky!","","","",false))
        list.add(GetCuisineListDataModel.Data("",false,"","Pizza","","","",false))
        list.add(GetCuisineListDataModel.Data("",false,"","Mexican","","","",false))
        list.add(GetCuisineListDataModel.Data("",false,"","Latin","","","",false))
        list.add(GetCuisineListDataModel.Data("",false,"","Southwestern","","","",false))
        list.add(GetCuisineListDataModel.Data("",false,"","Steakhouse","","","",false))
        list.add(GetCuisineListDataModel.Data("",false,"","Vietnames","","","",false))
        list.add(GetCuisineListDataModel.Data("",false,"","Other","","","",false))
        /*list.add(GetCuisineListDataModel.Data("Pizza",false))
        list.add(GetCuisineListDataModel.Data("Mexican",false))
        list.add(GetCuisineListDataModel.Data("Latin",false))
        list.add(GetCuisineListDataModel.Data("Southwestern",false))
        list.add(GetCuisineListDataModel.Data("Steakhouse",false))
        list.add(GetCuisineListDataModel.Data("Vietnames",false))
        list.add(GetCuisineListDataModel.Data("Other",false))*/


        whatRecyclerView.layoutManager = LinearLayoutManager(this)
        whatRecyclerView.adapter = WhatAdapter(this,this,list,false)
    }

    inner class WhatDataModel(var name:String,var isSelected:Boolean)

}