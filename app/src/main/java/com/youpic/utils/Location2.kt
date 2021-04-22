package com.youpic.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.*
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener


@SuppressLint("StaticFieldLeak")
object Location2: GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnSuccessListener<Location> {
    lateinit var activity:Activity
     var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var isLocServiceStarted = false
    private var locationCallback: LocationCallback? = null
    var latitude: Double? = 0.0
    var longitude: Double? = 0.0
    private var mLastLocation: Location? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    lateinit var context:Context
    private var LOCATION_PERMISSION_CODE:Int=1
    private var GOOGLE_SERVICE_CODE:Int=2
    private var PERMISSION_SETTING_CODE:Int=3

    //starting point
    fun startLocation(context: Context,LOCATION_PERMISSION_CODE:Int,GOOGLE_SERVICE_CODE:Int,PERMISSION_SETTING_CODE:Int){
        this.context=context
        this.LOCATION_PERMISSION_CODE=LOCATION_PERMISSION_CODE
        this.GOOGLE_SERVICE_CODE=GOOGLE_SERVICE_CODE
        this.PERMISSION_SETTING_CODE=PERMISSION_SETTING_CODE

        this.activity=context as Activity

        if(checkPermissions()) { startLocationFunctioning()}

    }



    private fun checkPermissions(): Boolean {
        var ret = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(activity,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
            }
        }
        return ret
    }

    fun startLocationFunctioning() {
        if (!isOnline(context)) {
            Toast.makeText(context, "Internet not available.", Toast.LENGTH_SHORT).show()
        } else {
            if (isGPlayServicesOK(activity)) {
                buildGoogleApiClient()
            }
        }
    }

    private fun buildGoogleApiClient() {
        if (googleApiClient!=null && googleApiClient!!.isConnected) {
            googleApiClient!!.disconnect()
        }
        googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
        googleApiClient!!.connect()
        createLocationRequest()
    }


    fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest!!.setInterval(2000)
        locationRequest!!.setFastestInterval(10 * 1000.toLong())
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }


    @SuppressLint("MissingPermission")
    fun loadCurrentLoc() {
        try {
            mFusedLocationClient!!.lastLocation.addOnSuccessListener(this)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Log.e("LocationTest", "" + locationResult)
                    for (location in locationResult.locations) {
                        if (location != null) {
                            if (!isLocServiceStarted) {
                                locationCallBack(location)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun locationCallBack(location: Location?) {
        mLastLocation = location
        latitude = mLastLocation!!.latitude
        longitude = mLastLocation!!.longitude
        isLocServiceStarted = true

    }

    fun getLat():Double{
       if(latitude==null){
           return 0.0
       }
        return latitude!!
       // return 32.494839
       // return 0.0
    }

    fun getLong():Double{
        if(longitude==null){
            return 0.0
        }
        return longitude!!
     //   return -97.366089
       // return 0.0
    }

    fun setUpLocationSettingsTaskStuff() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { loadCurrentLoc() }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    Log.e("requested", "yes")
                    e.startResolutionForResult(activity, PERMISSION_SETTING_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
        setUpLocationSettingsTaskStuff()
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onSuccess(location: Location?) {
        if (location != null)  {locationCallBack(location) }
        else { isLocServiceStarted=false }
    }

    /*override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            PERMISSION -> {
                var permissionDenied = false
                for (i in 0..(grantResults.size - 1)) {
                    if (grantResults.get(i) == PackageManager.PERMISSION_DENIED) {
                        permissionDenied = true
                        break
                    }

                }

                if (permissionDenied) CommonUtils.showSnackBar(requireActivity(), "Please Allow permission for the security purpose")
                else startLocationFunctioning()
            }
        }
    }*/


    fun isOnline(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting || wifi_info!!.isConnectedOrConnecting) true
                else false
            } else {
                if (wifi_info!!.isConnectedOrConnecting) true
                else false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("" + e)
            false
        }
    }

    fun isGPlayServicesOK(activity: Activity?): Boolean {
        val isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)) {
            val dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, isAvailable, GOOGLE_SERVICE_CODE)
            dialog.show()
        } else {
            Toast.makeText(activity, "Can't connect to Google Play Services", Toast.LENGTH_SHORT).show()
        }
        return false
    }

}