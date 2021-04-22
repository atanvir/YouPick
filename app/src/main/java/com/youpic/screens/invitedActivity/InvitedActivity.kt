package com.youpic.screens.invitedActivity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.adapter.InvitedAdapter
import com.youpic.apiConnection.Body.MeetingRequestProcessedBody
import com.youpic.apiConnection.Body.RestaurantChoice
import com.youpic.baseClass.BaseActivity
import com.youpic.databinding.ActivityInvitedBinding
import com.youpic.screens.GoodWorkActivity
import com.youpic.screens.declineActivity.DeclineActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.screens.meetUpDetailActivity.MeetUpDetailActivity
import com.youpic.screens.whatActivity.WhatActivity
import com.youpic.screens.youPickActivity.YouPickViewModel
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.DATE_FORMAT
import com.youpic.utils.Constants.Companion.MSG_DIALOG
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_ID
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_invited.*
import java.text.SimpleDateFormat
import java.util.*

class InvitedActivity : BaseActivity(), Utils.OnCountFinish {

    lateinit var viewModel: InvitedViewModel
    lateinit var meetingViewModel:YouPickViewModel
    var countDownTimer: CountDownTimer?=null
    var meetingType=""
    var type=""
    var isNotificationStatus=false
    var onCountFinish=false
    /*var lat=""
    var _long=""
    var distance=""*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invited)

        init()
        initControl()
        myObserver()


    }

    override fun init() {
        (findViewById<View>(R.id.titleTextView) as TextView).text = "You're Invited"
        findViewById<View>(R.id.backTextView).visibility = View.INVISIBLE

        viewModel=ViewModelProvider(this).get(InvitedViewModel::class.java)
        meetingViewModel=ViewModelProvider(this).get(YouPickViewModel::class.java)

        viewModel.getMeetingInviteDetailsApi(this,intent.getStringExtra("meetingId")!!)
    }

    override fun initControl() {
        acceptButton.setOnClickListener {
            if (intent.getBooleanExtra("locationDetail", false)) {
                startActivity(Intent(this@InvitedActivity, HomeActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            } else {

                if(!isNotificationStatus){
                    viewModel.directAcceptMeetingRequestApi(this,intent.getStringExtra("meetingId")!!)
                }else{
                    acceptButton()
                }


            }
        }

        declineButton.setOnClickListener { startActivity(Intent(this@InvitedActivity, DeclineActivity::class.java).putExtras(intent.extras!!)) }
    }

    override fun myObserver() {
        viewModel.getMeetingInviteDetailsResponse.observe(this,{
            if(it.status==SUCCESS){

                meetingType=it.data.meetingData.meetingType
                type=it.data.meetingData.type
                it.data.memberList.get(0).memberData

                /*lat=it.data.meetingData.latitude.toString()
                _long=it.data.meetingData.longitude.toString()
                distance=it.data.meetingData.distance.toString()*/

                detailLayout.visibility=View.VISIBLE
                bottomLayout.visibility=View.VISIBLE

                Glide.with(this).load(it.data.meetingByData.profilePic).circleCrop().into(profileImageView)
                nameTextView.setText(it.data.meetingByData.name+" invited you")

                typeTextView.setText(it.data.meetingData.meetForType)
                timeTextView.setText(Utils.getDateString(SimpleDateFormat(DATE_FORMAT).parse(it.data.meetingData.meetingDate))+" @ "+Utils.getTimeString(it.data.meetingData.meetingTime))

                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")

                var startTime = Calendar.getInstance()

                var endTime = Calendar.getInstance()
                endTime.time = simpleDateFormat.parse(it.data.meetingData.createdAt)

                if(type=="Now"){
                    endTime.add(Calendar.MINUTE, Constants.TOTAL_NOW_MIN)
                }else if(type=="Later Today"){
                    endTime.add(Calendar.HOUR, Constants.TOTAL_LATER_TODAY_HOUR)
                }else{
                    endTime.add(Calendar.HOUR, Constants.TOTAL_ANOTHER_HOUR)
                }


                if(it.data.meetingData.meetingType=="Direct"){
                    restroNameTextView.visibility=View.VISIBLE
                    restroNameTextView.text=it.data.meetingData.restaurantName

                }else{
                    restroNameTextView.visibility=View.GONE
                }





                invitedRecyclerView.layoutManager = LinearLayoutManager(this)
                invitedRecyclerView.adapter = InvitedAdapter(this,it.data.memberList)

                countDownTimer=Utils.setTimer(this,totalTimeTextView,startTime.timeInMillis,endTime.timeInMillis,this)
                countDownTimer!!.start()

                for(userData in it.data.memberList){

                    if(userData.memberData.id==getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_ID,"")){
                        isNotificationStatus=userData.isNotificationSend
                        break
                    }

                }



            }else{
                val dialog=Utils.showMessage(this,it.message, MSG_DIALOG)
                val okTextView = dialog!!.findViewById<TextView>(R.id.okTextView)

                okTextView.setOnClickListener {
                    finish()
                }

            }
        })

        meetingViewModel.meetingRequestProcessedResponse.observe(this,{
            val dialog=Utils.showMessage(this,it.message, MSG_DIALOG)
            val okTextView = dialog!!.findViewById<TextView>(R.id.okTextView)

            okTextView.setOnClickListener {
                startActivity(Intent(this, HomeActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }

        })


        viewModel.directAcceptMeetingRequestResponse.observe(this,{
            if(it.status==SUCCESS){
                isNotificationStatus=true

                if(meetingType=="Direct"){
                    val dialog=Utils.showMessage(this,it.message, MSG_DIALOG)
                    val okTextView = dialog!!.findViewById<TextView>(R.id.okTextView)

                    okTextView.setOnClickListener {
                        startActivity(Intent(this, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    }
                }else{
                    acceptButton()
                }



            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }
        })

        /*meetingViewModel.meetingRequestProcessedResponse.observe(this,{
            if(it.status==SUCCESS){
                if (intent.getBooleanExtra("fromDone", false)) {
                    startActivity(Intent(this@YouPickActivity, MeetUpDetailActivity::class.java).putExtras(intent.extras!!))
                }else{
                    startActivity(Intent(this@YouPickActivity, GoodWorkActivity::class.java).putExtra("fromDone", true))
                }

            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }

        })*/

        //meetingViewModel.meetingRequestProcessedResponse
    }

    override fun onDestroy() {
        super.onDestroy()

        if(countDownTimer!=null){
            countDownTimer!!.cancel()
        }

    }

    override fun onCountFinish(){



        onCountFinish=true



    }

    fun acceptButton(){

        if(meetingType=="Direct"){

            val body= MeetingRequestProcessedBody(null,
                    intent.getStringExtra("meetingId")!!,
                    null)
            meetingViewModel.meetingRequestProcessedApi(this,body)


        }else{
            if(onCountFinish){
                val body=MeetingRequestProcessedBody(null,
                        intent.getStringExtra("meetingId")!!,
                        null)

                meetingViewModel.meetingRequestProcessedApi(this,body)
            }else{
                startActivity(Intent(this@InvitedActivity, WhatActivity::class.java)
                        .putExtras(intent.extras!!)
                        .putExtra("fromInvites", true)
                        /*.putExtra("latitude",lat)
                        .putExtra("longitude",_long)
                        .putExtra("distance",distance)*/)
            }


        }

    }


}