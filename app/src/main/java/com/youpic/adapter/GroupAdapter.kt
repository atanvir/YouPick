package com.youpic.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import com.youpic.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.youpic.apiConnection.DataModel.GetFrndGroupListDataModel
import com.youpic.databinding.GroupLayoutBinding
import com.youpic.utils.OverlapDecoration

class GroupAdapter(private val context: Context,var dataList:List<GetFrndGroupListDataModel.Data>,var onGroupClick:OnGroupClick?,val showSetting:Boolean) : RecyclerView.Adapter<GroupAdapter.ViewHolder>()
{

    interface OnGroupClick{
        fun onGroupClick(data:GetFrndGroupListDataModel.Data,isAdd:Boolean,position: Int)
        fun onGroupSettingClick(data:GetFrndGroupListDataModel.Data,name:String,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: GroupLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.group_layout, parent, false)
        return ViewHolder(binding)
        // return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_invitation_layout,parent,false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recyclerView2.adapter = NameInitAdapter(context,dataList.get(position).memberData)
        holder.binding.nameTextView.text=getAllName(dataList.get(position))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: GroupLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        init
        {
            binding.recyclerView2.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.recyclerView2.addItemDecoration(OverlapDecoration(context))

            if(!showSetting)
                binding.settingImageView.visibility=View.INVISIBLE

            binding.innerLayout.setOnClickListener {

                if(onGroupClick!=null){

                    if(binding.tickImageView.visibility== View.INVISIBLE){
                        binding.tickImageView.visibility=View.VISIBLE
                        onGroupClick?.onGroupClick(dataList.get(adapterPosition),true,adapterPosition)
                    }else{
                        binding.tickImageView.visibility=View.INVISIBLE
                        onGroupClick?.onGroupClick(dataList.get(adapterPosition),false,adapterPosition)
                    }
                }
            }

            binding.settingImageView.setOnClickListener{
                if(onGroupClick!=null){
                    onGroupClick!!.onGroupSettingClick(dataList.get(adapterPosition),binding.nameTextView.text.toString(),adapterPosition)
                }
            }
        }
    }

    fun getAllName(data:GetFrndGroupListDataModel.Data):String{
        var allName=""

        for(name in data.memberData)
        {
            allName+=name.memberData.name+","
        }
        return allName.substring(0,allName.length-1)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }
}