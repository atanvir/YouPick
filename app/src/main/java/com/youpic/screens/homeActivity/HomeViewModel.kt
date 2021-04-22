package com.youpic.screens.homeActivity

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.AddFriendDataModel
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.Constants.Companion.PREF_INCOMING_COUNT
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_NOTI_COUNT
import com.youpic.utils.Constants.Companion.PREF_OUTGOING_COUNT
import com.youpic.utils.Constants.Companion.PREF_SCHEDULE_COUNT
import com.youpic.utils.Constants.Companion.PREF_USER_ID
import com.youpic.utils.Constants.Companion.PREF_USER_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_PROFILE
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel:ApiBaseViewModel()
{
    var addFriendResponse=MutableLiveData<AddFriendDataModel>()

    fun addFriendApi(context: Context,friendId:String){
        CustomProgressBar.getCustomDialog(context)
        apiInterface.addFriendApi(Utils.getUserToken(context),friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    CustomProgressBar.dismissDialog()
                    addFriendResponse.value=it
                },{
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context,it)
                })

    }

}