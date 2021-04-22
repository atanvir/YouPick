package com.youpic.screens.verificationActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.youpic.R
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.Intent
import android.view.View
import com.youpic.screens.newPasswordActivity.NewPasswordActivity
import com.youpic.screens.loginActivity.LoginActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity(), View.OnClickListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_verification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.app_green)
        }
        cancelButton.setOnClickListener(this)
        submitButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.submitButton) {
            if(validate())
                startActivity(Intent(this@VerificationActivity, NewPasswordActivity::class.java)
                        .putExtra("fromReset", intent.getBooleanExtra("fromReset", false))
                        .putExtra("userId",intent.getStringExtra("userId")))
        } else if (v.id == R.id.cancelButton) {
            if (intent.getBooleanExtra("fromReset", false)) {
                startActivity(Intent(this@VerificationActivity, LoginActivity::class.java))
            } else {
                startActivity(Intent(this@VerificationActivity, HomeActivity::class.java).putExtra("openProfile", true))
            }
        }
    }

    private fun validate():Boolean{
      //  return if(intent.getStringExtra("otp").equals(otpEditText.text.toString().trim(),false))
        return if(intent.getStringExtra("otp").equals(otpEditText.text.toString().trim(),false))
            true
        else{
            Utils.showMessage(this,"Invalid Otp",MSG_TOAST)
            false
        }
    }
}