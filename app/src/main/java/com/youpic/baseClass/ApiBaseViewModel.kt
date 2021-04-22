package com.youpic.baseClass

import androidx.lifecycle.ViewModel
import com.youpic.apiConnection.ApiInterface
import com.youpic.apiConnection.RetroClient
import com.youpic.utils.Constants.Companion.BASE_URL

abstract class ApiBaseViewModel: ViewModel() {
    val apiInterface:ApiInterface by lazy { RetroClient.getApiClient(BASE_URL) }
}