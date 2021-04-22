package com.youpic.screens.nearActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.youpic.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.youpic.adapter.NearAdapter
import android.widget.TextView
import com.youpic.apiConnection.Body.MeetingRequestBody
import com.youpic.databinding.ActivityNearBinding
import com.youpic.utils.FriendList
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_near.*

class NearActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_near)
        Utils.printAllExtras(intent.extras!!)


        nearRecyclerView.layoutManager = LinearLayoutManager(this)
        var tempList=FriendList.getFriendListWithMe(this).toMutableList()
        tempList.add(MeetingRequestBody.UsersData("","","",""))
        nearRecyclerView.adapter = NearAdapter(this,tempList)
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }
        (findViewById<View>(R.id.titleTextView) as TextView).text = "Near Who?"
    }
}