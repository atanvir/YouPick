package com.youpic.screens.youPickActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.Body.GetResaturnatListBody
import com.youpic.apiConnection.Body.MeetingRequestProcessedBody
import com.youpic.apiConnection.DataModel.GetResaturnatListDataModel
import com.youpic.apiConnection.DataModel.MeetingRequestProcessedDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class YouPickViewModel:ApiBaseViewModel() {

    var getResaturnatListResponse=MutableLiveData<GetResaturnatListDataModel>()
    var meetingRequestProcessedResponse=MutableLiveData<MeetingRequestProcessedDataModel>()

    fun getResaturnatListApi(context: Context,body: GetResaturnatListBody,showDialog:Boolean){

        if(showDialog)
            CustomProgressBar.getCustomDialog(context)

        apiInterface.getResaturnatListApi(Utils.getUserToken(context),body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(showDialog)
                        CustomProgressBar.dismissDialog()
                    getResaturnatListResponse.value=it
                },{
                    ErrorHandler.handlerError(context,it)

                    if(showDialog)
                        CustomProgressBar.dismissDialog()
                })

    }

    fun meetingRequestProcessedApi(context: Context,body: MeetingRequestProcessedBody){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.meetingRequestProcessedApi(Utils.getUserToken(context),body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    CustomProgressBar.dismissDialog()
                    meetingRequestProcessedResponse.value=it
                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })

    }


}