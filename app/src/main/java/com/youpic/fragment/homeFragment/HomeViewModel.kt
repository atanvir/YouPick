package com.youpic.fragment.homeFragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.GetActivityDataModel
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.apiConnection.DataModel.ThingsNearDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel:ApiBaseViewModel() {

    var homeDataApiResponse= MutableLiveData<HomeDataDataModel>()
    var thingsNearApiResponse= MutableLiveData<ThingsNearDataModel>()
    var getActivityDataApiResponse= MutableLiveData<GetActivityDataModel>()
    var error= MutableLiveData<Throwable>()



    fun homeDataApi(context: Context){
        CustomProgressBar.getCustomDialog(context)
        apiInterface.homeDataApi(Utils.getUserToken(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()
                            saveHomeData(context,it)
                            homeDataApiResponse.value=it
                        },
                        {
                            CustomProgressBar.dismissDialog()
                            error.value=it
                            ErrorHandler.handlerError(context,it)
                        })
    }


    fun updateLocation(context: Context,latitude : Double,longitude: Double){
        CustomProgressBar.getCustomDialog(context)
        apiInterface.updateLocation(Utils.getUserToken(context),latitude=latitude,longitude=longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                }
    }

    fun thingsNearApi(context: Context,latitude:String,longitude:String)
    {
        if(HomeData.thingsNearApiResponse==null||HomeData.thingsNearApiResponse!!.data.restaurantList.isEmpty()){
            CustomProgressBar.getCustomDialog(context)
        }


        apiInterface.thingsNearApi(Utils.getUserToken(context),latitude,longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()

                            if(HomeData.thingsNearApiResponse==null||HomeData.thingsNearApiResponse!!.data.restaurantList.isEmpty()){
                                thingsNearApiResponse.value=it
                            }else{
                                HomeData.thingsNearApiResponse=it
                            }

                        },
                        {
                            CustomProgressBar.dismissDialog()
                            error.value=it
                            ErrorHandler.handlerError(context,it)
                        })
    }

    fun getActivityDataApi(context: Context,latitude:String,longitude:String){


        if(HomeData.getActivityDataApiResponse==null||HomeData.getActivityDataApiResponse!!.data.restaurantList.isEmpty()){
            CustomProgressBar.getCustomDialog(context)
        }
        apiInterface.getActivityDataApi(Utils.getUserToken(context),latitude,longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()
                            if(HomeData.getActivityDataApiResponse==null||HomeData.getActivityDataApiResponse!!.data.restaurantList.isEmpty()){
                                getActivityDataApiResponse.value=it
                            }else{
                                HomeData.getActivityDataApiResponse=it
                            }

                        },
                        {
                            CustomProgressBar.dismissDialog()
                            error.value=it
                            ErrorHandler.handlerError(context,it)
                        })
    }

    private fun saveHomeData(context: Context, response: HomeDataDataModel){

        if(response.status== Constants.SUCCESS){

            val pref=context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).edit()
            pref.putString(Constants.PREF_USER_NAME,response.data?.userName)
            pref.putString(Constants.PREF_INCOMING_COUNT,response.data?.incomingCount)
            pref.putString(Constants.PREF_NOTI_COUNT,response.data?.notiCount)
            pref.putString(Constants.PREF_OUTGOING_COUNT,response.data?.outgoingCount)
            pref.putString(Constants.PREF_SCHEDULE_COUNT,response.data?.scheduleCount)
            pref.putString(Constants.PREF_USER_ID,response.data?.userId)
            pref.putString(Constants.PREF_USER_PROFILE,response.data?.userProfile)
            pref.putString(Constants.PREF_POINT,response.data?.points)
            pref.putString(Constants.PREF_BADGE_IMG,response.data?.batch?.image)
            pref.putString(Constants.PREF_BADGE_NAME,response.data?.batch?.name)
            pref.putString(Constants.PREF_COUNTRY_NAME_CODE,response.data?.country)
            pref.apply()

        }else{
            Utils.showMessage(context,response.message, Constants.MSG_TOAST)
        }
    }



}