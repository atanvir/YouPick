package com.youpic.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetRestaurantForFinalCallDataModel
import com.youpic.databinding.MeetUpDetailLayoutBinding

class MeetUpDetailAdapter(private val context: Context, var dataList:List<GetRestaurantForFinalCallDataModel.Data.FinalRestaurant>, val onRestroClickListener: OnRestroClickListener?) : RecyclerView.Adapter<MeetUpDetailAdapter.ViewHolder>()
{
    private var pos = -1

    interface OnRestroClickListener{
        fun onClick(selectedRestro:GetRestaurantForFinalCallDataModel.Data.FinalRestaurant,pos:Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MeetUpDetailLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.meet_up_detail_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.nameTextView.text=dataList.get(position).restaurant.name
        holder.binding.addressTextView.text=dataList.get(position).restaurant.address

        if (position == pos) {
            holder.binding.boxLayout.background = ContextCompat.getDrawable(context, R.drawable.round_corner_white_stroke_green)
        } else {
            holder.binding.boxLayout.background = ContextCompat.getDrawable(context, R.drawable.round_corner_white)
        }

        holder.binding.initTextView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.binding.initTextView.adapter=UserListAdapter(context,dataList.get(position).userList)
        holder.binding.cuisineTextView.text=TextUtils.join(",",dataList.get(position).restaurant.cuisineArray)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: MeetUpDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.boxLayout.setOnClickListener {
                binding.boxLayout.background = ContextCompat.getDrawable(context, R.drawable.round_corner_white_stroke_green)
                pos = adapterPosition


                if(onRestroClickListener!=null){
                    onRestroClickListener.onClick(dataList.get(adapterPosition),adapterPosition)
                }

                notifyDataSetChanged()
            }
        }
    }
}