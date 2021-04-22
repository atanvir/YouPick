package com.youpic.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.youpic.R
import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.databinding.HomeInvitationItemLayoutBinding
import com.youpic.screens.pendingActivity.PendingActivity
import com.youpic.screens.invitedActivity.InvitedActivity

class HomeInvitationItemAdapter(private val context: Context, private val dataList:  List<HomeDataDataModel.Data.IncomingData.MemberData>, private val isPending: Boolean) : RecyclerView.Adapter<HomeInvitationItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: HomeInvitationItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_invitation_item_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(dataList.get(position).memberData!=null){
            if(dataList.get(position).memberData!!.profilePic==null){
                holder.binding.profileImageView.setImageResource(R.drawable.default_profile_image)
            }else{
                Glide.with(context).load(dataList.get(position).memberData!!.profilePic).placeholder(R.drawable.default_profile_image).circleCrop().into(holder.binding.profileImageView)
            }

            if(dataList.get(position).status.equals("Pending")){
                holder.binding.statusImageView.visibility= View.VISIBLE
                holder.binding.statusImageView.setImageResource(R.drawable.sleep)
            }else if(dataList.get(position).status.equals("Accept")){
                holder.binding.statusImageView.visibility= View.VISIBLE
                holder.binding.statusImageView.setImageResource(R.drawable.hand_up)
            }else{
                holder.binding.statusImageView.visibility= View.VISIBLE
                holder.binding.statusImageView.setImageResource(R.drawable.hand_down)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: HomeInvitationItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.parentLayout.setOnClickListener {
                if (isPending)
                    context.startActivity(Intent(context, PendingActivity::class.java).putExtra("meetingId",dataList.get(adapterPosition).meetingId))
                else
                    context.startActivity(Intent(context, InvitedActivity::class.java).putExtra("meetingId",dataList.get(adapterPosition).meetingId))
            }
        }
    }
}