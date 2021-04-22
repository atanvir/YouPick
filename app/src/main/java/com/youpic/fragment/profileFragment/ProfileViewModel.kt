package com.youpic.fragment.profileFragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.Body.ChangeNotificationStatusBody
import com.youpic.apiConnection.DataModel.*
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_NOTIFICATION_STATUS
import com.youpic.utils.Constants.Companion.PREF_SOUND_STATUS
import com.youpic.utils.Constants.Companion.PREF_USER_PROFILE
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*


class ProfileViewModel: ApiBaseViewModel()
{
    var logoutResponse=MutableLiveData<UserLogoutDataModel>()
    var terminateAccountDataModel= MutableLiveData<TerminateAccountDataModel>()
    var changeNotificationStatusApiResponse=MutableLiveData<ChangeNotificationStatusDataModel>()
    var updateUserProfileResponse=MutableLiveData<UserUpdateProfileDataModel>()
    var userUpdateDetailsResponse=MutableLiveData<UserUpdateDetailsDataModel>()
    var error=MutableLiveData<Throwable>()


    fun logoutApi(context: Context, userToken: String?)
    {
        CustomProgressBar.getCustomDialog(context)

        apiInterface.userLogoutApi(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            logoutResponse.value = it
                            CustomProgressBar.dismissDialog()
                        },
                        {
                            error.value = it
                            ErrorHandler.handlerError(context, it)
                            CustomProgressBar.dismissDialog()
                        })

    }

    fun terminateAccountApi(context: Context, userToken: String)
    {
        CustomProgressBar.getCustomDialog(context)
        apiInterface.terminateAccountApi(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            terminateAccountDataModel.value = it
                            CustomProgressBar.dismissDialog()
                        },
                        {
                            error.value = it
                            ErrorHandler.handlerError(context, it)
                            CustomProgressBar.dismissDialog()
                        })
    }

    fun changeNotificationStatusApi(context: Context, userToken: String, body: ChangeNotificationStatusBody){
        CustomProgressBar.getCustomDialog(context)

        apiInterface.changeNotificationStatusApi(userToken, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()
                            saveNotificationSetting(context, it)
                            changeNotificationStatusApiResponse.value = it

                        }, {
                    CustomProgressBar.dismissDialog()
                    error.value = it
                    ErrorHandler.handlerError(context, it)
                })
    }

    fun userUpdateProfileApi(context: Context, userToken: String, image: MultipartBody.Part){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.userUpdateProfileApi(userToken, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    CustomProgressBar.dismissDialog()
                    saveProfileImage(context, it)
                    updateUserProfileResponse.value = it
                }, {
                    CustomProgressBar.dismissDialog()
                    error.value = it
                    ErrorHandler.handlerError(context, it)
                })
    }

    fun userUpdateDetailsApi(context: Context, name: String){

        CustomProgressBar.getCustomDialog(context)
        apiInterface.userUpdateDetailsApi(Utils.getUserToken(context), UpdateprofileBody(name).body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    CustomProgressBar.dismissDialog()
                    userUpdateDetailsResponse.value = it

                }, {
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context, it)
                })
    }

    private fun saveNotificationSetting(context: Context, response: ChangeNotificationStatusDataModel){

        if(response.status.equals(SUCCESS, false)){
            val pref=context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit()

            pref.putBoolean(PREF_NOTIFICATION_STATUS, response.data.notificationStatus)
            pref.putBoolean(PREF_SOUND_STATUS, response.data.soundStatus)

            pref.apply()

        }


    }

    private fun saveProfileImage(context: Context, response: UserUpdateProfileDataModel){
        if(response.status.equals(SUCCESS, false)){
            val pref=context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit()

            pref.putString(PREF_USER_PROFILE, response.data)

            pref.apply()

        }
    }

    class UpdateprofileBody {
        private val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
        private val requestBodyMap: MutableMap<String, RequestBody> = HashMap()

        constructor(name: String) {

            requestBodyMap.put("name", name.toRequestBody(mediaType))

        }



        val body: Map<String, RequestBody>
            get() = requestBodyMap
    }





    /**/



}