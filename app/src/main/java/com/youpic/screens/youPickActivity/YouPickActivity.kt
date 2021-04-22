package com.youpic.screens.youPickActivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.youpic.R
import com.youpic.adapter.CardSwipeAdapter
import com.youpic.apiConnection.Body.GetResaturnatListBody
import com.youpic.apiConnection.Body.MeetingRequestProcessedBody
import com.youpic.apiConnection.Body.RestaurantChoice
import com.youpic.apiConnection.DataModel.GetResaturnatListDataModel
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.GoodWorkActivity
import com.youpic.screens.meetUpDetailActivity.MeetUpDetailActivity
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.DISTANCE
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_ID
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Location
import com.youpic.utils.Location2
import com.youpic.utils.Utils
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.android.synthetic.main.activity_you_pick.*
import kotlinx.android.synthetic.main.common_titlebar.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class YouPickActivity : BaseActivity(), OnMapReadyCallback, CardStackListener
{

    lateinit var viewModel:YouPickViewModel
    lateinit var dataList:ArrayList<GetResaturnatListDataModel.Data>
    private val manager by lazy { CardStackLayoutManager(this, this) }
    var position=0
    var pageNo=1
    var restaurantChoice= ArrayList<RestaurantChoice>()
    lateinit var googleMap: GoogleMap
    private var LOCATION_PERMISSION_CODE:Int=1
    private var GOOGLE_SERVICE_CODE:Int=2
    private var PERMISSION_SETTING_CODE:Int=3

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_you_pick_test)

        init()
        initControl()
        myObserver()

        Location2.startLocation(this,LOCATION_PERMISSION_CODE,GOOGLE_SERVICE_CODE,PERMISSION_SETTING_CODE)


        setLayout()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.frameLayout2) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        moreButton.isEnabled=false

        Utils.printAllExtras(intent.extras!!)
    }

    private fun setLayout() {
        titleTextView.text="You Pick"
        if (intent.getBooleanExtra("fromInvites", false) ||
                intent.getBooleanExtra("fromDone", false)) {
            doneButton.visibility = View.GONE
            goodButton.visibility = View.VISIBLE
            moreButton.visibility = View.VISIBLE
        } else {
            doneButton.visibility = View.VISIBLE
            goodButton.visibility = View.GONE
            moreButton.visibility = View.GONE
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap=googleMap
        googleMap.addMarker(MarkerOptions().position(LatLng(dataList.get(0)!!.latitude!!.toDouble(),dataList.get(0)!!.longitude!!.toDouble())))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(dataList.get(0)!!.latitude!!.toDouble(),dataList.get(0)!!.longitude!!.toDouble()), 16.0f))

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap.isMyLocationEnabled=true

    }


    override fun init() {
        viewModel=ViewModelProvider(this).get(YouPickViewModel::class.java)

        var data:ArrayList<GetResaturnatListDataModel.Data>?=null
        try {
            data = intent.getParcelableArrayListExtra("restroList")
        }catch (e:Exception){
            e.printStackTrace()
        }
        dataList=data!!



        swipeStackView.layoutManager = manager
        swipeStackView.adapter = CardSwipeAdapter(this,dataList)
    }

    override fun initControl() {
        findViewById<View>(R.id.backTextView).setOnClickListener { finish() }

        doneButton.setOnClickListener {
            if(restaurantChoice.isNullOrEmpty()){

                finish()
               // Utils.showMessage(this,"Please select any restaurant!", MSG_TOAST)

            }else{
                val body=MeetingRequestProcessedBody(intent.getStringArrayListExtra("cuisineList")!!,
                        intent.getStringExtra("meetingId")!!,
                        restaurantChoice)
                viewModel.meetingRequestProcessedApi(this,body)
            }
        }

        goodButton.setOnClickListener {
            if(restaurantChoice.isNullOrEmpty()){

                Utils.showMessage(this,"Please select any restaurant!", MSG_TOAST)

            }else{
                val body=MeetingRequestProcessedBody(intent.getStringArrayListExtra("cuisineList")!!,
                        intent.getStringExtra("meetingId")!!,
                        restaurantChoice)
                viewModel.meetingRequestProcessedApi(this,body)
            }
        }

        moreButton.setOnClickListener {


            val requestUserData= ArrayList<String>()
            requestUserData.add(getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_USER_ID,"")!!)

            val body= GetResaturnatListBody((pageNo+1).toString(),
                    intent.getStringExtra("meetingId")!!,
                    intent.getStringArrayListExtra("cuisineList")!!.size.toString()
                    ,intent.getStringArrayListExtra("cuisineList")!!
                    /*,intent.getStringExtra("distance")!!
                    ,intent.getStringExtra("latitude")!!
                    ,intent.getStringExtra("longitude")!!*/
                    ,requestUserData)

            viewModel.getResaturnatListApi(this,body,true)


        }

    }

    override fun myObserver() {
        viewModel.getResaturnatListResponse.observe(this,{


            if(it.status==SUCCESS){
              //  Log.w("testPosition","Pos: "+position+" Condition: "+(it.data.isNullOrEmpty() || position==2))
                if(it.data?.cuisine1Restuarants?.isEmpty()!! && it?.data?.cuisine2Restuarants?.isEmpty()!!  &&  it?.data?.cuisine3Restuarants?.isEmpty()!!){
                    //Log.w("testPagination","list is null or empty")
                  //  Utils.showMessage(this,"No More Restaurant found. Try selecting different cuisine!", MSG_TOAST)

                    moreButton.visibility=View.GONE
                    goodButton.visibility=View.GONE
                    doneButton.visibility=View.VISIBLE

                    smileyImageView.visibility=View.VISIBLE
                    noDateFoundTextView.visibility=View.VISIBLE
                    swipeStackView.visibility=View.GONE

                    if(restaurantChoice.isNullOrEmpty()){
                        noDateFoundTextView.text="You've reached the end.\nClick below button to select different cuisines"
                    }else{
                        noDateFoundTextView.text="You've reached the end.\nClick below button to proceed."
                    }

                }else{

                    smileyImageView.visibility=View.GONE
                    noDateFoundTextView.visibility=View.GONE
                    swipeStackView.visibility=View.VISIBLE

                    moreButton.isEnabled=false
                    moreButton.background=ContextCompat.getDrawable(this,R.drawable.round_corner_grey)
                    manager.setCanScrollHorizontal(true)
                    manager.setCanScrollVertical(false)
                    pageNo++
                    var startPos=dataList.size
                    getAllData(it)

                    Log.w("testPagination",dataList.size.toString())
                    swipeStackView.adapter!!.notifyItemRangeInserted(startPos,dataList?.size?:0)
                }

            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }


        })

        viewModel.meetingRequestProcessedResponse.observe(this,{
            if(it.status==SUCCESS){
                if (intent.getBooleanExtra("fromDone", false)) {
                    startActivity(Intent(this@YouPickActivity, MeetUpDetailActivity::class.java).putExtras(intent.extras!!))
                }else{
                    startActivity(Intent(this@YouPickActivity, GoodWorkActivity::class.java).putExtra("fromDone", true))
                }

            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }

        })
    }

    private fun getAllData(it: GetResaturnatListDataModel?): ArrayList<GetResaturnatListDataModel.Data>? {
        if(!it?.data?.cuisine1Restuarants?.isEmpty()!!) dataList.addAll(it?.data?.cuisine1Restuarants!!)
        if(!it?.data?.cuisine2Restuarants?.isEmpty()!!) dataList.addAll(it?.data?.cuisine2Restuarants!!)
        if(!it?.data?.cuisine3Restuarants?.isEmpty()!!) dataList.addAll(it?.data?.cuisine3Restuarants!!)
        Collections.shuffle(dataList)
        return dataList
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
     //   Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        /*Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")

        Log.w("CardStackView",direction.name)*/

        if((dataList.size-1)<=position){
            manager.setCanScrollHorizontal(false)
            manager.setCanScrollVertical(false)
            smileyImageView.visibility=View.VISIBLE
            noDateFoundTextView.visibility=View.VISIBLE
            swipeStackView.visibility=View.GONE
            moreButton.background=ContextCompat.getDrawable(this,R.drawable.round_corner_green2)
            moreButton.isEnabled=true

            //Utils.showMessage(this,"You've reached the end. Click see more button to get more restaurant", MSG_TOAST)
        }

        if(direction.name=="Right"){
            var position=manager.topPosition-1
            restaurantChoice.add(RestaurantChoice(dataList.get(position).address!!,
                    dataList.get(position).id!!,
                    dataList.get(position).latitude!!,
                    dataList.get(position).longitude!!,
                    dataList.get(position).name!!,
                    getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_ID,"")!!))
        }

    }

    override fun onCardRewound() {
     //   Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
      //  Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        Log.d("CardStackView", "onCardAppeared: ")
        this.position=position

        googleMap.clear()

        googleMap.addMarker(MarkerOptions().position(LatLng(dataList.get(position)!!.latitude!!.toDouble(),dataList.get(position)!!.longitude!!.toDouble())))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(dataList.get(position)!!.latitude!!.toDouble(),dataList.get(position)!!.longitude!!.toDouble()), 16.0f))



    }

    override fun onCardDisappeared(view: View, position: Int) {
        Log.d("CardStackView", "onCardDisappeared: ")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==GOOGLE_SERVICE_CODE || requestCode==PERMISSION_SETTING_CODE){
            if(resultCode== Activity.RESULT_OK){
                Location2.startLocation(this,LOCATION_PERMISSION_CODE,GOOGLE_SERVICE_CODE,PERMISSION_SETTING_CODE)
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
                    Location2.startLocation(this,LOCATION_PERMISSION_CODE,GOOGLE_SERVICE_CODE,PERMISSION_SETTING_CODE)
                }else {
                    Utils.showMessage(this,"Please Allow permission for the security purpose", Constants.MSG_TOAST)
                }

            }
        }
    }

}