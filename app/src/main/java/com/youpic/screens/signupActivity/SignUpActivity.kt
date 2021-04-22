package com.youpic.screens.signupActivity

import android.os.Bundle
import com.youpic.R
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.smsVerifyActivity.SmsVerifyActivity
import com.youpic.utils.Constants.Companion.MSG_SNACK
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Matcher

class SignUpActivity : BaseActivity()
{

    lateinit var signUpViewModel:SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_sign_up)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.app_green)
        }
        init()
        initControl()
        myObserver()
    }


    override fun init() {
        signUpViewModel=ViewModelProvider(this).get(SignUpViewModel::class.java)
        countryCodePicker.registerCarrierNumberEditText(phoneEditText)
    }

    override fun initControl() {
        nextButton.setOnClickListener {
            if(validate()) signUpViewModel.checkUserMobileNumberApi(this,countryCodePicker.selectedCountryCodeWithPlus,phoneEditText.text.toString().trim())
        }
    }

    override fun myObserver()
    {
        signUpViewModel.checkUserMobileResponse.observe(this,{
            if(it.status.equals(SUCCESS,true)){
                startActivity(Intent(this, SmsVerifyActivity::class.java)
                        .putExtra("otp",it.data!!.otp)
                        .putExtra("phoneNumber",phoneEditText.text.toString())
                        .putExtra("countryCode",countryCodePicker.selectedCountryCodeWithPlus)
                        .putExtra("country",countryCodePicker.selectedCountryNameCode)
                        .putExtra("name",nameEditText.text.toString().trim())
                        .putExtra("password",passwordEditText.text.toString().trim()))
            }else{
                Utils.showMessage(this,it.message, MSG_SNACK)
            }
        })
    }



    private fun validate():Boolean{
        if(phoneEditText.text.toString().trim().isEmpty()){
            Utils.showMessage(this,"Please enter your phone number!", MSG_TOAST)
            return false
        }else if(nameEditText.text.toString().trim().isEmpty())
        {
            Utils.showMessage(this,"Please enter your screen name!", MSG_TOAST)
            return false
        }else if(passwordEditText.text.toString().trim().isEmpty()){
            Utils.showMessage(this,"Please enter your password!", MSG_TOAST)
            return false
        }else if(passwordEditText.text.toString().length<6){
            Utils.showMessage(this,"Password must be at least 6 digits!", MSG_TOAST)
            return false
        }else if(!countryCodePicker.isValidFullNumber)
        {
            Utils.showMessage(this,"Invalid Phone Number!", MSG_TOAST)
            return false
        }
        return true
    }

    fun validatePassword(password:String):Boolean{
        var rejex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}".toRegex()
        return rejex.matches(password)
    }
}