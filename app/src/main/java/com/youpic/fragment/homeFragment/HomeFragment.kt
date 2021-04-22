package com.youpic.fragment.homeFragment

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youpic.R
import com.youpic.adapter.HomeAdapter
import com.youpic.adapter.HomeInvitationAdapter
import com.youpic.adapter.HomeScheduledAdapter
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.baseClass.BaseFragment
import com.youpic.screens.OccasionActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.screens.whatActivity.WhatActivity
import com.youpic.utils.Constants.Companion.MSG_DIALOG
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_INCOMING_COUNT
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_OUTGOING_COUNT
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.HomeData
import com.youpic.utils.Location2
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.incomingCountTextView
import kotlinx.android.synthetic.main.fragment_home.incomingTextView
import kotlinx.android.synthetic.main.fragment_home.invitationRecyclerView
import kotlinx.android.synthetic.main.fragment_home.inviteTextView
import kotlinx.android.synthetic.main.fragment_home.nearYouRecyclerView
import kotlinx.android.synthetic.main.fragment_home.noInvitesLayout
import kotlinx.android.synthetic.main.fragment_home.outgoingCountTextView
import kotlinx.android.synthetic.main.fragment_home.outgoingTextView
import kotlinx.android.synthetic.main.fragment_home.scheduledHeadTextView
import kotlinx.android.synthetic.main.fragment_home.scheduledLayout
import kotlinx.android.synthetic.main.fragment_home.scheduledRecyclerView
import kotlinx.android.synthetic.main.fragment_home.suggestionRecyclerView
import kotlinx.android.synthetic.main.fragment_home_test.*

