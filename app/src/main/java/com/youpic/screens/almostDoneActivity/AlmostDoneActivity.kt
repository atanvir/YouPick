package com.youpic.screens.almostDoneActivity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.adapter.AlmostDoneAdapter
import com.youpic.apiConnection.Body.DirectMeetingRequestBody
import com.youpic.apiConnection.Body.MeetingRequestBody
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.GoodWorkActivity
import com.youpic.utils.Constants.Companion.DATE_FORMAT
import com.youpic.utils.Constants.Companion.DISTANCE
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_PROFILE
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.FriendList
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_almost_done.*
import java.text.SimpleDateFormat

class AlmostDoneActivity : BaseActivity() {

    lateinit var viewModel: AlmostDoneViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_almost_done)

        Glide.with(this).load(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_PROFILE,"")).circleCrop().into(profileImageView)
        typeTextView.text=intent.getStringExtra("occasion")
        timeTextView.text=Utils.getDateString(SimpleDateFormat(DATE_FORMAT).parse(intent.getStringExtra("whenDate")))+" @ "+Utils.getTimeString(intent.getStringExtra("whenTime")!!)

        almostDoneRecyclerView.layoutManager = LinearLayoutManager(this)
        val tempList=FriendList.getFriendList(this).toMutableList()
        tempList.add(0,MeetingRequestBody.UsersData("","You","",""))
        almostDoneRecyclerView.adapter = AlmostDoneAdapter(this,tempList)

        init()
        initControl()
        myObserver()

        Utils.printAllExtras(intent.extras!!)



    }

    override fun init() {
        (findViewById<View>(R.id.titleTextView) as TextView).text = "Looks Good?"

        if (intent.getBooleanExtra("locationDetail", false)) {
            var data: HomeDataDataModel.Data.Restaurant? = null
            data=intent.getParcelableExtra("locationData")

            if(data!=null){
                restroNameTextView.visibility = View.VISIBLE
                restroAddressTextView.visibility = View.VISIBLE

                restroNameTextView.text=data.name
                restroAddressTextView.text=data.address
            }

        } else {
            restroNameTextView.visibility = View.GONE
            restroAddressTextView.visibility = View.GONE
        }


        viewModel=ViewModelProvider(this).get(AlmostDoneViewModel::class.java)
    }

    override fun initControl() {
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }

        doneButton.setOnClickListener {
            //occasion,when,whenDate,whenTime
            Log.w("testTime","Time: "+intent.getStringExtra("whenTime")+" TimeFormated: "+Utils.getServerTimeString(intent.getStringExtra("whenTime")!!)
                    +" Date: "+intent.getStringExtra("whenDate")+" DateFormatted: "+Utils.getServerDateString(intent.getStringExtra("whenDate")!!))
            if(intent.getBooleanExtra("locationDetail", false)){
               // lateinit var body: DirectMeetingRequestBody
                var data: HomeDataDataModel.Data.Restaurant? = null
                data=intent.getParcelableExtra("locationData")

                var body= DirectMeetingRequestBody(intent.getStringExtra("occasion")!!,
                        intent.getStringExtra("whenDate"),
                        Utils.getServerTimeString(intent.getStringExtra("whenTime")!!),
                       data!!.address!!,
                        data!!.id!!,
                        data!!.latitude!!,
                        data!!.longitude!!,
                        data!!.name!!,
                        intent.getStringExtra("when")!!,
                        FriendList.getFriendList(this))
                viewModel.directMeetingRequestApi(this,body)
            }else{

                var body= MeetingRequestBody(null,
                        intent.getStringExtra("distance")!!,
                        intent.getStringExtra("lat")!!,
                        intent.getStringExtra("long")!!,
                        intent.getStringExtra("occasion")!!,
                        intent.getStringExtra("whenDate"),
                        Utils.getServerTimeString(intent.getStringExtra("whenTime")!!),
                        null,
                        "User",
                        intent.getStringExtra("when")!!,
                        FriendList.getFriendList(this))
                viewModel.meetingRequestApi(this,body)
            }


        }
    }

    override fun myObserver() {
        viewModel.meetingRequestResponse.observe(this,{
            if(it.status==SUCCESS){
                startActivity(Intent(this@AlmostDoneActivity, GoodWorkActivity::class.java)
                        .putExtra("locationDetail", intent.getBooleanExtra("locationDetail", false)))
            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }
        })

        viewModel.directMeetingRequestResponse.observe(this,{

            if(it.status== SUCCESS){
                startActivity(Intent(this@AlmostDoneActivity, GoodWorkActivity::class.java)
                        .putExtra("locationDetail", intent.getBooleanExtra("locationDetail", false)))
            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }

        })
    }

}