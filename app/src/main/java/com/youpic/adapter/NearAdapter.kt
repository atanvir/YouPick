package com.youpic.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.Body.MeetingRequestBody
import com.youpic.databinding.NearLayoutBinding
import com.youpic.screens.WhereActivity
import com.youpic.screens.almostDoneActivity.AlmostDoneActivity
import com.youpic.utils.Constants.Companion.DISTANCE
import com.youpic.utils.FriendList

class NearAdapter(private val context: Context,val dataList:List<MeetingRequestBody.UsersData>) : RecyclerView.Adapter<NearAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: NearLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.near_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == dataList.size-1) {
            holder.binding.subTextView.visibility = View.GONE
            holder.binding.nearTextView.text = "Somewhere Else"
        } else {
            holder.binding.subTextView.visibility = View.VISIBLE
            holder.binding.nearTextView.text = "Near"
            holder.binding.subTextView.text=dataList.get(position).name
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder( val binding: NearLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemLayout.setOnClickListener{
                val mIntent:Intent
                if (adapterPosition == dataList.size-1) {
                    mIntent=Intent(context, WhereActivity::class.java)
                    mIntent.putExtras((context as Activity).intent.extras!!)
                } else {
                    mIntent=Intent(context, AlmostDoneActivity::class.java)
                    FriendList.selectedPosition=adapterPosition
                    mIntent.putExtras((context as Activity).intent.extras!!)
                    mIntent.putExtra("lat",dataList.get(adapterPosition).lat)
                    mIntent.putExtra("long",dataList.get(adapterPosition)._long)
                    mIntent.putExtra("distance","10")

                }
                context.startActivity(mIntent)
            }
        }
    }
}