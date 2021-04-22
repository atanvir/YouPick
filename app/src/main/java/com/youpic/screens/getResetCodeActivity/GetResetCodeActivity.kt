package com.youpic.screens.getResetCodeActivity

import android.os.Bundle
import com.youpic.R
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.verificationActivity.VerificationActivity
import com.youpic.screens.loginActivity.LoginActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_get_reset_code.*

class GetResetCodeActivity : BaseActivity(), View.OnClickListener
{
    var viewModel=ResetCodeViewModel()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_get_reset_code)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.app_green)
        }

        init()
        initControl()
        myObserver()


    }

    override fun init() {

        countryCodePicker.registerCarrierNumberEditText(phoneEditText)

        viewModel=ViewModelProvider(this).get(ResetCodeViewModel::class.java)
    }

    override fun initControl() {

        if(intent.getBooleanExtra("fromProfile",false)){
            countryCodePicker.isEnabled=false
            phoneEditText.isEnabled=false

            countryCodePicker.setCountryForPhoneCode(Integer.parseInt(intent.getStringExtra("countryCode")!!.replace("+","")))
            phoneEditText.setText(intent.getStringExtra("phoneNumber"))
        }

        getCodeButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
    }

    override fun myObserver() {
        viewModel.checkMobilForForgotPasswordResponse.observe(this,{
            if(it.status.equals(SUCCESS,false)){
                startActivity(Intent(this@GetResetCodeActivity, VerificationActivity::class.java)
                        .putExtra("fromReset", intent.getBooleanExtra("fromReset", false))
                        .putExtra("otp",it.data.otp)
                        .putExtra("userId",it.data.userId))
            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }

        })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.getCodeButton) {
            if(phoneEditText.text.isEmpty()){
                Utils.showMessage(this,"Please enter phone number", MSG_TOAST)

            }else if(countryCodePicker.isValidFullNumber){
                viewModel.checkMobilForForgotPasswordApi(this,countryCodePicker.selectedCountryCodeWithPlus,phoneEditText.text.toString())
            }else
                Utils.showMessage(this,"Invalid Phone Number", MSG_TOAST)




        } else if (v.id == R.id.cancelButton) {
            if (intent.getBooleanExtra("fromReset", false)) {
                startActivity(Intent(this@GetResetCodeActivity, LoginActivity::class.java))
            } else {
                startActivity(Intent(this@GetResetCodeActivity, HomeActivity::class.java).putExtra("openProfile", true))
            }
        }
    }
}