package com.youpic.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetResaturnatListDataModel
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.databinding.CardSwipeLayoutTestBinding
import com.youpic.utils.Constants
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_location_detail_test.*
import kotlinx.android.synthetic.main.home_recycler_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class CardSwipeAdapter(private val context: Context,var dataList:List<GetResaturnatListDataModel.Data>) : RecyclerView.Adapter<CardSwipeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CardSwipeLayoutTestBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.card_swipe_layout_test, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {

        holder.binding.addressTextView.text=dataList.get(position).address
        holder.binding.numberTextView.text=dataList.get(position).phoneNumber
        holder.binding.totalTextView.text=dataList.get(position).totalVisited.toString()+" YouPickers have eaten here."


       /// var list=dataList.get(position).imageData

        var list:ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?= ArrayList()


        if(!Utils.isDefaultImage(dataList.get(position).adminImage!!)){
            list!!.add( HomeDataDataModel.Data.Restaurant.ImageData("","", dataList.get(position)!!.adminImage!!,"","","","","",""))
        }else if(!dataList.get(position).imageData.isNullOrEmpty()){
            list!!.addAll(dataList.get(position).imageData!!)
        }else if(!dataList.get(position).cuisineData.isNullOrEmpty()){
            for(item in dataList.get(position).cuisineData!!){
                for (name in holder.arr){
                    if(name.equals(item.name,true)){
                        list!!.add(item)
                        break
                    }
                }
            }

        }




        /*if(dataList.get(position).cuisineData!!.image==null||dataList.get(position).cuisineData!!.image!!.isEmpty()){
            list!!.add(0, HomeDataDataModel.Data.Restaurant.ImageData("","", dataList!!.get(position)!!.adminImage!!,"","","","",""))
        }else{
            list!!.add(0, HomeDataDataModel.Data.Restaurant.ImageData("","", dataList!!.get(position)!!.cuisineData!!.image!!,"","","","",""))
            list!!.add(1, HomeDataDataModel.Data.Restaurant.ImageData("","", dataList!!.get(position)!!.adminImage!!,"","","","",""))
        }*/

        holder.binding.viewPager.adapter = CardPagerAdapter(context,list,dataList.get(position).name!!)

        if(holder.timer!=null){
            holder.timer!!.cancel()
            holder.timer= Timer()
        }else{
            holder.timer= Timer()
        }

        if(!list.isNullOrEmpty()){
            holder.timer!!.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    (context as Activity).runOnUiThread(Runnable {
                        if (holder.binding.viewPager.currentItem == list.size - 1) {
                            holder.binding.viewPager.setCurrentItem(0, true)
                            //Log.w("testSlider",data!![position].name+" Inside")
                        }else{
                            //Log.w("testSlider",data!![position].name+" outside")
                            holder.binding.viewPager.setCurrentItem(holder.binding.viewPager.currentItem + 1, true)
                        }
                    })
                }
            }, 500, 5000)
        }



        if(list==null||list.size<=1){
            holder.binding.sliderIndicator.visibility= View.INVISIBLE
        }else{
            holder.binding.sliderIndicator.setupWithViewPager(holder.binding.viewPager, true)
        }


        holder.binding.userRecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.binding.userRecyclerView.adapter=YouPickUserAdapter(context,dataList.get(position).userData)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder( val binding: CardSwipeLayoutTestBinding) : RecyclerView.ViewHolder(binding.root){
        var timer : Timer?=null
        var arr=(context as Activity).intent.getStringArrayListExtra("cuisineList")!!
        init {

            binding.numberTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + binding.numberTextView.text.toString()))
                context.startActivity(intent)
            }
            /*binding.viewPager.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }*/

        }

    }
}