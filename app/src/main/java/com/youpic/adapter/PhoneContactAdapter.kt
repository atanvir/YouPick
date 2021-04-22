package com.youpic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.databinding.PhoneContactLayoutBinding
import kotlinx.android.synthetic.main.phone_contact_layout.view.*
import java.util.*

public class PhoneContactAdapter(private val context: Context, var contactList:List<String>) : RecyclerView.Adapter<PhoneContactAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder= ViewHolder(LayoutInflater.from(context).inflate(R.layout.phone_contact_layout, parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.itemView.nameTextView.text=contactList.get(position)
        holder.itemView.initTextView.text=contactList.get(position)[0].toString().toUpperCase(Locale.getDefault())
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    inner class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){

    }
}