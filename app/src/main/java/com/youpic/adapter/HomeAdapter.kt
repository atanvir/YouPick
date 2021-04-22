package com.youpic.adapter

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.screens.locationDetailActivity.LocationDetailActivity
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_PICK_FAV
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.home_recycler_layout.view.*
import java.lang.StringBuilder
import java.util.*


class HomeAdapter(private val context: Context, var data: List<HomeDataDataModel.Data.Restaurant>?, var isDetail2: Boolean) : RecyclerView.Adapter<HomeAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_recycler_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.itemView.nameTextView.text= data!![position].name




        /*if(data!![position].cuisineData==null||data!![position].cuisineData!!.isEmpty()){
            Glide.with(context).load(data!![position].adminImage?.trim()).placeholder(R.drawable.miha_rekar_is_vt_bk_nh_j_2_g_unsplash).into(holder.itemView.restroImageView)
        }else{
            Glide.with(context).load(data!![position].cuisineData!!.get(0).image?.trim()).placeholder(R.drawable.miha_rekar_is_vt_bk_nh_j_2_g_unsplash).into(holder.itemView.restroImageView)
        }*/

        var list:ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?= ArrayList()

        var string=""

        if(!data!!.get(position).cuisineArray.isNullOrEmpty()){
            for(item in data!!.get(position).cuisineArray!!){
                for (name in holder.arr){
                    if(name.equals(item,true)){
                        string= string+","+name
                        break
                    }
                }
            }
        }

        Log.w("savedCus",context.getSharedPreferences(PREF_NAME,MODE_PRIVATE).getString(PREF_PICK_FAV,"")!!)
        Log.w("testSize",string.split(",").size.toString()+" "+ string.toString())

        if(!string.isNullOrEmpty()){
            if(string[0].toString()==",")
            {
                string=string.substring(1)
            }
        }

       /* if(string.split(",").size==2){
            string=string.split(",")[1]
        }*/
        if(!Utils.isDefaultImage(data!!.get(position).adminImage!!)){

            Log.w("testInside","Inside default Image")

            list!!.add( HomeDataDataModel.Data.Restaurant.ImageData("","", data!!.get(position).adminImage!!,"","","","","",""))
        }else  if(!data!!.get(position).imageData.isNullOrEmpty()){
            Log.w("testInside","Inside  Image")
            list!!.addAll(data!!.get(position).imageData!!)
        }else if(!data!!.get(position).cuisineData.isNullOrEmpty()){
            Log.w("testInside","Inside cuisineData")
            if(isDetail2){
                list!!.addAll(data!!.get(position).cuisineData!!)
            }else{
                for(item in data!!.get(position).cuisineData!!){
                    for (name in holder.arr){
                        if(name.equals(item.name,true)){
                            Log.w("testInsideImage",data!![position].name+" position: "+position+" Name: "+name+" url: "+item.image)
                            list!!.add(item)
                            break
                        }
                    }
                }
            }

        }


        Log.w("testSize","pos: "+position+"after "+ string.toString())





        holder.itemView.restroImageView.adapter = HomePagerAdapter(context, list,string,isDetail2)

        if(holder.timer!=null){
            holder.timer!!.cancel()
            holder.timer=Timer()
        }else{
            holder.timer=Timer()
        }

        if(!list.isNullOrEmpty()){
            holder.timer!!.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    (context as Activity).runOnUiThread(Runnable {
                        if (holder.itemView.restroImageView.currentItem == list.size - 1) {
                            holder.itemView.restroImageView.setCurrentItem(0, true)
                            Log.w("testSlider",data!![position].name+" Inside")
                        }else{
                            Log.w("testSlider",data!![position].name+" outside")
                            holder.itemView.restroImageView.setCurrentItem(holder.itemView.restroImageView.currentItem + 1, true)
                        }
                    })
                }
            }, 500, 5000)
        }



        /*holder.itemView.restroImageView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }*/

        /*if(isDetail2){
            holder.itemView.typeTextView.visibility=View.INVISIBLE
        }else{
            holder.itemView.typeTextView.text= data!![position].cuisineArray!![0]
        }*/


    }
    override fun getItemCount(): Int {
        return data?.size?:0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timer : Timer?=null
        var arr=context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(PREF_PICK_FAV,"")!!.split(",")
        init {
            try {
                itemView.clickView.setOnClickListener { context.startActivity(Intent(context, LocationDetailActivity::class.java)
                        .putExtra("detail2", isDetail2).putExtra("locationData", data!![adapterPosition])) }
            }catch (e:Exception){
                e.printStackTrace()

            }

            /*itemView.restroImageView.setOnTouchListener(
                    object : OnTouchListener {
                        private var moved = false
                        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                                moved = false
                            }
                            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                                moved = true
                            }
                            if (motionEvent.action == MotionEvent.ACTION_UP) {
                                if (!moved) {
                                    view.performClick()
                                }
                            }
                            return false
                        }
                    }
            )*/


        }
    }
}