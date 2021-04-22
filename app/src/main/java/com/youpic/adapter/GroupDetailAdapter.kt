package com.youpic.adapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetMeetingDetailsDataModel
import com.youpic.databinding.GroupDetailLayoutBinding
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_ID

class GroupDetailAdapter(private val context: Context,var dataList:List<GetMeetingDetailsDataModel.Data.MeetingUsersData>) : RecyclerView.Adapter<GroupDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: GroupDetailLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.group_detail_layout, parent, false)
        //View view= LayoutInflater.from(context).inflate(R.layout.group_detail_layout,parent,false);
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {

        if(dataList.get(position).userId!!.id.equals(context.getSharedPreferences(PREF_NAME,MODE_PRIVATE).getString(PREF_USER_ID,""))){
            holder.binding.nameTextView.text = "You"
        }else{
            holder.binding.nameTextView.text = dataList.get(position).userId!!.name
        }


        holder.binding.statusTextView.text = dataList.get(position).status
        if(dataList.get(position).status.equals("Accept")){
            holder.binding.statusTextView.setTextColor(ContextCompat.getColor(context,R.color.app_green))
        }else{
            holder.binding.statusTextView.setTextColor(ContextCompat.getColor(context,R.color.red))
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(var binding: GroupDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}