package com.youpic.screens.takeImageOptionActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.CancelMeetingRequestDataModel
import com.youpic.apiConnection.DataModel.GetRestaurantDataDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TakeImageOptionViewModel: ApiBaseViewModel() {

    val getRestaurantDataResponse= MutableLiveData<GetRestaurantDataDataModel>()


    fun getRestaurantDataApi(context: Context, meetingId:String){

        CustomProgressBar.getCustomDialog(context)
        apiInterface.getRestaurantDataApi(Utils.getUserToken(context),meetingId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    getRestaurantDataResponse.value=it
                    CustomProgressBar.dismissDialog()
                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })

    }

}