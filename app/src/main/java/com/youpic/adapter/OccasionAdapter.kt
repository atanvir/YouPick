package com.youpic.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.youpic.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.youpic.databinding.OccasionLayoutBinding
import com.youpic.screens.nearActivity.NearActivity
import com.youpic.screens.WhenActivity

class OccasionAdapter(private val context: Context) : RecyclerView.Adapter<OccasionAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val binding: OccasionLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.occasion_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> holder.binding.titleTextView.text = "Breakfast"
            1 -> holder.binding.titleTextView.text = "Brunch"
            2 -> holder.binding.titleTextView.text = "Lunch"
            3 -> holder.binding.titleTextView.text = "Dinner"
            4 -> holder.binding.titleTextView.text = "Business Meeting"
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(val binding: OccasionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemLayout.setOnClickListener {
                val mintent:Intent
                /*if ((context as Activity).intent.getBooleanExtra("fromHome", false))
                {
                    mintent=Intent(context, NearActivity::class.java).putExtra("fromHome", true)
                } else {
                    mintent=Intent(context, WhenActivity::class.java)
                }*/

                mintent=Intent(context, WhenActivity::class.java)

                mintent.putExtra("occasion",binding.titleTextView.text)
                if((context as Activity).intent.extras!=null)
                    mintent.putExtras((context as Activity).intent.extras!!)

                context.startActivity(mintent)
            }
        }
    }
}