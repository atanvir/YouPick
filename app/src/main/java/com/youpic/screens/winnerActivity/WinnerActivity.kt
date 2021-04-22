package com.youpic.screens.winnerActivity

import android.app.Activity
import android.os.Bundle
import com.youpic.R
import android.widget.TextView
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.youpic.screens.homeActivity.HomeActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.youpic.adapter.GroupDetailAdapter
import com.youpic.baseClass.BaseActivity
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.DATE_FORMAT
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Location
import com.youpic.utils.Location2
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_winner.*
import java.text.SimpleDateFormat

class WinnerActivity : BaseActivity()
{

    lateinit var viewModel: WinnerViewModel
    var groupStatus:Boolean?=null
    var lat:String?=null
    var _long:String?=null


    private var LOCATION_PERMISSION_CODE:Int=1
    private var GOOGLE_SERVICE_CODE:Int=2
    private var PERMISSION_SETTING_CODE:Int=3

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_winner)

        Location2.startLocation(this,LOCATION_PERMISSION_CODE,GOOGLE_SERVICE_CODE,PERMISSION_SETTING_CODE)

        init()
        initControl()
        myObserver()

        saveGroupLayout.visibility = View.VISIBLE

        saveGroupLayout.setOnClickListener {
            if(groupStatus!=null ){
                if(!groupStatus!!){
                    viewModel.makeGroupApi(this,intent.getStringExtra("meetingId")!!)
                }else{
                    Utils.showMessage(this,"You've already created this group!", MSG_TOAST)
                }

            }

        }

    }

    override fun init() {
        if (intent.getBooleanExtra("isWinner", false)) {
            imageView13.visibility = View.VISIBLE
            (findViewById<View>(R.id.titleTextView) as TextView).text = "We have a winner!"
        } else {
            imageView13.visibility = View.GONE
            doneButton.text = "Close"
            (findViewById<View>(R.id.titleTextView) as TextView).text = "Meeting Details"
        }

        viewModel=ViewModelProvider(this).get(WinnerViewModel::class.java)

        viewModel.getMeetingDetailsApi(this,intent.getStringExtra("meetingId")!!)
    }

    override fun initControl() {
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }
        doneButton.setOnClickListener { startActivity(Intent(this@WinnerActivity, HomeActivity::class.java)) }

        openMapButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" +  Location2.getLat() + "," + Location2.getLong() + "&daddr=" + lat + "," + _long))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
    }

    override fun myObserver() {

        viewModel.getMeetingDetailsResponse.observe(this,{
            if(it.status==SUCCESS){



                groupStatus=it.data.groupCreatedStatus
                detailLayout.visibility=View.VISIBLE
                typeTextView.text=it.data.meetingData.meetForType
                timeTextView.text=Utils.getDateString(SimpleDateFormat(DATE_FORMAT).parse(it.data.meetingData.meetingDate))+" @ "+Utils.getTimeString(it.data.meetingData.meetingTime!!)
                restroNameTextView.text=it.data.meetingData.restaurantName
                restroAddressTextView.text=it.data.meetingData.restaurantAddress

                groupDetailRecycler.layoutManager = LinearLayoutManager(this)
                groupDetailRecycler.adapter = GroupDetailAdapter(this,it.data.meetingUsersData)
                lat=it.data.meetingData.restaurantLatitude
                _long=it.data.meetingData.restaurantLongitude


                var count=0

                for(data in it.data.meetingUsersData){
                    if(!data.userId!!.id!!.equals(getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_USER_ID,""))){
                        count++
                    }
                }

                if(count<=1){
                    saveGroupLayout.visibility=View.INVISIBLE
                }


            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }
        })

        viewModel.makeGroupResponse.observe(this,{
            if(it.status== SUCCESS){
                Utils.showMessage(this,it.message, MSG_TOAST)
                groupStatus=true
            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }
        })
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

}