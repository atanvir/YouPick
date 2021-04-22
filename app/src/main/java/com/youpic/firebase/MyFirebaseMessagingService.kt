package com.youpic.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.youpic.R
import com.youpic.screens.friendRequestActivity.FriendRequestActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.screens.invitedActivity.InvitedActivity
import com.youpic.screens.notificationActivity.NotificationActivity
import com.youpic.screens.pendingActivity.PendingActivity
import com.youpic.screens.takeImageOptionActivity.TakeImageOptionActivity
import com.youpic.screens.whatActivity.WhatActivity
import com.youpic.screens.winnerActivity.WinnerActivity
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.NOTI_TYPE_FRIEND_REQUEST
import com.youpic.utils.Constants.Companion.NOTI_TYPE_FRIEND_REQ_DECLINE
import com.youpic.utils.Constants.Companion.NOTI_TYPE_MEETING_FINAL_CALL
import com.youpic.utils.Constants.Companion.NOTI_TYPE_MEETING_REQUEST
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_NOTI_COUNT


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val msgMap = remoteMessage.data
        //{body=Hi, varu - Abhi has sent you a friend request., type=frndReuest, sound=default, title=New friend request received, value=5ff4125a285e180fe6b8d671}
        Log.w("testNotification", remoteMessage.data.toString())
        sendNotification((if (msgMap["title"] == null) "YouPick" else msgMap["title"])!!,
                msgMap["body"], msgMap["type"], msgMap["value"], msgMap["notificationId"], msgMap["notiCount"])
      //  sendNotification( msgMap["title"]!!,msgMap["body"]!!,remoteMessage.data)
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).edit().putString(Constants.PREF_DEVICE_TOKEN, s).apply()
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    private fun sendNotification(title: String, messageBody: String?, type: String?, value: String?, notificationId: String?, notiCount: String?)
    {

        var intent: Intent
        val i = Intent("com.youpick")
        var broadcast=true
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(PREF_NOTI_COUNT, notiCount).apply()



        if(type==NOTI_TYPE_FRIEND_REQUEST){

            intent = Intent(this, FriendRequestActivity::class.java)
                    .putExtra("invitedBy", value)
                    .putExtra("notificationid", notificationId)

        }else if(type==NOTI_TYPE_MEETING_REQUEST){
            intent = Intent(this, InvitedActivity::class.java)
                    .putExtra("locationDetail", false)
                    .putExtra("fromInvites", true)
                    .putExtra("meetingId", value)
        }else if(type==NOTI_TYPE_MEETING_FINAL_CALL){
            intent=Intent(this, WhatActivity::class.java)
                    .putExtra("fromDone", true)
                    .putExtra("meetingId", value)
        }else if(type==Constants.NOTI_TYPE_MEETING_REQ_ACCEPT){
            intent=Intent(this, PendingActivity::class.java)
                    .putExtra("meetingId", value)
        }else if(type==Constants.NOTI_TYPE_MEETING_WINNER){
            intent=Intent(this, WinnerActivity::class.java)
                    .putExtra("isWinner", true)
                    .putExtra("meetingId", value)

        }else if(type==Constants.NOTI_TYPE_MEETING_TAKE_PICTURE){

            intent=Intent(this, TakeImageOptionActivity::class.java)
                    .putExtra("notificationId", notificationId)
                    .putExtra("restroId", value)


        }else if(type==NOTI_TYPE_FRIEND_REQ_DECLINE) {
            sendBroadcast(i)
            intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        }else if(type==Constants.NOTI_TYPE_MEETING_CANCEL){
            intent=Intent(this, PendingActivity::class.java)
                    .putExtra("fromInvites", true)
                    .putExtra("fromCancel",true)
                    .putExtra("meetingId", value)

        }
        else{
            intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        }

        if(broadcast){
            sendBroadcast(i)
        }


        intent.putExtra("typeNoti", type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "youpick_noti"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icn)
                .setContentTitle(title + "")
                .setContentText(messageBody + "")
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.good_job))
                .setVibrate(LongArray(0))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
              //  .setFullScreenIntent(pendingIntent, false)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.good_job) //Here is FILE_NAME is the name of file that you want to play


            val attributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            val channel = NotificationChannel(channelId,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.vibrationPattern = LongArray(0)
            channel.setSound(sound, attributes);
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(430, notificationBuilder.build())
    }


    private fun sendNotification(messageTitle: String, messageBody: String, data: Map<String, String>) {

        val channelId = "youpick_noti"

        val mBuilder = NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.icn)
                .setContentTitle(messageTitle).setContentText(messageBody).setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(NotificationCompat.PRIORITY_MAX) //Important for heads-up notification
        val notifyId = System.currentTimeMillis().toInt() //For each push the older one will not be replaced for this unique id
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification"
            val description = "Desx"
            val importance = NotificationManager.IMPORTANCE_HIGH //Important   for heads-up notification
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(true)
            channel.vibrationPattern = LongArray(0)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            val notificationManager = getSystemService(NotificationManager::class.java)
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
                notificationManager.notify(notifyId, mBuilder.build())
            }
        } else {
            val mNotifyMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mNotifyMgr?.notify(notifyId, mBuilder.build())
        }
    }
}