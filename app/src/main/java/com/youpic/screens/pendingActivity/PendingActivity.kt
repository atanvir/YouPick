package com.youpic.screens.pendingActivity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.adapter.GroupDetailAdapter
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.screens.winnerActivity.WinnerViewModel
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.DATE_FORMAT
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_PROFILE
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Constants.Companion.TOTAL_HOUR
import com.youpic.utils.Utils
import com.youpic.utils.Utils.setTimer
import kotlinx.android.synthetic.main.activity_pending.*
import java.text.SimpleDateFormat
import java.util.*

class PendingActivity : BaseActivity() {


    lateinit var viewModel:WinnerViewModel
    lateinit var countDownTimer:CountDownTimer
    var type=""


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending)


        init()
        initControl()
        myObserver()


    }


    override fun init() {
        if(intent.getBooleanExtra("fromCancel",false)){
            (findViewById<View>(R.id.titleTextView) as TextView).text = "Cancel"
        }else{
            (findViewById<View>(R.id.titleTextView) as TextView).text = "Pending"
        }



        viewModel=ViewModelProvider(this).get(WinnerViewModel::class.java)

        viewModel.getMeetingDetailsApi(this, intent.getStringExtra("meetingId")!!)
    }

    override fun initControl() {
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }
        doneButton.setOnClickListener { startActivity(Intent(this@PendingActivity, HomeActivity::class.java)) }
    }

    override fun myObserver() {
        viewModel.getMeetingDetailsResponse.observe(this, {
            if (it.status == SUCCESS) {

                detailLayout.visibility=View.VISIBLE
                view22.visibility=View.VISIBLE
                doneButton.visibility=View.VISIBLE

                Glide.with(this).load(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_PROFILE, "")).circleCrop().into(profileImageView)
                typeTextView.text = it.data.meetingData.meetForType
                dateTypeTextView.text = Utils.getDateString(SimpleDateFormat(DATE_FORMAT).parse(it.data.meetingData.meetingDate)) + " @ " + Utils.getTimeString(it.data.meetingData.meetingTime!!)


                if(intent.getBooleanExtra("fromCancel",false)){
                    textView67.visibility=View.GONE
                    textView68.visibility=View.GONE
                    timeTextView.visibility=View.GONE
                 //   view21.visibility=View.GONE
                }else{

                    textView67.visibility=View.VISIBLE
                    textView68.visibility=View.VISIBLE
                    timeTextView.visibility=View.VISIBLE
                   // view21.visibility=View.VISIBLE

                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")

                    var startTime = Calendar.getInstance()

                    var endTime = Calendar.getInstance()
                    endTime.time = simpleDateFormat.parse(it.data.meetingData.createdAt)

                    type=it.data.meetingData.type!!

                    if(type=="Now"){
                        endTime.add(Calendar.MINUTE, Constants.TOTAL_NOW_MIN)
                    }else if(type=="Later Today"){
                        endTime.add(Calendar.HOUR, Constants.TOTAL_LATER_TODAY_HOUR)
                    }else{
                        endTime.add(Calendar.HOUR, Constants.TOTAL_ANOTHER_HOUR)
                    }







                    countDownTimer=setTimer(this,timeTextView, startTime.timeInMillis, endTime.timeInMillis,null)

                    countDownTimer.start()

                }



                pendingRecyclerView.layoutManager = LinearLayoutManager(this)
                pendingRecyclerView.adapter = GroupDetailAdapter(this, it.data.meetingUsersData)

            } else {
                Utils.showMessage(this, it.message, MSG_TOAST)
            }
        })
    }


    override fun onStop() {
        super.onStop()
        try{
            countDownTimer.cancel()
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}