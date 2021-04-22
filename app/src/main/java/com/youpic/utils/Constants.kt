package com.youpic.utils

public class Constants {

    companion object{
        const val BASE_URL="http://18.225.14.119:2025/api/v1/";
        const val MSG_SNACK="snackbar";
        const val MSG_TOAST="toast";
        const val MSG_DIALOG="dialog";
        const val SUCCESS="200";
        const val ANDROID="Android";
        const val DISTANCE="10";


        /*-----------------Shared Pref---------------*/
        const val PREF_NAME="YouPic_Pref"
        const val PREF_DEVICE_TOKEN="Device_Token"
        const val PREF_USER_TOKEN="User_Token"
        const val PREF_USER_NAME="User_Name"
        const val PREF_USER_NUMBER="User_Number"
        const val PREF_COUNTRY_CODE="country_code"
        const val PREF_USER_ID="userId"
        const val PREF_USER_PROFILE="userProfile"
        const val PREF_SCHEDULE_COUNT="scheduleCount"
        const val PREF_OUTGOING_COUNT="outgoingCount"
        const val PREF_INCOMING_COUNT="incomingCount"
        const val PREF_NOTI_COUNT="notiCount"
        const val PREF_NOTIFICATION_STATUS="NotificationStatus"
        const val PREF_SOUND_STATUS="SoundStatus"
        const val PREF_DEEPLINK="deeeplink"
        const val PREF_POINT="user_point"
        const val PREF_BADGE_NAME="badge_name"
        const val PREF_COUNTRY_NAME_CODE="country_name_code"
        const val PREF_BADGE_IMG="badge_img"
        const val PREF_PICK_FAV="pickFavorites"
        const val TOTAL_HOUR=1
        const val TOTAL_NOW_MIN=30
        const val TOTAL_LATER_TODAY_HOUR=1
        const val TOTAL_ANOTHER_HOUR=2

        const val DATE_FORMAT="yyyy-MM-dd"
        /*----------------XXXXXXXX-------------------*/


        /*------------------Notification Type--------------*/

        const val NOTI_TYPE_FRIEND_REQUEST="frndReuest"
        const val NOTI_TYPE_MEETING_REQUEST="meetingRequest"
        const val NOTI_TYPE_JOIN="joinYouPick"
        const val NOTI_TYPE_MEETING_REQ_ACCEPT="meetingRequestAccept"
        const val NOTI_TYPE_MEETING_FINAL_CALL="finalCall"
        const val NOTI_TYPE_MEETING_TAKE_PICTURE="takePicture"
        const val NOTI_TYPE_MEETING_WINNER="winner"
        const val NOTI_TYPE_FRIEND_REQ_DECLINE="frndReuestDecline"
        const val NOTI_TYPE_MEETING_CANCEL="cancelMeeting"

        /*------------------XXXXXXXXXXXXXXXX----------------*/
    }

}