class HomeFragment : BaseFragment(), View.OnClickListener {
    private var isIncoming = false
    var viewModel=HomeViewModel()
    var homeDataResponse: HomeDataDataModel?=null
    var broadcastReceiver: BroadcastReceiver?=null
    private var LOCATION_PERMISSION_CODE:Int=1
    private var GOOGLE_SERVICE_CODE:Int=2
    private var PERMISSION_SETTING_CODE:Int=3
    private var dataLoaded=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(checkPermissions()) Location2.startLocation(requireContext(), LOCATION_PERMISSION_CODE, GOOGLE_SERVICE_CODE, PERMISSION_SETTING_CODE)
        init()
        initCtrl()
        observer()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {


                /*if(intent!!.getStringExtra("typeNoti")==NOTI_TYPE_MEETING_REQ_ACCEPT){
                    viewModel.homeDataApi(requireActivity())
                }*/

                viewModel.homeDataApi(requireActivity())


            }
        }



      //  Utils.getDateString(SimpleDateFormat(DATE_FORMAT).parse("2021-02-19"))


    }


    override fun onResume() {
        super.onResume()
        dataLoaded=false
        viewModel.updateLocation(requireActivity(),Location2.getLat(),Location2.getLong())
        viewModel.homeDataApi(requireActivity())
        if(HomeData.homeData==null || HomeData.homeData!!.data!!.scheduleData.isNullOrEmpty()){
            scheduledRecyclerView.visibility=View.GONE
            scheduledLayout.visibility=View.VISIBLE

        }else{
            scheduledRecyclerView.visibility=View.VISIBLE
            scheduledLayout.visibility=View.GONE

            scheduledHeadTextView.text="Scheduled ("+HomeData.homeData!!.data!!.scheduleCount+")"
            scheduledRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            scheduledRecyclerView.adapter = HomeScheduledAdapter(requireActivity(), HomeData.homeData!!.data!!.scheduleData!!)

        }

        if(HomeData.thingsNearApiResponse!=null ){
            suggestionRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            suggestionRecyclerView.adapter = HomeAdapter(requireActivity(), HomeData.thingsNearApiResponse!!.data.restaurantList, false)
        }

        if(HomeData.getActivityDataApiResponse!=null ){
            nearYouRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            nearYouRecyclerView.adapter = HomeAdapter(requireActivity(), HomeData.getActivityDataApiResponse!!.data.restaurantList, true)
        }

        if(broadcastReceiver!=null)
        {

            requireActivity().registerReceiver(broadcastReceiver, IntentFilter("com.youpick"));
            Log.w("testBroadcast", "Broadcast register")
        }
    }


    private fun checkPermissions(): Boolean {
        var ret = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions( arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
            }
        }
        return ret
    }


    override fun onClick(v: View)
    {
        if (v.id == R.id.incomingTextView)
        {
            incomingTextView.background = ContextCompat.getDrawable(requireActivity(), R.drawable.circle_black)
            outgoingTextView.background = ContextCompat.getDrawable(requireActivity(), R.drawable.circle_grey)
            invitationRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            //invitationRecyclerView.adapter = HomeInvitationAdapter(activity, 3, false)



            if(homeDataResponse!=null){
                if(homeDataResponse!!.data!!.incomingData.isNotEmpty()){

                    invitationRecyclerView.visibility=View.VISIBLE
                    noInvitesLayout.visibility=View.GONE

                    invitationRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
                    invitationRecyclerView.adapter = HomeInvitationAdapter(requireActivity(), homeDataResponse!!.data!!.incomingData, false)


                }else{
                    inviteTextView.text="No Pending Invites!"
                    invitationRecyclerView.visibility=View.GONE
                    noInvitesLayout.visibility=View.VISIBLE
                }

            }else{
                viewModel.homeDataApi(requireActivity())
            }

            //binding.invitationRecyclerView.addItemDecoration(new LineDecoration(getActivity()));
            isIncoming = true
        } else if (v.id == R.id.outgoingTextView)
        {
            isIncoming = false
            outgoingTextView.background = ContextCompat.getDrawable(requireActivity(), R.drawable.circle_black)
            incomingTextView.background = ContextCompat.getDrawable(requireActivity(), R.drawable.circle_grey)
            invitationRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
           // invitationRecyclerView.adapter = HomeInvitationAdapter(activity, 1, true)

            if(homeDataResponse!=null){
                if(homeDataResponse!!.data!!.outgoingData!!.isNotEmpty()){

                    invitationRecyclerView.visibility=View.VISIBLE
                    noInvitesLayout.visibility=View.GONE

                    invitationRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
                    invitationRecyclerView.adapter = HomeInvitationAdapter(requireActivity(), homeDataResponse!!.data!!.outgoingData!!, true)


                }else{
                    inviteTextView.text="Send Some Invites!"
                    invitationRecyclerView.visibility=View.GONE
                    noInvitesLayout.visibility=View.VISIBLE
                }

            }else{
                viewModel.homeDataApi(requireActivity())
            }


            // binding.invitationRecyclerView.addItemDecoration(new LineDecoration(getActivity()));
        }else if (v.id == R.id.noInvitesLayout||v.id==R.id.scheduledLayout){
            startActivity(Intent(requireActivity(), OccasionActivity::class.java))
        }
    }

    override fun init()
    {
        incomingCountTextView.visibility=View.VISIBLE
        incomingCountTextView.text=requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_INCOMING_COUNT, "0")

        outgoingCountTextView.visibility=View.VISIBLE
        outgoingCountTextView.text=requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_OUTGOING_COUNT, "0")

        viewModel=ViewModelProvider(this).get(HomeViewModel::class.java)

    }

    override fun initCtrl() {
        swipeRefreshLayout.setOnRefreshListener {
            HomeData.getActivityDataApiResponse=null
            HomeData.thingsNearApiResponse=null
            swipeRefreshLayout.isRefreshing=false
            checkHomeData()
        }

        incomingTextView.setOnClickListener(this)
        outgoingTextView.setOnClickListener(this)
        scheduledLayout.setOnClickListener(this)
        noInvitesLayout.setOnClickListener(this)
    }

    override fun observer() {

        viewModel.homeDataApiResponse.observe(requireActivity(), {
            if (it.status == SUCCESS) {
                homeDataResponse = it
                HomeData.homeData=it
                setView()
                (requireActivity() as HomeActivity).setNotificationCount()
                if (!it.data!!.pickFavorites!!) {
                    val dialog = Utils.showMessage(requireActivity(), "Click Ok To Pick Your Favorites!", MSG_DIALOG)
                    val okTextView = dialog!!.findViewById<TextView>(R.id.okTextView)
                    okTextView.setOnClickListener {
                        dialog?.dismiss()
                        startActivity(Intent(requireActivity(), WhatActivity::class.java).putExtra("fromHome", true))
                    }

                }else {
                    checkHomeData()
                }

            } else {
                Utils.showMessage(requireActivity(), it.message, MSG_TOAST)
            }


        })

        viewModel.getActivityDataApiResponse.observe(requireActivity(), {

            if (it.status == SUCCESS) {
                dataLoaded=true

                HomeData.getActivityDataApiResponse = it
                /*nearYouRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                nearYouRecyclerView.adapter = HomeAdapter(requireActivity(),it?.data?.restaurantList,true)*/
                checkHomeData()
            } else {
                Utils.showMessage(requireActivity(), it.message, MSG_TOAST)
            }


        })

        viewModel.thingsNearApiResponse.observe(requireActivity(), {

            if (it.status == SUCCESS) {
                HomeData.thingsNearApiResponse = it
                dataLoaded=true
                /*suggestionRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                suggestionRecyclerView.adapter = HomeAdapter(requireActivity(),it?.data?.restaurantList,false)*/

                checkHomeData()
            } else {
                Utils.showMessage(requireActivity(), it.message, MSG_TOAST)
            }


        })
    }

    private fun setView() {

        val response=homeDataResponse

        incomingCountTextView.visibility=View.VISIBLE
        incomingCountTextView.text=requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_INCOMING_COUNT, "0")

        outgoingCountTextView.visibility=View.VISIBLE
        outgoingCountTextView.text=requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_OUTGOING_COUNT, "0")

        scheduledHeadTextView.text="Scheduled ("+response!!.data!!.scheduleCount+")"

        if(response.data!!.scheduleData.isNullOrEmpty()){
            scheduledRecyclerView.visibility=View.GONE
            scheduledLayout.visibility=View.VISIBLE

        }else{
            scheduledRecyclerView.visibility=View.VISIBLE
            scheduledLayout.visibility=View.GONE


            scheduledRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            scheduledRecyclerView.adapter = HomeScheduledAdapter(requireActivity(), response!!.data!!.scheduleData!!)


        }



        invitationRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
       // invitationRecyclerView.adapter = HomeInvitationAdapter(activity, 3, false)


        invitationRecyclerView.visibility=View.GONE

        incomingTextView.performClick()



    }

    private fun checkHomeData(){
        if(!dataLoaded){
            if(HomeData.thingsNearApiResponse!=null && (HomeData.thingsNearApiResponse!!.data.restaurantList.isEmpty())){
                HomeData.thingsNearApiResponse=null
            }

            if(HomeData.getActivityDataApiResponse!=null && (HomeData.getActivityDataApiResponse!!.data.restaurantList.isEmpty())){
                HomeData.getActivityDataApiResponse=null
            }

        }


        if(HomeData.thingsNearApiResponse==null ){
            Log.w("CheckHomeData", "near: null")
            viewModel.thingsNearApi(requireActivity(), Location2.getLat().toString(), Location2.getLong().toString())
            return
        } else{
            Log.w("CheckHomeData", "near: found")
            suggestionRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            suggestionRecyclerView.adapter = HomeAdapter(requireActivity(), HomeData.thingsNearApiResponse!!.data.restaurantList, false)
        }

        if(HomeData.getActivityDataApiResponse==null ){
            Log.w("CheckHomeData", "activity: null")
            viewModel.getActivityDataApi(requireActivity(), Location2.getLat().toString(), Location2.getLong().toString())
            return
        } else {
            Log.w("CheckHomeData", "activity: found")
            nearYouRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            nearYouRecyclerView.adapter = HomeAdapter(requireActivity(), HomeData.getActivityDataApiResponse!!.data.restaurantList, true)
        }

        /*if(HomeData.firstTime){
            HomeData.firstTime=false
        }else{
            Log.w("CheckHomeData", "near: silentCall")
            viewModel.thingsNearApi(requireActivity(), Location2.getLat().toString(), Location2.getLong().toString())
            viewModel.getActivityDataApi(requireActivity(), Location2.getLat().toString(), Location2.getLong().toString())
        }*/

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== GOOGLE_SERVICE_CODE || requestCode==PERMISSION_SETTING_CODE){
            if(resultCode== Activity.RESULT_OK){
                Location2.startLocation(requireActivity(), LOCATION_PERMISSION_CODE, GOOGLE_SERVICE_CODE, PERMISSION_SETTING_CODE)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            LOCATION_PERMISSION_CODE -> {
                var allAccepted = true
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        allAccepted = false
                        break
                    }
                }
                if (allAccepted) {
                    Location2.startLocation(requireActivity(),  LOCATION_PERMISSION_CODE, GOOGLE_SERVICE_CODE, PERMISSION_SETTING_CODE)
                }else{
                    Utils.showMessage(requireActivity(),"Please Allow permission for the security purpose", MSG_TOAST)
                 }

            }
        }
    }

    override fun onPause() {
        super.onPause()

        if(broadcastReceiver!=null)
        {
            requireActivity().unregisterReceiver(broadcastReceiver);
            Log.w("testBroadcast", "Broadcast Remove")
        }
    }



    override fun onDestroy() {
        super.onDestroy()


    }




}