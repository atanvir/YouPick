package com.youpic.screens.friendRequestActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.AcceptOrDeclinedFrndRequestDataModel
import com.youpic.apiConnection.DataModel.GetInviteByDataDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FriendRequestViewModel: ApiBaseViewModel() {

    var acceptOrDeclinedFrndRequestResponse=MutableLiveData<AcceptOrDeclinedFrndRequestDataModel>()
    var getInviteByDataResponse=MutableLiveData<GetInviteByDataDataModel>()

    //var compositeDisposable:CompositeDisposable?= CompositeDisposable()

    fun acceptOrDeclinedFrndRequestApi(context: Context,invitedBy:String,notificationId:String,status:String){

        CustomProgressBar.getCustomDialog(context)
        apiInterface.acceptOrDeclinedFrndRequestApi(Utils.getUserToken(context),invitedBy,notificationId,status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           CustomProgressBar.dismissDialog()
                    acceptOrDeclinedFrndRequestResponse.value=it

                },{
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context,it)
                })/*.let {
                    compositeDisposable?.add(it)
                }*/

    }

    fun getInviteByDataApi(context: Context,invitedBy:String){

        CustomProgressBar.getCustomDialog(context)
        apiInterface.getInviteByDataApi(Utils.getUserToken(context),invitedBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    CustomProgressBar.dismissDialog()
                    getInviteByDataResponse.value=it

                },{
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context,it)
                })

    }

   /* override fun onCleared() {
        super.onCleared()
        compositeDisposable?.dispose()
    }*/

}