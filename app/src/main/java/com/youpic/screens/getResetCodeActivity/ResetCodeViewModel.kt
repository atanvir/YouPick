package com.youpic.screens.getResetCodeActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.CheckMobilForForgotPasswordDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ResetCodeViewModel: ApiBaseViewModel()
{
    var checkMobilForForgotPasswordResponse=MutableLiveData<CheckMobilForForgotPasswordDataModel>()
    var error=MutableLiveData<Throwable>()

    fun checkMobilForForgotPasswordApi(context: Context,countryCode:String,phoneNumber:String){

        CustomProgressBar.getCustomDialog(context)
        apiInterface.checkMobilForForgotPasswordApi(countryCode,phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           CustomProgressBar.dismissDialog()
                    checkMobilForForgotPasswordResponse.value=it
                },{
                    CustomProgressBar.dismissDialog()
                    ErrorHandler.handlerError(context,it)
                    error.value=it
                })
    }
}