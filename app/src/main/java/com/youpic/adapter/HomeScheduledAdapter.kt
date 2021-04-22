package com.youpic.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.youpic.R
import android.content.Intent
import android.util.Log
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.databinding.HomeScheduledLayoutBinding
import com.youpic.screens.winnerActivity.WinnerActivity
import com.youpic.utils.Constants.Companion.DATE_FORMAT
import com.youpic.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class HomeScheduledAdapter(private val context: Context,var dataList:List<HomeDataDataModel.Data.ScheduleData>) : RecyclerView.Adapter<HomeScheduledAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val binding: HomeScheduledLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_scheduled_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
        holder.binding.dateTextView.text=Utils.getDateString(simpleDateFormat.parse(dataList.get(position).meetingDate))
        holder.binding.nameTextView.text=dataList.get(position).restaurantName
        holder.binding.timeTextView.text=Utils.getTimeString(dataList.get(position).meetingTime)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder( val binding: HomeScheduledLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        init
        {
            binding.parentLayout.setOnClickListener { context.startActivity(Intent(context, WinnerActivity::class.java)
                    .putExtra("isWinner", false)
                    .putExtra("meetingId",dataList[adapterPosition].id)) }
        }
    }
}