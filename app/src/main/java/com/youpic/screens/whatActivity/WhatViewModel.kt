package com.youpic.screens.whatActivity

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.Body.AddFavoritePickBody
import com.youpic.apiConnection.Body.UserSignupBody
import com.youpic.apiConnection.DataModel.AddFavoritePickDataModel
import com.youpic.apiConnection.DataModel.GetCuisineListDataModel
import com.youpic.apiConnection.DataModel.UserLoginDataModel
import com.youpic.apiConnection.DataModel.UserSignupDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WhatViewModel: ApiBaseViewModel() {

    var signUpResponse= MutableLiveData<UserSignupDataModel>()
    var getCuisineListResponse= MutableLiveData<GetCuisineListDataModel>()
    var addFavoritePickResponse= MutableLiveData<AddFavoritePickDataModel>()


    fun userSignUpApi(context:Context,body:UserSignupBody) {

        CustomProgressBar.getCustomDialog(context)
        apiInterface.userSignUpApi(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            signUpResponse.value=it
                            saveHomeData(context,it)
                            CustomProgressBar.dismissDialog()
                        },
                        {
                            ErrorHandler.handlerError(context,it)
                            CustomProgressBar.dismissDialog()
                        })
    }


    fun addFavoritePickApi(context:Context,body: AddFavoritePickBody) {
        context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE).edit().putString(Constants.PREF_PICK_FAV,TextUtils.join(",",body.pickFavorites)).apply()

        CustomProgressBar.getCustomDialog(context)
        apiInterface.addFavoritePickApi(Utils.getUserToken(context),body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            addFavoritePickResponse.value=it
                            CustomProgressBar.dismissDialog()
                        },
                        {
                            ErrorHandler.handlerError(context,it)
                            CustomProgressBar.dismissDialog()
                        })
    }

    fun getCuisineListApi(context:Context) {

        CustomProgressBar.getCustomDialog(context)
        apiInterface.getCuisineListApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getCuisineListResponse.value=it
                            CustomProgressBar.dismissDialog()
                        },
                        {
                            ErrorHandler.handlerError(context,it)
                            CustomProgressBar.dismissDialog()
                        })
    }

    private fun saveHomeData(context: Context,response: UserSignupDataModel){

        if(response.status.equals(Constants.SUCCESS,true)){
            val pref=context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).edit()

            pref.putString(Constants.PREF_USER_TOKEN,response.data.jwtToken).apply()
            pref.putString(Constants.PREF_USER_NAME,response.data.name)
            pref.putString(Constants.PREF_USER_PROFILE,response.data.profilePic)
            pref.putBoolean(Constants.PREF_NOTIFICATION_STATUS,response.data.notificationStatus)
            pref.putBoolean(Constants.PREF_SOUND_STATUS,response.data.soundStatus)
            pref.putString(Constants.PREF_USER_NUMBER,response.data.mobileNumber)
            pref.putString(Constants.PREF_COUNTRY_CODE,response.data.countryCode)
            pref.putString(Constants.PREF_PICK_FAV, TextUtils.join(",",response.data.pickFavorites))


            pref.apply()
        }




    }


}