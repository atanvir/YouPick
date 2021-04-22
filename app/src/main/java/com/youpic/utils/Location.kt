package com.youpic.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

/*fun getLat():Double{
        return 27.2046
    }

    fun getLong():Double{
        return 77.4977
    }*/

class Location(private val mContext: Context) : Service(), LocationListener {
    var checkGPS = false
    var checkNetwork = false
    var canGetLocation = false
    var loc: Location? = null
    var latitude = 0.0
    var longitude = 0.0
    protected var locationManager: LocationManager? = null//Toast.makeText(mContext,"GPS",Toast.LENGTH_SHORT).show();//Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();
    // if GPS Enabled get lat/long using GPS Services
// First get location from Network Provider
    // getting GPS status

    // getting network status
    val location: Location?
        get() {
            try {
                locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

                // getting GPS status
                checkGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

                // getting network status
                checkNetwork = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!checkGPS && !checkNetwork) {
                    Toast.makeText(mContext, "Please enable your device gps!", Toast.LENGTH_SHORT).show()
                } else {
                    canGetLocation = true
                    // First get location from Network Provider
                    if (checkNetwork) {
                        //Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();
                        try {
                            locationManager!!.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                            Log.d("Network", "Network")
                            if (locationManager != null) {
                                loc = locationManager!!
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            }
                            if (loc != null) {
                                latitude = loc!!.latitude
                                longitude = loc!!.longitude
                            }
                        } catch (e: SecurityException) {
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {
                    //Toast.makeText(mContext,"GPS",Toast.LENGTH_SHORT).show();
                    if (loc == null) {
                        try {
                            locationManager!!.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                            Log.d("GPS Enabled", "GPS Enabled")
                            if (locationManager != null) {
                                loc = locationManager!!
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                if (loc != null) {
                                    latitude = loc!!.latitude
                                    longitude = loc!!.longitude
                                }
                            }
                        } catch (e: SecurityException) {
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return loc
        }

    fun getLat():Double{
        if (loc != null) {
            latitude = loc!!.latitude
        }
        return latitude
        //return 32.494839
    }

    fun getLong():Double{
        if (loc != null) {
            longitude = loc!!.longitude
        }
        return longitude
        //return -97.366089
    }




    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    /*private fun setUpLocationSettingsTaskStuff() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { loadCurrentLoc() }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    Log.e("requested", "yes")
                    e.startResolutionForResult(requireActivity(), PERMISSION_DIALOG_REQ)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }*/

    fun showSettingsAlert(requestCode: Int, activity: Activity) {
        val alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Location Disable")
        alertDialog.setMessage("Please enable your Location")
        alertDialog.setPositiveButton("Ok") { dialog: DialogInterface?, which: Int ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivityForResult(intent, requestCode)
        }
        alertDialog.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
            //showSettingsAlert();
            dialog.cancel()
        }
        alertDialog.show()
    }

    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
    override fun onProviderEnabled(s: String) {}
    override fun onProviderDisabled(s: String) {}

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    }

    init {
        location
    }
}