package com.youpic.screens.giveSecActivity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.youpic.R
import com.youpic.apiConnection.Body.GetResaturnatListBody
import com.youpic.apiConnection.DataModel.GetResaturnatListDataModel
import com.youpic.baseClass.BaseActivity
import com.youpic.databinding.ActivityGiveSecBinding
import com.youpic.screens.youPickActivity.YouPickActivity
import com.youpic.screens.youPickActivity.YouPickViewModel
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Location
import com.youpic.utils.Location2
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_give_sec.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class GiveSecActivity : BaseActivity()
{
    lateinit var viewModel: YouPickViewModel
    private var LOCATION_PERMISSION_CODE:Int=1
    private var GOOGLE_SERVICE_CODE:Int=2
    private var PERMISSION_SETTING_CODE:Int=3

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_give_sec)

        Location2.startLocation(this,LOCATION_PERMISSION_CODE,GOOGLE_SERVICE_CODE,PERMISSION_SETTING_CODE)

        init()
        initControl()
        myObserver()

    }


    override fun init() {
        viewModel=ViewModelProvider(this).get(YouPickViewModel::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.app_green)
        }

        val requestUserData= ArrayList<String>()
        requestUserData.add(getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_USER_ID,"")!!)

        val body= GetResaturnatListBody("1",
                intent.getStringExtra("meetingId")!!,
                intent.getStringArrayListExtra("cuisineList")!!.size.toString()
                ,intent.getStringArrayListExtra("cuisineList")!!
                /*, intent.getStringExtra("distance")!!
                , intent.getStringExtra("latitude")!!
                , intent.getStringExtra("longitude")!!*/
                ,requestUserData)

        viewModel.getResaturnatListApi(this,body,false)
    }

    override fun initControl() {

    }

    override fun myObserver() {

        viewModel.getResaturnatListResponse.observe(this,{
            if(it.status==SUCCESS){

                if(it?.data?.cuisine1Restuarants?.isEmpty()!! && it?.data?.cuisine2Restuarants?.isEmpty()!! && it?.data?.cuisine3Restuarants?.isEmpty()!!){
                    Utils.showMessage(this,"No Restaurant Found!!", MSG_TOAST)
                }else{
                    startActivity(Intent(this@GiveSecActivity, YouPickActivity::class.java)
                            .putExtras(intent.extras!!)
                            .putParcelableArrayListExtra("restroList",getRestroList(it)))
                }

                finish()

            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
                finish()
            }
        })

    }

    private fun getRestroList(it: GetResaturnatListDataModel?): ArrayList<GetResaturnatListDataModel.Data>? {
        var data= ArrayList<GetResaturnatListDataModel.Data>()
        if(!it?.data?.cuisine1Restuarants?.isEmpty()!!) data.addAll(it?.data?.cuisine1Restuarants!!)
        if(!it?.data?.cuisine2Restuarants?.isEmpty()!!) data.addAll(it?.data?.cuisine2Restuarants!!)
        if(!it?.data?.cuisine3Restuarants?.isEmpty()!!) data.addAll(it?.data?.cuisine3Restuarants!!)
        Collections.shuffle(data)
        return data
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
                    Utils.showMessage(this,"Please Allow permission for the security purpose", MSG_TOAST)
                }

            }
        }
    }

    override fun onBackPressed() {

    }
}