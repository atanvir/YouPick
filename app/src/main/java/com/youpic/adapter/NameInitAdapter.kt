package com.youpic.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetFrndGroupListDataModel
import com.youpic.databinding.NameInitialLayoutBinding

class NameInitAdapter(private val context: Context,val dataList:List<GetFrndGroupListDataModel.Data.MemberData>) : RecyclerView.Adapter<NameInitAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: NameInitialLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.name_initial_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.nameInitTextView.text=dataList.get(position).memberData.name[0].toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder( val binding: NameInitialLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}