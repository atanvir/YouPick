package com.youpic.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetCuisineListDataModel
import com.youpic.screens.whatActivity.WhatActivity
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.what_layout.view.*

class WhatAdapter(private val context: Context,private val itemClickListener: ItemClickListener?,private val dataList:List<GetCuisineListDataModel.Data>?,val isSignUp:Boolean) : RecyclerView.Adapter<WhatAdapter.ViewHolder>() {

    var count=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.what_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.titleTextView.text=dataList?.get(position)?.name
        if(dataList?.get(position)?.isSelected == true){
            holder.itemView.tickImageView.visibility=View.VISIBLE
        }else{
            holder.itemView.tickImageView.visibility=View.INVISIBLE
        }


    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener(View.OnClickListener {


                    if(itemView.tickImageView.visibility==View.VISIBLE){

                        itemView.tickImageView.visibility=View.INVISIBLE
                        dataList?.get(adapterPosition)?.isSelected=false

                       // if(isSignUp)
                            count--

                    }else{
                        if((count+1)<=3){
                            itemView.tickImageView.visibility=View.VISIBLE
                            dataList?.get(adapterPosition)?.isSelected=true

                            //if(isSignUp)
                                count++
                        }else{
                            Utils.showMessage(context,"Oops! You can only pick 3!",MSG_TOAST)
                        }




                }

                itemClickListener?.itemClick(itemView.tickImageView.visibility==View.VISIBLE,adapterPosition,
                        itemView.titleTextView.text.toString())

            })
        }

    }

    fun setTotalCount( count:Int){
        Log.w("test","prv count: "+this.count+" count: "+count)
        this.count=count
    }


    interface ItemClickListener{
        fun itemClick(isCheck:Boolean,pos:Int,selectedItem:String)
    }
}