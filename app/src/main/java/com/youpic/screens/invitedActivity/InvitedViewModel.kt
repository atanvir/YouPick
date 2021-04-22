package com.youpic.screens.invitedActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.DirectAcceptMeetingRequestDataModel
import com.youpic.apiConnection.DataModel.GetMeetingInviteDetailsDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class InvitedViewModel : ApiBaseViewModel(){

    val getMeetingInviteDetailsResponse=MutableLiveData<GetMeetingInviteDetailsDataModel>()
    val directAcceptMeetingRequestResponse=MutableLiveData<DirectAcceptMeetingRequestDataModel>()


    fun directAcceptMeetingRequestApi(context: Context,meetingId:String){
        CustomProgressBar.getCustomDialog(context)
        apiInterface.directAcceptMeetingRequestApi(Utils.getUserToken(context),meetingId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    directAcceptMeetingRequestResponse.value=it
                    CustomProgressBar.dismissDialog()
                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })

    }

    fun getMeetingInviteDetailsApi(context: Context,meetingId:String){
        CustomProgressBar.getCustomDialog(context)
        apiInterface.getMeetingInviteDetailsApi(Utils.getUserToken(context),meetingId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    getMeetingInviteDetailsResponse.value=it
                    CustomProgressBar.dismissDialog()
                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })

    }


}