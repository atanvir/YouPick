package com.youpic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.databinding.HomeInvitationLayoutBinding
import com.youpic.utils.OverlapDecoration

class HomeInvitationAdapter(private val context: Context, private var incomingData: List<HomeDataDataModel.Data.IncomingData>, private val isPending: Boolean) : RecyclerView.Adapter<HomeInvitationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: HomeInvitationLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_invitation_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*if (size == 1) {
            holder.binding.homeInvitationItemRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            holder.binding.homeInvitationItemRecyclerView.adapter = HomeInvitationItemAdapter(context, 3, isPending)
            holder.binding.homeInvitationItemRecyclerView.addItemDecoration(OverlapDecoration(context))
        } else {
            if (position == 1) {
                holder.binding.homeInvitationItemRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                holder.binding.homeInvitationItemRecyclerView.adapter = HomeInvitationItemAdapter(context, 1, isPending)
                holder.binding.homeInvitationItemRecyclerView.addItemDecoration(OverlapDecoration(context))
            } else if (position == 2) {
                holder.binding.homeInvitationItemRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                holder.binding.homeInvitationItemRecyclerView.adapter = HomeInvitationItemAdapter(context, 3, isPending)
                holder.binding.homeInvitationItemRecyclerView.addItemDecoration(OverlapDecoration(context))
            }
        }
        if (position == size - 1) holder.binding.line.visibility = View.GONE else holder.binding.line.visibility = View.VISIBLE*/
        holder.binding.homeInvitationItemRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        holder.binding.homeInvitationItemRecyclerView.adapter = HomeInvitationItemAdapter(context, incomingData.get(position).memberData, isPending)
        holder.binding.homeInvitationItemRecyclerView.addItemDecoration(OverlapDecoration(context))
        if (position == incomingData.size - 1) holder.binding.line.visibility = View.GONE else holder.binding.line.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return incomingData.size
    }



    inner class ViewHolder( val binding: HomeInvitationLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}