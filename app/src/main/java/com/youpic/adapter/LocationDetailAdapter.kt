package com.youpic.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youpic.R
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.databinding.LocationDetailLayoutBinding
import com.youpic.databinding.LocationDetailLayoutTestBinding
import com.youpic.utils.Constants
import com.youpic.utils.Utils
import com.youpic.utils.Utils.getImageString
import com.youpic.utils.Utils.isDefaultImage
import java.text.DecimalFormat

class LocationDetailAdapter(private val context: Context, private val data: HomeDataDataModel.Data.Restaurant) : RecyclerView.Adapter<LocationDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LocationDetailLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.location_detail_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.addressTextView.text = data.address
        holder.binding.phoneTextView.text = data.phoneNumber
        holder.binding.disTextView.setText(Utils.roundTwoDecimals(data.dist!!.calculated!!).toString())

        Glide.with(context).load(context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_USER_PROFILE, "")).placeholder(R.drawable.default_profile_image).circleCrop().into(holder.binding.profileImageView)
        var list:ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?= ArrayList()


        if(!isDefaultImage(data.adminImage!!)){
            list!!.add( HomeDataDataModel.Data.Restaurant.ImageData("","", data!!.adminImage!!,"","","","","",""))
        }else if(!data.imageData.isNullOrEmpty()){
            list!!.addAll(data.imageData!!)
        }else if(!data.cuisineData.isNullOrEmpty()){
            list!!.addAll(data.cuisineData!!)
        }


        /*if(data.cuisineData==null||data.cuisineData!!.isEmpty()){
            list!!.add(0, HomeDataDataModel.Data.Restaurant.ImageData("","", data!!.adminImage!!,"","","","",""))
        }else{
            list!!.add(0, HomeDataDataModel.Data.Restaurant.ImageData("","",data!!.cuisineData!!.get(0).image!!,"","","","",""))
            list!!.add(1, HomeDataDataModel.Data.Restaurant.ImageData("","", data!!.adminImage!!,"","","","",""))
        }*/

        holder.binding.imageView24.adapter = DetailCardPagerAdapter(context, data.name!!,list)
        holder.binding.imageView24.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
        holder.binding.sliderIndicator.setupWithViewPager(holder.binding.imageView24, true)
        if(list==null||list.isEmpty()){
            holder.binding.sliderIndicator.visibility=View.INVISIBLE
        }
        if ((context as Activity).intent.getBooleanExtra("detail2", false)) {
            holder.binding.totalCountTextView.visibility = View.INVISIBLE
        }else{
            holder.binding.totalCountTextView.text="${data.totalVisited} YouPickers have eaten here."
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class ViewHolder(val binding: LocationDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}