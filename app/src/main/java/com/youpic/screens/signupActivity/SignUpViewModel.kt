package com.youpic.screens.signupActivity

import CheckUserMobileNumberDataModel
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.CheckUserMobileNumberDataModel2
import com.youpic.apiConnection.DataModel.UserSignupDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignUpViewModel: ApiBaseViewModel()
{
    var checkUserMobileResponse=MutableLiveData<CheckUserMobileNumberDataModel2>()
    var error=MutableLiveData<Throwable>()


    fun checkUserMobileNumberApi(context: Context,countryCode: String,mobileNumber: String)
    {
        CustomProgressBar.getCustomDialog(context)
        apiInterface.checkUserMobileNumberApi(countryCode, mobileNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    checkUserMobileResponse.value=it
                    CustomProgressBar.dismissDialog()
                },{
                    error.value=it
                    CustomProgressBar.dismissDialog()
                })
    }




}