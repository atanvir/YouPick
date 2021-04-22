package com.youpic.screens.loginActivity


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.youpic.R
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.getResetCodeActivity.GetResetCodeActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.screens.signupActivity.SignUpActivity
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.ANDROID
import com.youpic.utils.Constants.Companion.MSG_DIALOG
import com.youpic.utils.Constants.Companion.MSG_SNACK
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_DEVICE_TOKEN
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_TOKEN
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Utils

import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : BaseActivity(), View.OnClickListener
{

    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.app_green)
        }

        init()
        initControl()
        myObserver()

    }

    override fun init() {

        countryCodePicker.registerCarrierNumberEditText(phoneEditText)

        loginViewModel= ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun initControl() {
        loginButton.setOnClickListener(this)
        signUpTextView.setOnClickListener(this)
        resetPasswordTextView.setOnClickListener(this)
    }

    override fun myObserver() {
        loginViewModel.loginResponse.observe(this, Observer {
            if(it.status.equals(SUCCESS,true)){
                getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().remove(Constants.PREF_DEEPLINK).apply()
                startActivity(Intent(this, HomeActivity::class.java));
            }else{

                if(it.status == "424"){
                    val dialog=Utils.showMessage(this,it.message, MSG_DIALOG,true)
                    (dialog?.findViewById<TextView>(R.id.okTextView))?.setOnClickListener{
                        Utils.dismissDialog(dialog)
                        loginViewModel.directLoginApi(this,
                                countryCodePicker.selectedCountryCodeWithPlus,
                                phoneEditText.text.toString(),
                                // TimeZone.getDefault().getDisplayName(false,TimeZone.LONG),
                                TimeZone.getDefault().id,
                                ANDROID,
                                getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_DEVICE_TOKEN,""),
                                passwordEditText.text.toString())
                    }
                }else{
                    Utils.showMessage(this, it.message, MSG_SNACK)
                }

            }
        })

        loginViewModel.error.observe(this, { Utils.showMessage(this,"Connection Error", MSG_SNACK) })

    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.loginButton) {
            if(validate()){
                loginViewModel.userLoginApi(this,
                        countryCodePicker.selectedCountryCodeWithPlus,
                        phoneEditText.text.toString(),
                       // TimeZone.getDefault().getDisplayName(false,TimeZone.LONG),
                        TimeZone.getDefault().id,
                        ANDROID,
                        getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_DEVICE_TOKEN,""),
                        passwordEditText.text.toString())
            }
        } else if (id == R.id.signUpTextView) {
            startActivity(Intent(this, SignUpActivity::class.java))
        } else if (id == R.id.resetPasswordTextView) {

            startActivity(Intent(this, GetResetCodeActivity::class.java).putExtra("fromReset", true))


        }
    }

    fun validate():Boolean{

        if(phoneEditText.text.isEmpty()){
            Utils.showMessage(this,"Please enter phone number", MSG_TOAST)
            return false
        }
        else if(passwordEditText.text.toString().isEmpty()){
            Utils.showMessage(this,"Please Enter Password", MSG_TOAST)
            return false
        }else if(!countryCodePicker.isValidFullNumber){
            Utils.showMessage(this,"Invalid Phone Number", MSG_TOAST)
            return false
        }



        return true
    }
}
