package com.youpic.screens.declineActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.CancelMeetingRequestDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DeclineViewModel: ApiBaseViewModel() {

    val cancelMeetingRequestResponse=MutableLiveData<CancelMeetingRequestDataModel>()


    fun cancelMeetingRequestApi(context: Context,meetingId:String){

        CustomProgressBar.getCustomDialog(context)
        apiInterface.cancelMeetingRequestApi(Utils.getUserToken(context),meetingId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    cancelMeetingRequestResponse.value=it
                           CustomProgressBar.dismissDialog()
                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })

    }

}