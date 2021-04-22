package com.youpic.screens.loginActivity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.room.util.StringUtil
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.apiConnection.DataModel.UserLoginDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.utils.Constants
import com.youpic.utils.CustomProgressBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel: ApiBaseViewModel()
{
    var loginResponse=MutableLiveData<UserLoginDataModel>()
    var error=MutableLiveData<Throwable>()

    fun userLoginApi(context: Context,countryCode:String,mobileNumber:String,timezone:String,
                     deviceType:String,deviceToken:String?,password:String){
        CustomProgressBar.getCustomDialog(context)
        apiInterface.userLoginApi(countryCode,mobileNumber,timezone,deviceType,deviceToken,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            loginResponse.value=it
                            CustomProgressBar.dismissDialog()
                            saveHomeData(context,it)
                        },
                        {
                            error.value=it
                            CustomProgressBar.dismissDialog()
                        })
    }

    fun directLoginApi(context: Context,countryCode:String,mobileNumber:String,timezone:String,
                     deviceType:String,deviceToken:String?,password:String){
        CustomProgressBar.getCustomDialog(context)
        apiInterface.directLoginApi(countryCode,mobileNumber,timezone,deviceType,deviceToken,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            loginResponse.value=it
                            CustomProgressBar.dismissDialog()
                            saveHomeData(context,it)
                        },
                        {
                            error.value=it
                            CustomProgressBar.dismissDialog()
                        })
    }


    private fun saveHomeData(context: Context,response: UserLoginDataModel){

        if(response.status.equals(Constants.SUCCESS,true)){
            val pref=context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).edit()

            pref.putString(Constants.PREF_USER_TOKEN,response.data.jwtToken).apply()
            pref.putString(Constants.PREF_USER_NAME,response.data.name)
            pref.putString(Constants.PREF_USER_PROFILE,response.data.profilePic)
            pref.putBoolean(Constants.PREF_NOTIFICATION_STATUS,response.data.notificationStatus)
            pref.putBoolean(Constants.PREF_SOUND_STATUS,response.data.soundStatus)
            pref.putString(Constants.PREF_USER_NUMBER,response.data.mobileNumber)
            pref.putString(Constants.PREF_COUNTRY_CODE,response.data.countryCode)
            pref.putString(Constants.PREF_PICK_FAV,TextUtils.join(",",response.data.pickFavorites))


            pref.apply()
        }




    }
}