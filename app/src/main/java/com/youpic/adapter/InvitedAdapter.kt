package com.youpic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetMeetingInviteDetailsDataModel
import com.youpic.databinding.InvitedLayoutBinding

class InvitedAdapter(private val context: Context,val dataList: List<GetMeetingInviteDetailsDataModel.Data.Member>) : RecyclerView.Adapter<InvitedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: InvitedLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.invited_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.nameTextView.text=dataList.get(position).memberData.name
        holder.binding.nameInitTextView.text=dataList.get(position).memberData.name[0].toString()
    }
    override fun getItemCount(): Int {
        return dataList.size;
    }

    inner class ViewHolder(val binding: InvitedLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}