package com.youpic.adapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetFriendListDataModel
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_ID
import com.youpic.utils.Location
import kotlinx.android.synthetic.main.friends_layout.view.*

class FriendsAdapter(private val context: Context?, private val dataList: List<GetFriendListDataModel.Data>?,var onFriendClick: OnFriendClick,var showSetting:Boolean) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>()
{

    abstract interface OnFriendClick{
        abstract fun onSettingClick(friendId:String,position: Int)
        abstract fun onItemClick(friendId:String,name:String,lat:String,_long:String,isRemove:Boolean,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friends_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        if(context?.getSharedPreferences(PREF_NAME,MODE_PRIVATE)?.getString(PREF_USER_ID,"").equals(dataList?.get(position)?.acceptByUserData?.id,true)){
            holder.itemView.nameTextView.text= dataList?.get(position)?.acceptToData?.name
           // holder.itemView.initTextView.text= dataList?.get(position)?.acceptToData?.name?.get(0).toString()
            Glide.with(context!!).load(dataList?.get(position)?.acceptToData?.profilePic).placeholder(R.drawable.default_profile_image).circleCrop().into(holder.itemView.initTextView)
        }else{
            holder.itemView.nameTextView.text= dataList?.get(position)?.acceptByUserData?.name
           // holder.itemView.initTextView.text= dataList?.get(position)?.acceptByUserData?.name?.get(0).toString()
            Glide.with(context!!).load(dataList?.get(position)?.acceptByUserData?.profilePic).placeholder(R.drawable.default_profile_image).circleCrop().into(holder.itemView.initTextView)
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

            if(!showSetting){
                itemView.settingImageView.visibility=View.GONE
            }

            itemView.settingImageView.setOnClickListener{

                onFriendClick.onSettingClick(dataList!!.get(adapterPosition).id,adapterPosition)

            }

            itemView.setOnClickListener{
                val isRemove: Boolean
                if(itemView.tickImageView.visibility==View.VISIBLE)
                {
                    itemView.tickImageView.visibility=View.INVISIBLE
                    isRemove=true
                }else
                {
                    itemView.tickImageView.visibility=View.VISIBLE
                    isRemove=false
                }

                var id=""
                var name=""
                var lat=""
                var _long=""
                if(context!!.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_ID,"")!!.equals(dataList!!.get(adapterPosition).acceptByUserData.id)){
                    id=dataList!!.get(adapterPosition).acceptToData.id
                    name=dataList!!.get(adapterPosition).acceptToData.name
                    lat=dataList!!.get(adapterPosition).acceptToData.latitude
                    _long=dataList!!.get(adapterPosition).acceptToData.longitude
                }else{
                    id=dataList!!.get(adapterPosition).acceptByUserData.id
                    name=dataList!!.get(adapterPosition).acceptByUserData.name
                    lat=dataList!!.get(adapterPosition).acceptByUserData.latitude
                    _long=dataList!!.get(adapterPosition).acceptByUserData.longitude
                }
                onFriendClick.onItemClick(id,name, lat,_long,isRemove,adapterPosition)

            }
        }

    }
}