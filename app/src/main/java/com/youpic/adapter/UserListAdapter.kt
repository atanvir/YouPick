package com.youpic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetMeetingInviteDetailsDataModel
import com.youpic.apiConnection.DataModel.GetRestaurantForFinalCallDataModel
import com.youpic.databinding.UserLayoutBinding


class UserListAdapter(private val context: Context, val dataList: ArrayList<GetRestaurantForFinalCallDataModel.Data.FinalRestaurant.User>?) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: UserLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.user_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        Glide.with(context).load(dataList!!.get(position).profilePic).circleCrop().into(holder.binding.profileImageView)

    }
    override fun getItemCount(): Int {
        if(dataList==null){
            return 0
        }else{
            return dataList.size;
        }

    }

    inner class ViewHolder(val binding: UserLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}