package com.youpic.screens.newPasswordActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.youpic.R
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.util.Util
import com.youpic.baseClass.BaseActivity
import com.youpic.databinding.ActivityNewPasswordBinding
import com.youpic.screens.PasswordUpdatedActivity
import com.youpic.screens.loginActivity.LoginActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_new_password.*

class NewPasswordActivity : BaseActivity(), View.OnClickListener
{

    var newPasswordViewModel=NewPasswordViewModel();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.app_green)
        }


        init()
        initControl()
        myObserver()
    }

    override fun init() {
        newPasswordViewModel=ViewModelProvider(this).get(NewPasswordViewModel::class.java)
    }

    override fun initControl() {
        updateButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
    }

    override fun myObserver() {

        newPasswordViewModel.forgotPasswordresponse.observe(this,{
            startActivity(Intent(this@NewPasswordActivity, PasswordUpdatedActivity::class.java).putExtra("fromReset", intent.getBooleanExtra("fromReset", false)))
        })

    }

    override fun onClick(v: View) {
        if (v.id == R.id.updateButton) {
            if(validate())
                newPasswordViewModel.forgotPasswordApi(this,passwordEditText.text.toString().trim(),intent.getStringExtra("userId")!!)
        } else if (v.id == R.id.cancelButton) {
            if (intent.getBooleanExtra("fromReset", false)) {
                startActivity(Intent(this@NewPasswordActivity, LoginActivity::class.java))
            } else {
                startActivity(Intent(this@NewPasswordActivity, HomeActivity::class.java).putExtra("openProfile", true))
            }
        }
    }

    fun validate():Boolean{
        return when {
            passwordEditText.text.toString().isEmpty() -> {
                Utils.showMessage(this,"Please enter password",MSG_TOAST)
                false
            }
            confirmPasswordEditText.text.toString().isEmpty() -> {
                Utils.showMessage(this,"Please enter confirm password",MSG_TOAST)
                false
            }
            !passwordEditText.text.toString().equals(confirmPasswordEditText.text.toString(),false) -> {
                Utils.showMessage(this,"Both Password Must Be Same",MSG_TOAST)
                false
            }
            passwordEditText.text.toString().length<6 -> {
                Utils.showMessage(this,"Password must have at least 6 digits",MSG_TOAST)
                false
            }
            else -> true
        }
    }
}