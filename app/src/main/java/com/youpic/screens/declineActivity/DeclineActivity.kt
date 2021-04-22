package com.youpic.screens.declineActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.youpic.R
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.youpic.baseClass.BaseActivity
import com.youpic.databinding.ActivityDeclineBinding
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_decline.*

class DeclineActivity : BaseActivity()
{


    lateinit var viewModel: DeclineViewModel
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_decline)

        init()
        initControl()
        myObserver()

    }

    override fun init() {
        viewModel=ViewModelProvider(this).get(DeclineViewModel::class.java)
    }

    override fun initControl() {
        noButton.setOnClickListener { finish() }
        yesButton.setOnClickListener {
            viewModel.cancelMeetingRequestApi(this,intent.getStringExtra("meetingId")!!)
        }

    }

    override fun myObserver() {
        viewModel.cancelMeetingRequestResponse.observe(this,{
            if(it.status==SUCCESS){
                startActivity(Intent(this@DeclineActivity, HomeActivity::class.java))
            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }
        })
    }

}