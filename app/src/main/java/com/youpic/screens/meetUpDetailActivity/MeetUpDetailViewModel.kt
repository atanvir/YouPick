package com.youpic.screens.meetUpDetailActivity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.youpic.apiConnection.Body.WinnerAndSelectionBody
import com.youpic.apiConnection.DataModel.GetRestaurantForFinalCallDataModel
import com.youpic.apiConnection.DataModel.WinnerAndSelectionDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MeetUpDetailViewModel: ApiBaseViewModel() {

    var getRestaurantForFinalCallResponse= MutableLiveData<GetRestaurantForFinalCallDataModel>()
    var winnerAndSelectionResponse= MutableLiveData<WinnerAndSelectionDataModel>()


    fun getRestaurantForFinalCallApi(context:Context,meetingId:String){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.getRestaurantForFinalCallApi(Utils.getUserToken(context),meetingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           CustomProgressBar.dismissDialog()

                    var list=it.data.finalRestaurant

                    var i=0
                    while (i<list.size){

                        var j=0
                        while (j<list.size){


                            if(i==j){
                                if (list.get(i).userList.isNullOrEmpty()){
                                    list.get(i).userList=ArrayList()
                                    list.get(i).userList!!.add(list.get(i).user)
                                    j++
                                }
                            }else{
                                if(list.get(i).restaurant.id==list.get(j).restaurant.id){
                                    var userAlreadyAdded=false
                                    for( userId in list.get(i).userList){
                                        if(userId.id==list.get(j).user.id)
                                        {
                                            userAlreadyAdded=true
                                            break
                                        }
                                    }
                                    if(!userAlreadyAdded)
                                        list.get(i).userList.add(list.get(j).user)
                                    list.removeAt(j)
                                }else{
                                    j++
                                }
                            }

                        }
                        i++

                    }


                    it.data.finalRestaurant=list


                    getRestaurantForFinalCallResponse.value=it

                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })
    }

    fun winnerAndSelectionApi(context:Context,body: WinnerAndSelectionBody){

        CustomProgressBar.getCustomDialog(context)

        apiInterface.winnerAndSelectionApi(Utils.getUserToken(context),body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           CustomProgressBar.dismissDialog()
                    winnerAndSelectionResponse.value=it

                },{
                    ErrorHandler.handlerError(context,it)
                    CustomProgressBar.dismissDialog()
                })
    }



}