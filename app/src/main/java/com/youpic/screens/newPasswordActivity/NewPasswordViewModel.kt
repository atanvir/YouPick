package com.youpic.screens.newPasswordActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.ForgotPasswordDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewPasswordViewModel:ApiBaseViewModel() {

    var forgotPasswordresponse=MutableLiveData<ForgotPasswordDataModel>()
    var error=MutableLiveData<Throwable>()


    fun forgotPasswordApi(context: Context,password:String,userId:String){
        CustomProgressBar.getCustomDialog(context)
        apiInterface.forgotPasswordApi(password,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()
                            forgotPasswordresponse.value=it
                        },
                        {
                            CustomProgressBar.dismissDialog()
                            ErrorHandler.handlerError(context,it)
                        })
    }



}