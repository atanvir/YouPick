package com.youpic.screens.locationDetailActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.youpic.R
import com.youpic.adapter.DetailCardPagerAdapter
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.apiConnection.DataModel.HomeDataDataModel.Data.Restaurant
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.OccasionActivity
import com.youpic.utils.Constants
import com.youpic.utils.Location2
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_location_detail.*
import kotlinx.android.synthetic.main.activity_location_detail.button
import kotlinx.android.synthetic.main.activity_location_detail.doneButton
import kotlinx.android.synthetic.main.activity_location_detail_test.*

class LocationDetailActivity : BaseActivity(), OnMapReadyCallback
{
    var data: Restaurant? = null
    private var LOCATION_PERMISSION_CODE:Int=1
    private var GOOGLE_SERVICE_CODE:Int=2
    private var PERMISSION_SETTING_CODE:Int=3


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail_test)

        data = intent.getParcelableExtra("locationData")

        Location2.startLocation(this, LOCATION_PERMISSION_CODE, GOOGLE_SERVICE_CODE, PERMISSION_SETTING_CODE)
        init()
        initControl()
        myObserver()

        /*val cardStackLayoutManager = CardStackLayoutManager(this)
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.None)


        swipeStackView.layoutManager = cardStackLayoutManager
        swipeStackView.adapter = LocationDetailAdapter(this, data!!)*/


        Handler(Looper.getMainLooper()).post {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.frameLayout2) as SupportMapFragment?
            mapFragment!!.getMapAsync(this@LocationDetailActivity)
        }
        if (intent.getBooleanExtra("detail2", false)) {
            doneButton.visibility = View.INVISIBLE
        }
        setLayout(this, data!!)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.addMarker(MarkerOptions().position(LatLng(data!!.latitude!!.toDouble(), data!!.longitude!!.toDouble())))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(data!!.latitude!!.toDouble(), data!!.longitude!!.toDouble()), 16.0f))

    }

    private fun setLayout(context: Context, data: HomeDataDataModel.Data.Restaurant) {

        addressTextView.text = data.address
        phoneTextView.text = data.phoneNumber
        disTextView.setText(Utils.roundTwoDecimals(data.dist!!.calculated!!).toString())

        Glide.with(context).load(context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_USER_PROFILE, "")).placeholder(R.drawable.default_profile_image).circleCrop().into(profileImageView)
        var list:ArrayList<HomeDataDataModel.Data.Restaurant.ImageData>?= ArrayList()

        var arr=context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_PICK_FAV, "")!!.split(",")
        if(!Utils.isDefaultImage(data.adminImage!!)){
            list!!.add(HomeDataDataModel.Data.Restaurant.ImageData("", "", data!!.adminImage!!, "", "", "", "", "", ""))
        }
        if(!data.imageData.isNullOrEmpty()){
            list!!.addAll(data.imageData!!)
        }
        if(!data.cuisineData.isNullOrEmpty())
        {
            if(intent.getBooleanExtra("detail2", false)){
                list!!.addAll(data.cuisineData!!)
            }else{
                for(item in data.cuisineData!!){
                    for (name in arr){
                        if(name.equals(item.name, true)){
                            list!!.add(item)
                            break
                        }
                    }
                }

                /*for(item in data.cuisineData!!){
                    for (name in arr){
                        if(name.equals(item.name,true)){
                            list!!.add(item)
                            break
                        }
                    }
                }*/
            }

        }


        /*if(data.cuisineData==null||data.cuisineData!!.isEmpty()){
            list!!.add(0, HomeDataDataModel.Data.Restaurant.ImageData("","", data!!.adminImage!!,"","","","",""))
        }else{
            list!!.add(0, HomeDataDataModel.Data.Restaurant.ImageData("","",data!!.cuisineData!!.get(0).image!!,"","","","",""))
            list!!.add(1, HomeDataDataModel.Data.Restaurant.ImageData("","", data!!.adminImage!!,"","","","",""))
        }*/

        imageView24.adapter = DetailCardPagerAdapter(context, data.name!!, list)
        imageView24.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
        sliderIndicator.setupWithViewPager(imageView24, true)
        if(list==null||list.size<=1){
            sliderIndicator.visibility=View.INVISIBLE
        }
        if ((context as Activity).intent.getBooleanExtra("detail2", false)) {
            totalCountTextView.visibility = View.INVISIBLE
        }else{
            totalCountTextView.text="${data.totalVisited} YouPickers have eaten here."
        }


    }


    override fun init() {
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }
        (findViewById<View>(R.id.titleTextView) as TextView).text = "Location Details"
    }

    override fun initControl() {
        phoneTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneTextView.text.toString()))
            startActivity(intent)
        }
        button.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + Location2.getLat() + "," + Location2.getLong() + "&daddr=" + data!!.latitude + "," + data!!.longitude))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
        doneButton.setOnClickListener {

            startActivity(Intent(this@LocationDetailActivity, OccasionActivity::class.java)
                    .putExtra("locationDetail", true)
                    .putExtras(intent.extras!!))
        }
    }

    override fun myObserver() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==GOOGLE_SERVICE_CODE || requestCode==PERMISSION_SETTING_CODE){
            if(resultCode== Activity.RESULT_OK){
                Location2.startLocation(this, LOCATION_PERMISSION_CODE, GOOGLE_SERVICE_CODE, PERMISSION_SETTING_CODE)
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
                    Location2.startLocation(this, LOCATION_PERMISSION_CODE, GOOGLE_SERVICE_CODE, PERMISSION_SETTING_CODE)
                } else {
                    Utils.showMessage(this, "Please Allow permission for the security purpose", Constants.MSG_TOAST)
                }

            }
        }
    }

}