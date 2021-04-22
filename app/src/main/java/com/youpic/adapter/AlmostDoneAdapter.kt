package com.youpic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.apiConnection.Body.MeetingRequestBody
import com.youpic.databinding.NayoBinding

class AlmostDoneAdapter(private val context: Context ,var dataList:List<MeetingRequestBody.UsersData>) : RecyclerView.Adapter<AlmostDoneAdapter.ViewHolder>() {
    var selectedPos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: NayoBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.nayo, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView11.setText(dataList[position].name)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: NayoBinding) : RecyclerView.ViewHolder(binding.root)
}