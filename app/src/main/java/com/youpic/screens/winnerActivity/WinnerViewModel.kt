package com.youpic.screens.winnerActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.GetMeetingDetailsDataModel
import com.youpic.apiConnection.DataModel.MakeGroupDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WinnerViewModel : ApiBaseViewModel(){

    var makeGroupResponse=MutableLiveData<MakeGroupDataModel>();
    var getMeetingDetailsResponse=MutableLiveData<GetMeetingDetailsDataModel>();


    fun makeGroupApi(context:Context,meetingId:String){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.makeGroupApi(Utils.getUserToken(context),meetingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            makeGroupResponse.value=it
                            CustomProgressBar.dismissDialog()
                        },
                        {
                            CustomProgressBar.dismissDialog()
                            ErrorHandler.handlerError(context,it)
                        })
    }

    fun getMeetingDetailsApi(context:Context,meetingId:String){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.getMeetingDetailsApi(Utils.getUserToken(context),meetingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getMeetingDetailsResponse.value=it
                            CustomProgressBar.dismissDialog()
                        },
                        {
                            CustomProgressBar.dismissDialog()
                            ErrorHandler.handlerError(context,it)
                        })
    }





}