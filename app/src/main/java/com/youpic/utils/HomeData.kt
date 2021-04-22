package com.youpic.utils

import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.DataModel.GetActivityDataModel
import com.youpic.apiConnection.DataModel.GetContactListDataModel
import com.youpic.apiConnection.DataModel.HomeDataDataModel
import com.youpic.apiConnection.DataModel.ThingsNearDataModel

object HomeData {

    var homeData:HomeDataDataModel?=null
    var thingsNearApiResponse: ThingsNearDataModel?=null
    var getActivityDataApiResponse: GetActivityDataModel?=null
    var getContactListDataModelResponse:GetContactListDataModel?=null
}