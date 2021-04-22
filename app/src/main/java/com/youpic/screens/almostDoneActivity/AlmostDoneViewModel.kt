package com.youpic.screens.almostDoneActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.Body.DirectMeetingRequestBody
import com.youpic.apiConnection.Body.MeetingRequestBody
import com.youpic.apiConnection.DataModel.DirectMeetingRequestDataModel
import com.youpic.apiConnection.DataModel.MeetingRequestProcessedDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AlmostDoneViewModel:ApiBaseViewModel() {

    var meetingRequestResponse= MutableLiveData<MeetingRequestProcessedDataModel>()
    var directMeetingRequestResponse= MutableLiveData<DirectMeetingRequestDataModel>()


    fun meetingRequestApi(context: Context,body:MeetingRequestBody){

        CustomProgressBar.getCustomDialog(context)
        apiInterface.meetingRequestApi(Utils.getUserToken(context),body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                           CustomProgressBar.dismissDialog()
                    meetingRequestResponse.value=it

                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })

    }

    fun directMeetingRequestApi(context: Context,body: DirectMeetingRequestBody){

        CustomProgressBar.getCustomDialog(context)
        apiInterface.directMeetingRequestApi(Utils.getUserToken(context),body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                           CustomProgressBar.dismissDialog()
                    directMeetingRequestResponse.value=it

                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })

    }

}