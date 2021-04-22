package com.youpic.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetNotificationListDataModel
import com.youpic.screens.friendRequestActivity.FriendRequestActivity
import com.youpic.screens.invitedActivity.InvitedActivity
import com.youpic.screens.pendingActivity.PendingActivity
import com.youpic.screens.takeImageOptionActivity.TakeImageOptionActivity
import com.youpic.screens.whatActivity.WhatActivity
import com.youpic.screens.winnerActivity.WinnerActivity
import com.youpic.utils.Constants.Companion.NOTI_TYPE_FRIEND_REQUEST
import com.youpic.utils.Constants.Companion.NOTI_TYPE_JOIN
import com.youpic.utils.Constants.Companion.NOTI_TYPE_MEETING_CANCEL
import com.youpic.utils.Constants.Companion.NOTI_TYPE_MEETING_FINAL_CALL
import com.youpic.utils.Constants.Companion.NOTI_TYPE_MEETING_REQUEST
import com.youpic.utils.Constants.Companion.NOTI_TYPE_MEETING_REQ_ACCEPT
import com.youpic.utils.Constants.Companion.NOTI_TYPE_MEETING_TAKE_PICTURE
import com.youpic.utils.Constants.Companion.NOTI_TYPE_MEETING_WINNER
import kotlinx.android.synthetic.main.notification_layout.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(private val context: Context, var list: List<GetNotificationListDataModel.Data?>? = null,var takePicture: OnTakePicture) :
        RecyclerView.Adapter<NotificationAdapter.ViewHolder>()
{

    interface OnTakePicture{
        fun takePicture(notificationId:String,restroId:String,position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.itemView.titleTextView.text=list?.get(position)!!.notiTitle
        holder.itemView.msgTextView.text=list?.get(position)!!.notiMessage
        holder.itemView.dateTextView.text=parseDateToddMMyyyy(list?.get(position)!!.updatedAt)
        if(list!!.get(position)!!.notiType == NOTI_TYPE_FRIEND_REQUEST ||
                list!!.get(position)!!.notiType ==  NOTI_TYPE_MEETING_REQUEST ||
                list!!.get(position)!!.notiType == NOTI_TYPE_MEETING_REQ_ACCEPT ||
                list!!.get(position)!!.notiType == NOTI_TYPE_MEETING_FINAL_CALL ||
                list!!.get(position)!!.notiType == NOTI_TYPE_MEETING_TAKE_PICTURE ||
                list!!.get(position)!!.notiType == NOTI_TYPE_MEETING_WINNER ||
                list!!.get(position)!!.notiType == NOTI_TYPE_MEETING_CANCEL){
            holder.itemView.viewTextView.visibility=View.VISIBLE
        }else{
            holder.itemView.viewTextView.visibility=View.GONE
        }

    }

    override fun getItemCount(): Int {
       return list?.size ?: return 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener {
                if(list!!.get(adapterPosition)!!.notiType.equals(NOTI_TYPE_FRIEND_REQUEST, true))
                {
                    (context as Activity).startActivityForResult(Intent(context, FriendRequestActivity::class.java)
                            .putExtra("invitedBy", list!![adapterPosition]?.data)
                            .putExtra("notificationid", list!!.get(adapterPosition)?.id), 121)
                }else if(list!!.get(adapterPosition)!!.notiType.equals(NOTI_TYPE_MEETING_REQUEST, true)){
                    context.startActivity(Intent(context, InvitedActivity::class.java)
                            .putExtra("locationDetail", (context as Activity).intent.getBooleanExtra("locationDetail", false))
                            .putExtra("fromInvites", true)
                            .putExtra("meetingId",list!![adapterPosition]?.data))
                }else if(list!!.get(adapterPosition)!!.notiType.equals(NOTI_TYPE_MEETING_REQ_ACCEPT, true)){
                    context.startActivity(Intent(context, PendingActivity::class.java)
                            .putExtra("fromInvites", true)
                            .putExtra("meetingId",list!![adapterPosition]?.data))
                }else if(list!!.get(adapterPosition)!!.notiType.equals(NOTI_TYPE_MEETING_FINAL_CALL, true)){
                    context.startActivity(Intent(context, WhatActivity::class.java)
                            .putExtra("fromDone", true)
                            .putExtra("meetingId",list!![adapterPosition]?.data))
                }else if(list!!.get(adapterPosition)!!.notiType.equals(NOTI_TYPE_MEETING_TAKE_PICTURE, true)){

                    context.startActivity(Intent(context, TakeImageOptionActivity::class.java)
                            .putExtra("notificationId", list!!.get(adapterPosition)?.id!!)
                            .putExtra("restroId",list!!.get(adapterPosition)?.data!!))


                    //takePicture.takePicture(list!!.get(adapterPosition)?.id!!,list!!.get(adapterPosition)?.data!!,adapterPosition)
                }else if(list!!.get(adapterPosition)!!.notiType.equals(NOTI_TYPE_MEETING_WINNER, true)){
                    context.startActivity(Intent(context, WinnerActivity::class.java)
                            .putExtra("isWinner", true)
                            .putExtra("meetingId",list!![adapterPosition]?.data))

                }else if(list!!.get(adapterPosition)!!.notiType==NOTI_TYPE_MEETING_CANCEL){
                    context.startActivity(Intent(context, PendingActivity::class.java)
                            .putExtra("fromInvites", true)
                            .putExtra("fromCancel",true)
                            .putExtra("meetingId",list!![adapterPosition]?.data))

                }
            }
        }

    }

    fun parseDateToddMMyyyy(time: String?): String? {
        //2021-01-19T11:50:30.099Z
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        val outputPattern = "dd-MM-yyyy @ h:mma"
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")


        val outputFormat = SimpleDateFormat(outputPattern)
        var str: String? = null
        try {
            str = outputFormat.format(simpleDateFormat.parse(time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }
}