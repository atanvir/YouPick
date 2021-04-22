package com.youpic.screens.friendRequestActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.youpic.R
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.youpic.baseClass.BaseActivity
import com.youpic.databinding.ActivityFriendRequestBinding
import com.youpic.utils.Constants.Companion.MSG_DIALOG
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_friend_request.*
import kotlinx.android.synthetic.main.layout_message_dialog.*

class FriendRequestActivity : BaseActivity(),View.OnClickListener {


    var viewModel=FriendRequestViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_friend_request)


        init()
        initControl()
        myObserver()



    }


    override fun init() {
        viewModel=ViewModelProvider(this).get(FriendRequestViewModel::class.java)

        (findViewById<View>(R.id.titleTextView) as TextView).text = "Friend Request"

        viewModel.getInviteByDataApi(this, intent.getStringExtra("invitedBy")!!)

    }

    override fun initControl() {

        findViewById<View>(R.id.backTextView).setOnClickListener(this)

        yesButton.setOnClickListener(this)
        noButton.setOnClickListener(this)

    }

    override fun myObserver() {

        viewModel.getInviteByDataResponse.observe(this,{
            if(it.status.equals(SUCCESS,true)){
                Glide.with(this).load(it.data.profilePic).placeholder(R.drawable.default_profile_image).circleCrop().into(profileImageView)
                nameTextView.text=it.data.name
            }
        })

        viewModel.acceptOrDeclinedFrndRequestResponse.observe(this,{

            if(it.status.equals(SUCCESS,true)){
                val dialog=Utils.showMessage(this,it.message, MSG_DIALOG)
                (dialog?.findViewById<TextView>(R.id.okTextView))?.setOnClickListener{
                    this.finish()
                    setResult(RESULT_OK)
                }
            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }

        })
    }

    override fun onClick(v: View) {
        if(v.id==R.id.backTextView){
            finish()
        }else if(v.id==R.id.yesButton){
            viewModel.acceptOrDeclinedFrndRequestApi(this,
                    intent.getStringExtra("invitedBy")!!,
                    intent.getStringExtra("notificationid")!!,
                    "Accept")
        }else if(v.id==R.id.noButton){
            viewModel.acceptOrDeclinedFrndRequestApi(this,
                    intent.getStringExtra("invitedBy")!!,
                    intent.getStringExtra("notificationid")!!,
                    "Decline")

        }
    }
}