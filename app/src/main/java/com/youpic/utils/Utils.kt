package com.youpic.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.youpic.R
import com.youpic.screens.friendRequestActivity.FriendRequestActivity
import com.youpic.screens.invitedActivity.InvitedActivity
import com.youpic.screens.loginActivity.LoginActivity
import com.youpic.screens.notificationActivity.NotificationActivity
import com.youpic.screens.pendingActivity.PendingActivity
import com.youpic.screens.takeImageOptionActivity.TakeImageOptionActivity
import com.youpic.screens.whatActivity.WhatActivity
import com.youpic.screens.winnerActivity.WinnerActivity
import com.youpic.utils.Constants.Companion.DATE_FORMAT
import com.youpic.utils.Constants.Companion.MSG_DIALOG
import com.youpic.utils.Constants.Companion.MSG_SNACK
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_DEVICE_TOKEN
import com.youpic.utils.Constants.Companion.PREF_NAME
import kotlinx.android.synthetic.main.activity_pending.*
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    fun showMessage(context: Context, msg: String?, type: String):Dialog?{
        var temp=type
        if (msg.equals("Invalid Token", true))
        {
            temp= MSG_DIALOG
        }

        if(temp == MSG_TOAST){
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }else if(temp== MSG_DIALOG){

            val dialog = Dialog(context, android.R.style.Theme_Black)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.layout_message_dialog)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val okTextView = dialog.findViewById<TextView>(R.id.okTextView)
            val messageTextView = dialog.findViewById<TextView>(R.id.messageTextView)

            if (msg.equals("Invalid Token", true))
            {
                messageTextView.text = "This credential is logged in through other device"
            }else{
                messageTextView.text = msg
            }





            okTextView.setOnClickListener{
                if (msg.equals("Invalid Token", true)) {
                    context.startActivity(Intent(context, LoginActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    clearSharedPref(context)
                } else {
                    dismissDialog(dialog)
                }

                /*dismissDialog(dialog)*/
            }

            if (!(context as Activity).isFinishing) {
                dialog.show()
            }

            return dialog
        }else if(temp== MSG_SNACK){
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }




       return null
    }

    fun showMessage(context: Context, msg: String?, type: String, showCancel: Boolean):Dialog?{

        if(type == MSG_TOAST){
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }else if(type== MSG_DIALOG){
            val dialog = Dialog(context, android.R.style.Theme_Black)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.layout_message_dialog)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val okTextView = dialog.findViewById<TextView>(R.id.okTextView)
            val cancelTextView = dialog.findViewById<TextView>(R.id.cancelTextView)
            val messageTextView = dialog.findViewById<TextView>(R.id.messageTextView)

            if(showCancel)
                cancelTextView.visibility= View.VISIBLE
            else
                cancelTextView.visibility=View.GONE


            messageTextView.text = msg




            okTextView.setOnClickListener{
                /*if (msg.equalsIgnoreCase("Invalid Token")) {
                    val token = context.getSharedPreferences(GETFIT, MODE_PRIVATE).getString(DEVICE_TOKEN, "")
                    context.getSharedPreferences(GETFIT, MODE_PRIVATE).edit().clear().apply()
                    AppSingleton.getInstance().setHomeData(null)
                    AppSingleton.getInstance().setWorkPlan(null)
                    AppSingleton.getInstance().setData(null)
                    context.getSharedPreferences(GETFIT, MODE_PRIVATE).edit().putString(DEVICE_TOKEN, token).apply()
                    val intent = Intent(context, LetsGoActivity2::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    dismissDialog(dialog)
                }*/

                dismissDialog(dialog)
            }

            cancelTextView.setOnClickListener {
                dismissDialog(dialog)
            }

            if (!(context as Activity).isFinishing) {
                dialog.show()
            }

            return dialog
        }else if(type== MSG_SNACK){
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }




        return null
    }

    fun dismissDialog(dialog: Dialog?) {
        if (dialog != null && dialog.isShowing) dialog.dismiss()
    }

    fun getUserToken(context: Context):String{
        return context.getSharedPreferences(Constants.PREF_NAME, AppCompatActivity.MODE_PRIVATE).getString(Constants.PREF_USER_TOKEN, "")!!
    }


    fun clearSharedPref(context: Context){
        HomeData.thingsNearApiResponse=null
        HomeData.getActivityDataApiResponse=null
        val deviceToken=context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_DEVICE_TOKEN, "")
      //  val deepLink=context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_DEEPLINK, "")
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().clear().apply()
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(PREF_DEVICE_TOKEN, deviceToken).apply()
    }

    fun printAllExtras(bundle: Bundle){
        for (key in bundle.keySet()) {
            val value: Any? = bundle.get(key)
            Log.d("testIntent", String.format("%s %s (%s)", key,
                    value.toString(), value?.javaClass?.name))
        }
    }

    fun setTimer(context: Context, textView: TextView, startDate: Long, endDate: Long,onCountFinish: OnCountFinish?): CountDownTimer {


        Log.w("testTime", "End Time: " + endDate + " Start Time: " + startDate)

        var showHour=true
        val countDownTimer=object : CountDownTimer((endDate - startDate), 1000){

            override fun onTick(millisUntilFinished: Long) {

                val seconds = ((millisUntilFinished / 1000)  % 60).toInt()
                val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()

                if(hours<=0){
                    textView.text= String.format("%02d mins :%02d seconds",  minutes, seconds)
                    showHour=false

                }else{
                    textView.text= String.format("%02d hrs :%02d mins :%02d seconds", hours, minutes, seconds)
                }
                Log.w("testTime", "OnTick")

            }

            override fun onFinish() {
                Log.w("testTime", "Finish")
                if(showHour)
                    textView.text= String.format("00 hrs:  00 mins : 00 seconds")
                else
                    textView.text= String.format("00 mins : 00 seconds")
                //(context as Activity).finish()
              //  showMessage(context, "Meeting invite already expired!", MSG_TOAST)
                if(onCountFinish!=null)
                {
                    onCountFinish.onCountFinish()
                }
            }
        }

        return countDownTimer
    }

    fun getDateString(date: Date):String{

        /*if(date.compareTo(Calendar.getInstance().time)==0){

        }*/



        Log.w("testDate", date.compareTo(SimpleDateFormat(DATE_FORMAT).parse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().time))).toString())

        if(date.compareTo(SimpleDateFormat(DATE_FORMAT).parse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().time)))==0){
            return "Today"
        }/*else if(date.compareTo(Calendar.getInstance().time)==1){
            return "Tomorrow"
        }*/else{
            return SimpleDateFormat(DATE_FORMAT).format(date)
        }



    }

    fun getTimeString(time: String):String{

        val outputPattern = "h:mm a"
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.US)


        val outputFormat = SimpleDateFormat(outputPattern)

        try {
            return outputFormat.format(simpleDateFormat.parse(time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    fun getServerTimeString(time: String):String{

        val outputPattern = "HH:mm"
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.US)


        val outputFormat = SimpleDateFormat(outputPattern)

        try {
            return outputFormat.format(simpleDateFormat.parse(time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    fun getServerDateString(date: String):String{

        val outputPattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)


        val outputFormat = SimpleDateFormat(outputPattern)

        try {
            return outputFormat.format(simpleDateFormat.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    fun roundTwoDecimals(d: Double): String {
        val twoDForm = DecimalFormat("#.#")
        return twoDForm.format(d*0.000621371).toString()



        //return Math.round(d*0.000621371).toString()
       // return d.toString().split(".")[0]

    }

    fun getImageString(context: Context,url:String): RequestBuilder<Drawable> {

        if(url=="https://res.cloudinary.com/a2karya80559188/image/upload/v1610965599/image_2021_01_18T10_25_09_886Z_dca0sl.png"){
            return Glide.with(context).load(R.drawable.no_images_yet)
        }else{
            return Glide.with(context).load(url)
        }

    }

    fun isDefaultImage(url:String):Boolean{
        return url=="https://res.cloudinary.com/a2karya80559188/image/upload/v1610965599/image_2021_01_18T10_25_09_886Z_dca0sl.png"
    }

    fun getNotificationIntent(context: Context,value:String,notificationId:String,type:String):Intent?{

        var mIntent:Intent?=null


        if(type== Constants.NOTI_TYPE_FRIEND_REQUEST){

            mIntent = Intent(context, FriendRequestActivity::class.java)
                    .putExtra("invitedBy", value)
                    .putExtra("notificationid", notificationId)

        }else if(type== Constants.NOTI_TYPE_MEETING_REQUEST){
            mIntent = Intent(context, InvitedActivity::class.java)
                    .putExtra("locationDetail", true)
                    .putExtra("fromInvites", true)
                    .putExtra("meetingId", value)
        }else if(type== Constants.NOTI_TYPE_MEETING_FINAL_CALL){
            mIntent=Intent(context, WhatActivity::class.java)
                    .putExtra("fromDone", true)
                    .putExtra("meetingId",value)
        }else if(type==Constants.NOTI_TYPE_MEETING_REQ_ACCEPT){
            mIntent=Intent(context, PendingActivity::class.java)
                    .putExtra("meetingId",value)
        }else if(type==Constants.NOTI_TYPE_MEETING_WINNER){
            mIntent=Intent(context, WinnerActivity::class.java)
                    .putExtra("isWinner", true)
                    .putExtra("meetingId",value)

        }else if(type==Constants.NOTI_TYPE_MEETING_TAKE_PICTURE){

            mIntent=Intent(context, TakeImageOptionActivity::class.java)
                    .putExtra("notificationId", notificationId)
                    .putExtra("restroId",value)


        }/*else if(type==Constants.NOTI_TYPE_MEETING_CANCEL){
            mIntent=Intent(context, PendingActivity::class.java)
                    .putExtra("fromInvites", true)
                    .putExtra("meetingId",value)

        }*/



        return mIntent

    }

    interface OnCountFinish{
        fun onCountFinish()
    }
}