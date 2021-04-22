package com.youpic.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.youpic.apiConnection.Body.MeetingRequestBody
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_ID

object FriendList {

    var selectedFriendList= HashMap<String, MeetingRequestBody.UsersData>()
    var selectedPosition=-1

    fun getFriendList(context:Context):List<MeetingRequestBody.UsersData>{
        selectedFriendList.remove(context.getSharedPreferences(PREF_NAME,MODE_PRIVATE).getString(PREF_USER_ID,""))
        return  ArrayList(selectedFriendList!!.values)
    }

    fun getFriendListWithMe(context:Context):List<MeetingRequestBody.UsersData>{
        if(!selectedFriendList.containsKey(context.getSharedPreferences(PREF_NAME,MODE_PRIVATE).getString(PREF_USER_ID,""))){
            selectedFriendList.put(context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_USER_ID,"")!!,
                    MeetingRequestBody.UsersData(context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).getString(Constants.PREF_USER_ID,"")!!,
                            "Me",
                            Location(context).getLat().toString(),
                            Location(context).getLong().toString()))
        }
        return  ArrayList(selectedFriendList!!.values)
    }
}