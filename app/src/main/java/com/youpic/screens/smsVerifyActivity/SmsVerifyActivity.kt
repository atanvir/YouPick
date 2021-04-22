package com.youpic.screens.smsVerifyActivity

import android.os.Bundle
import com.youpic.R
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.Intent
import android.view.View
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.whatActivity.WhatActivity
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sms_verify.*

class SmsVerifyActivity : BaseActivity()
{
    override fun init() {
        TODO("Not yet implemented")
    }

    /* startActivity(Intent(this, SmsVerifyActivity::class.java)
                        .putExtra("otp",it.data!!.otp)
                        .putExtra("phoneNumber",phoneEditText.text.toString())
                        .putExtra("countryCode",countryCodePicker.selectedCountryCodeWithPlus)
                        .putExtra("name",nameEditText.text.toString().trim())
                        .putExtra("password",passwordEditText.text.toString().trim()))*/
    override fun initControl() {
        verifyButton.setOnClickListener { v: View? ->
            if(checkOtp()){
                startActivity(Intent(this@SmsVerifyActivity, WhatActivity::class.java)
                        .putExtra("fromSignup", true)
                        .putExtra("phoneNumber", intent.getStringExtra("phoneNumber"))
                        .putExtra("countryCode", intent.getStringExtra("countryCode"))
                        .putExtra("country",intent.getStringExtra("country"))
                        .putExtra("name", intent.getStringExtra("name"))
                        .putExtra("password", intent.getStringExtra("password"))
                )
            }
        }
    }

    override fun myObserver() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_sms_verify)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.app_green)
        }

     //   Utils.showMessage(this,intent.getStringExtra("otp"), MSG_TOAST)
        initControl()
    }

    private fun checkOtp():Boolean{
        //if(intent.getStringExtra("otp").equals(otpEditText.text.toString(),true)){
        if(intent.getStringExtra("otp").equals(otpEditText.text.toString(),true)){
            return true
        }else{
            Utils.showMessage(this,"Invalid Otp!",MSG_TOAST)
            return false
        }
    }
}