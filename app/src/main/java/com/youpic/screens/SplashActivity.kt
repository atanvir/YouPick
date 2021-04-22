package com.youpic.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.youpic.R
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.screens.invitedActivity.InvitedActivity
import com.youpic.screens.loginActivity.LoginActivity
import com.youpic.utils.Constants.Companion.PREF_DEEPLINK
import com.youpic.utils.Constants.Companion.PREF_DEVICE_TOKEN
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_PICK_FAV
import com.youpic.utils.Constants.Companion.PREF_USER_TOKEN
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getDeviceToken()

        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().remove(PREF_DEEPLINK).apply()



        if (intent != null) {
            val appLinkIntent = intent
            val appLinkData: Uri? = appLinkIntent.data
            if (appLinkData != null) {
                Log.w("testData", "deeplink: $appLinkData")
                getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(PREF_DEEPLINK,appLinkData.toString()).apply()
            }
        }


        Handler(Looper.getMainLooper()).postDelayed({
            if (getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_TOKEN, null) == null)
            {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LocationSharingActivity::class.java))
                }
            } else {
               // startActivity(Intent(this, YouPickActivity::class.java).putExtra("detail2",true))
                getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().remove(PREF_DEEPLINK).apply()
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            }
            finish()
        }, 2000)
    }


    fun getDeviceToken() {
        var token:String?=null
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(OnCompleteListener<String?> { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result

            getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(PREF_DEVICE_TOKEN, token).apply()
            Log.e("token", "" + token)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}