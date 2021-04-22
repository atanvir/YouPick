package com.youpic.utils

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import com.youpic.utils.Constants.Companion.MSG_TOAST
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler
{
    fun handlerError(context: Context?,throwable: Throwable){

        Log.w("errorHandler", throwable.message.toString())
        if(context==null)
            return
        else
        {
            when(throwable){
                is ConnectException -> Utils.showMessage(context,"Server Connection Error", MSG_TOAST)
                is SocketException ->Utils.showMessage(context,"Socket Time Out Exception",MSG_TOAST)
                is SocketTimeoutException -> Utils.showMessage(context,"Socket Time Out Exception",MSG_TOAST)
                is UnknownHostException -> Utils.showMessage(context,"No Internet Connection",MSG_TOAST)
                is InternalError -> Utils.showMessage(context,"Internal Server Error",MSG_TOAST)
               /* else->Utils.showMessage(context,"Something went wrong!",MSG_TOAST)*/
            }
        }
    }
}