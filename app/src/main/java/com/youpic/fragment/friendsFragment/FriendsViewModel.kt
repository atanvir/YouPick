package com.youpic.fragment.friendsFragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.GetFriendListDataModel
import com.youpic.apiConnection.DataModel.GetFrndGroupListDataModel
import com.youpic.apiConnection.DataModel.RemoveFriendDataModel
import com.youpic.apiConnection.DataModel.RemoveGroupDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FriendsViewModel:ApiBaseViewModel() {

    var getFriendListResponse=MutableLiveData<GetFriendListDataModel>()
    var removeFriendResponse=MutableLiveData<RemoveFriendDataModel>()
    var getFrndGroupListResponse=MutableLiveData<GetFrndGroupListDataModel>()
    var removeGroupResponse=MutableLiveData<RemoveGroupDataModel>()

    fun getFriendListApi(context: Context){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.getFriendListApi(Utils.getUserToken(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()
                            getFriendListResponse.value=it
                        },
                        {
                            CustomProgressBar.dismissDialog()
                            ErrorHandler.handlerError(context,it)
                        })

    }

    fun removeFriendApi(context: Context,friendId:String){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.removeFriendApi(Utils.getUserToken(context),friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()
                            removeFriendResponse.value=it
                        },
                        {
                            CustomProgressBar.dismissDialog()
                            ErrorHandler.handlerError(context,it)
                        })

    }


    fun getFrndGroupListApi(context: Context)
    {
        CustomProgressBar.getCustomDialog(context)
        apiInterface.getFrndGroupListApi(Utils.getUserToken(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           CustomProgressBar.dismissDialog()
                    getFrndGroupListResponse.value=it
                },{
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context,it)
                })
    }

    fun removeGroupApi(context: Context,groupId:String)
    {
        CustomProgressBar.getCustomDialog(context)
        apiInterface.removeGroupApi(Utils.getUserToken(context),groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           CustomProgressBar.dismissDialog()
                    removeGroupResponse.value=it
                },{
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context,it)
                })
    }


}