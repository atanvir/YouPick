package com.youpic.screens.homeActivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.baseClass.BaseActivity
import com.youpic.fragment.appInvitesFragment.AppInvitesFragment
import com.youpic.fragment.friendsFragment.FriendsFragment
import com.youpic.fragment.homeFragment.HomeFragment
import com.youpic.fragment.profileFragment.ProfileFragment
import com.youpic.screens.OccasionActivity
import com.youpic.screens.notificationActivity.NotificationActivity
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.PREF_DEEPLINK
import com.youpic.utils.Constants.Companion.PREF_NAME
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_titlebar.*
import kotlinx.android.synthetic.main.home_titlebar.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : BaseActivity(), View.OnClickListener
{
    private var lastButton = 0
    private var backButtonCount = 0
    lateinit var viewModel:HomeViewModel
    var homeFrag=HomeFragment()
    var friendsFrag=FriendsFragment()
    var appInviteFrag=AppInvitesFragment()
    var profileFrag=ProfileFragment()
    var broadcastReceiver:BroadcastReceiver?=null


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState.containsKey("state")){
            setFragment(R.id.profileImageView)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        lastButton = R.id.homeImageViewLayout


        init()
        initControl()
        myObserver()



        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {



                setNotificationCount()
            }
        }

    }



    override fun onResume() {
        super.onResume()
        backButtonCount = 0



        if(broadcastReceiver!=null)
        {

            registerReceiver(broadcastReceiver, IntentFilter("com.youpick"));

        }
        setNotificationCount()

    }

    override fun init() {

        viewModel=ViewModelProvider(this).get(HomeViewModel::class.java)

        if(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_DEEPLINK, null)!=null){
            val deepLink=getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_DEEPLINK, null)!!.split("/")
            Log.w("testData", "Inside DeepLink: " + deepLink.get(deepLink.size - 1))
            viewModel.addFriendApi(this, deepLink.get(deepLink.size - 1))
        }


        setNotificationCount()






        when {
            intent.getBooleanExtra("showFriends", false) -> {
                setFragment(R.id.friendImageViewLayout)
            }
            intent.getBooleanExtra("openProfile", false) -> {
                setFragment(R.id.profileImageViewLayout)
            }
            else -> {
                setFragment(R.id.homeImageViewLayout)
            }
        }



        findViewById<View>(R.id.notificationImageView).setOnClickListener { startActivity(Intent(this@HomeActivity, NotificationActivity::class.java)) }
    }

    override fun initControl() {
        homeImageViewLayout.setOnClickListener(this)
        friendImageViewLayout.setOnClickListener(this)
        favImageViewLayout.setOnClickListener(this)
        profileImageViewLayout.setOnClickListener(this)
        addLayout.setOnClickListener(this)
    }

    override fun myObserver() {

    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.homeImageViewLayout || id == R.id.friendImageViewLayout || id == R.id.favImageViewLayout || id == R.id.profileImageViewLayout) {
            setFragment(id)
        } else if (id == R.id.addLayout) {
            startActivity(Intent(this@HomeActivity, OccasionActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if (backButtonCount >= 1) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show()
            Handler(Looper.myLooper()!!).postDelayed({ backButtonCount = 0 }, 2000)
            backButtonCount++
        }
    }



    private fun setFragment(id: Int) {
        if (lastButton == R.id.homeImageViewLayout) {
            homeImageView.setImageResource(R.drawable.home_un)
            homeTextView.setTextColor(ContextCompat.getColor(this,R.color.text_grey))
        }
        else if (lastButton == R.id.friendImageViewLayout) {
            friendImageView.setImageResource(R.drawable.friend_un)
            friendTextView.setTextColor(ContextCompat.getColor(this,R.color.text_grey))
        }
        else if (lastButton == R.id.favImageViewLayout) {
            favImageView2.setImageResource( R.drawable.invites_un)
            inviteTextView2.setTextColor(ContextCompat.getColor(this,R.color.text_grey))
        }
        else if (lastButton == R.id.profileImageViewLayout) {
            profileImageView.setImageResource(R.drawable.profile_un)
            profileTextView.setTextColor(ContextCompat.getColor(this,R.color.text_grey))
        }
        if (id == R.id.homeImageViewLayout) {

            Glide.with(this)
                    .load(getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                            .getString(Constants.PREF_USER_PROFILE, "")).placeholder(R.drawable.default_profile_image)
                    .circleCrop()
                    .into(profileTitleImageView)
            nameTextView.text="Hi, "+getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_USER_NAME, "User")+"!"

            homeImageView.setImageResource(R.drawable.home_s)
            homeTextView.setTextColor(ContextCompat.getColor(this,R.color.app_green))
            (findViewById(R.id.titleBar) as View).visibility = View.VISIBLE
            (findViewById(R.id.titleBar2) as View).visibility = View.INVISIBLE
            (findViewById(R.id.titleBar3) as View).visibility = View.INVISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, homeFrag).commit()
        }
        else if (id == R.id.friendImageViewLayout) {
            friendImageView.setImageResource(R.drawable.friend_s)
            friendTextView.setTextColor(ContextCompat.getColor(this,R.color.app_green))
            (findViewById(R.id.titleBar) as View).visibility = View.INVISIBLE
            (findViewById(R.id.titleBar3) as View).visibility = View.INVISIBLE
            (findViewById(R.id.titleBar2) as View).visibility = View.VISIBLE
            (findViewById(R.id.backTextView) as View).visibility = View.INVISIBLE
            (findViewById<View>(R.id.titleTextView) as TextView).text = "Friends"
            val bundle = Bundle()
            bundle.putBoolean("openGroups", intent.getBooleanExtra("openGroups", false))
            //  friendsFrag
            friendsFrag.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, friendsFrag).commit()
        }
        else if (id == R.id.favImageViewLayout) {
            favImageView2.setImageResource(R.drawable.invites_s)
            inviteTextView2.setTextColor(ContextCompat.getColor(this,R.color.app_green))
            (findViewById(R.id.titleBar) as View).visibility = View.INVISIBLE
            (findViewById(R.id.titleBar2) as View).visibility = View.INVISIBLE
            (findViewById(R.id.titleBar3) as View).visibility = View.VISIBLE
            (findViewById<View>(R.id.titleTextView) as TextView).text = "App Invites"
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, appInviteFrag).commit()
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
        else if (id == R.id.profileImageViewLayout) {
            profileImageView.setImageResource(R.drawable.profile_s)
            profileTextView.setTextColor(ContextCompat.getColor(this,R.color.app_green))
            (findViewById(R.id.titleBar) as View).visibility = View.INVISIBLE
            (findViewById(R.id.titleBar3) as View).visibility = View.INVISIBLE
            (findViewById(R.id.titleBar2) as View).visibility = View.VISIBLE
            (findViewById(R.id.backTextView) as View).visibility = View.INVISIBLE
            (findViewById<View>(R.id.titleTextView) as TextView).text = "Profile"
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, profileFrag).commit()
        }
        lastButton = id
    }

    fun setTitle(title: String?) {
        (findViewById<View>(R.id.titleTextView) as TextView).text = title
    }

    fun setNotificationCount(){
        if(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_NOTI_COUNT, "0").equals("0", true)){
            notifCountTextView.visibility=View.GONE
            noNotiTextView.visibility=View.VISIBLE
        }else{
            notifCountTextView.visibility=View.VISIBLE
            noNotiTextView.visibility=View.GONE
            notifCountTextView.text=getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_NOTI_COUNT, "0")
        }

        pointTextView.text=getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_POINT, "0")
        badgeNameTextView.text=getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_BADGE_NAME, "0")
        Glide.with(this).load(getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_BADGE_IMG, "")).into(badgeImageView)
    }

    override fun onPause() {
        super.onPause()
        if(broadcastReceiver!=null)
        {
            unregisterReceiver(broadcastReceiver);
        }

    }


}