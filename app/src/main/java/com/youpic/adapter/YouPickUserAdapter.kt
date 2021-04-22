package com.youpic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetMeetingInviteDetailsDataModel
import com.youpic.apiConnection.DataModel.GetResaturnatListDataModel
import com.youpic.apiConnection.DataModel.GetRestaurantForFinalCallDataModel
import com.youpic.databinding.UserLayoutBinding
import com.youpic.utils.Utils


class YouPickUserAdapter(private val context: Context, var userData: List<GetResaturnatListDataModel.Data.UserData>?) : RecyclerView.Adapter<YouPickUserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: UserLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.user_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        Glide.with(context).load(userData!!.get(position).profilePic).circleCrop().into(holder.binding.profileImageView)
        holder.binding.distanceTextView.text=Utils.roundTwoDecimals(userData!!.get(position).distance).toString()
        holder.binding.distanceTextView.visibility= View.VISIBLE
    }
    override fun getItemCount(): Int {
        if(userData==null){
            return 0
        }else{
            return userData!!.size;
        }

    }

    inner class ViewHolder(val binding: UserLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}