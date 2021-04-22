package com.youpic.apiConnection

import com.youpic.apiConnection.Body.*
import com.youpic.apiConnection.DataModel.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiInterface
{
    @FormUrlEncoded
    @POST("user/checkUserMobileNumber")
    fun checkUserMobileNumberApi(@Field("countryCode") countryCode:String,
                                 @Field("mobileNumber") mobileNumber:String):Observable<CheckUserMobileNumberDataModel2>

    @POST("user/userSignup")
    fun userSignUpApi(@Body() body:UserSignupBody):Observable<UserSignupDataModel>


    @FormUrlEncoded
    @POST("user/directLogin")
    fun directLoginApi(@Field("countryCode") countryCode:String,
                       @Field("mobileNumber") mobileNumber:String,
                       @Field("timezone") timezone:String,
                       @Field("deviceType") deviceType:String,
                       @Field("deviceToken") deviceToken:String?,
                       @Field("password") password:String):Observable<UserLoginDataModel>

    @FormUrlEncoded
    @POST("user/userLogin")
    fun userLoginApi(@Field("countryCode") countryCode:String,
                       @Field("mobileNumber") mobileNumber:String,
                       @Field("timezone") timezone:String,
                       @Field("deviceType") deviceType:String,
                       @Field("deviceToken") deviceToken:String?,
                       @Field("password") password:String):Observable<UserLoginDataModel>

    @FormUrlEncoded
    @POST("user/forgotPassword")
    fun forgotPasswordApi(@Field("password") password:String,
                       @Field("userId") userId:String):Observable<ForgotPasswordDataModel>

    @GET("user/getUserDetails")
    fun getUserDetailsApi(@Header("Authorization") Authorization:String):Observable<GetUserDetailsDataModel>

    @GET("user/getNotificationCount")
    fun getNotificationCountApi(@Header("Authorization") Authorization:String):Observable<GetNotificationCountDataModel>

    @GET("user/userLogout")
    fun userLogoutApi(@Header("Authorization") Authorization:String?):Observable<UserLogoutDataModel>

    @GET("user/terminateAccount")
    fun terminateAccountApi(@Header("Authorization") Authorization:String):Observable<TerminateAccountDataModel>

    @POST("user/changeNotificationStatus")
    fun changeNotificationStatusApi(@Header("Authorization") Authorization:String,
                       @Body() body:ChangeNotificationStatusBody):Observable<ChangeNotificationStatusDataModel>

    @FormUrlEncoded
    @POST("user/checkMobilForForgotPassword")
    fun checkMobilForForgotPasswordApi(@Field("countryCode") countryCode:String,
                          @Field("mobileNumber") mobileNumber:String):Observable<CheckMobilForForgotPasswordDataModel>

    @Multipart
    @POST("user/userUpdateProfile")
    fun userUpdateProfileApi(@Header("Authorization") Authorization:String,
                             @Part image: MultipartBody.Part):Observable<UserUpdateProfileDataModel>

    @Multipart
    @POST("user/userUpdateDetails")
    fun userUpdateDetailsApi(@Header("Authorization") Authorization:String,
                             @PartMap requestBody: Map<String,@JvmSuppressWildcards RequestBody>?):Observable<UserUpdateDetailsDataModel>
    @Multipart
    @POST("user/takeAPicture")
    fun takeAPictureApi(@Header("Authorization") Authorization:String,
                        @Part image:MultipartBody.Part,
                         @PartMap requestBody: Map<String,@JvmSuppressWildcards RequestBody>?):Observable<TakeAPictureDataModel>

    @GET("user/getNotificationList")
    fun getNotificationListApi(@Header("Authorization") Authorization:String):Observable<GetNotificationListDataModel>

    @GET("user/homeDataApi")
    fun homeDataApi(@Header("Authorization") Authorization:String):Observable<HomeDataDataModel>

    @FormUrlEncoded
    @POST("user/thingsNear")
    fun thingsNearApi(@Header("Authorization") Authorization:String,
    @Field("latitude") latitude:String,
    @Field("longitude") longitude:String):Observable<ThingsNearDataModel>

    @FormUrlEncoded
    @POST("user/getActivityData")
    fun getActivityDataApi(@Header("Authorization") Authorization:String,@Field("latitude") latitude:String,
                           @Field("longitude") longitude:String):Observable<GetActivityDataModel>

    @POST("user/getFriendList")
    fun getFriendListApi(@Header("Authorization") Authorization:String):Observable<GetFriendListDataModel>

    @POST("user/updatePhoneConatct")
    fun updatePhoneConatctApi(@Header("Authorization") Authorization:String,
                              @Body body:UpdatePhoneConatctBody):Observable<UpdatePhoneConatctDataModel>

    @POST("user/getContactList")
    fun getContactListApi(@Header("Authorization") Authorization:String):Observable<GetContactListDataModel>

    @POST("user/sendFrndRequest")
    fun sendFrndRequestApi(@Header("Authorization") Authorization:String,
                             @Body body:SendFrndRequestBody):Observable<SendFrndRequestDataModel>

    @FormUrlEncoded
    @POST("user/acceptOrDeclinedFrndRequest")
    fun acceptOrDeclinedFrndRequestApi(@Header("Authorization") Authorization:String,
                             @Field("inviteById") inviteById:String,
                             @Field("notificationId") notificationId:String,
                             @Field("status") status:String):Observable<AcceptOrDeclinedFrndRequestDataModel>

    @FormUrlEncoded
    @POST("user/getInviteByData")
    fun getInviteByDataApi(@Header("Authorization") Authorization:String,
                             @Field("inviteById") inviteById:String):Observable<GetInviteByDataDataModel>
    @FormUrlEncoded
    @POST("user/removeFriend")
    fun removeFriendApi(@Header("Authorization") Authorization:String,
                             @Field("frndId") frndId:String):Observable<RemoveFriendDataModel>

    @FormUrlEncoded
    @POST("user/addFriend")
    fun addFriendApi(@Header("Authorization") Authorization:String,
                             @Field("frndId") frndId:String):Observable<AddFriendDataModel>

    @POST("user/directMeetingRequest")
    fun directMeetingRequestApi(@Header("Authorization") Authorization:String,
                                @Body body: DirectMeetingRequestBody):Observable<DirectMeetingRequestDataModel>

    @FormUrlEncoded
    @POST("user/getMeetingInviteDetails")
    fun getMeetingInviteDetailsApi(@Header("Authorization") Authorization:String,
                                   @Field("meetingId") meetingId:String):Observable<GetMeetingInviteDetailsDataModel>

    @FormUrlEncoded
    @POST("user/directAcceptMeetingRequest")
    fun directAcceptMeetingRequestApi(@Header("Authorization") Authorization:String,
                                   @Field("meetingId") meetingId:String):Observable<DirectAcceptMeetingRequestDataModel>

    @FormUrlEncoded
    @POST("user/cancelMeetingRequest")
    fun cancelMeetingRequestApi(@Header("Authorization") Authorization:String,
                                   @Field("meetingId") meetingId:String):Observable<CancelMeetingRequestDataModel>

    @POST("user/meetingRequestProcessed")
    fun meetingRequestProcessedApi(@Header("Authorization") Authorization:String,
                                   @Body body:MeetingRequestProcessedBody):Observable<MeetingRequestProcessedDataModel>

    @POST("user/meetingRequest")
    fun meetingRequestApi(@Header("Authorization") Authorization:String,
                          @Body body:MeetingRequestBody):Observable<MeetingRequestProcessedDataModel>

    @FormUrlEncoded
    @POST("user/makeGroup")
    fun makeGroupApi(@Header("Authorization") Authorization:String,
                          @Field("meetingId") meetingId:String):Observable<MakeGroupDataModel>

    @POST("user/getFrndGroupList")
    fun getFrndGroupListApi(@Header("Authorization") Authorization:String):Observable<GetFrndGroupListDataModel>

    @POST("user/getResaturnatList")
    fun getResaturnatListApi(@Header("Authorization") Authorization:String,
                             @Body body: GetResaturnatListBody):Observable<GetResaturnatListDataModel>


    @POST("user/getCuisineList")
    fun getCuisineListApi():Observable<GetCuisineListDataModel>

    @POST("user/winnerAndSelection")
    fun winnerAndSelectionApi(@Header("Authorization") Authorization:String,
                              @Body body:WinnerAndSelectionBody):Observable<WinnerAndSelectionDataModel>

    @POST("user/addFavoritePick")
    fun addFavoritePickApi(@Header("Authorization") Authorization:String,
                              @Body body:AddFavoritePickBody):Observable<AddFavoritePickDataModel>

    @FormUrlEncoded
    @POST("user/getMeetingDetails")
    fun getMeetingDetailsApi(@Header("Authorization") Authorization:String,
                              @Field("meetingId") meetingId:String):Observable<GetMeetingDetailsDataModel>

    @FormUrlEncoded
    @POST("user/getRestaurantForFinalCall")
    fun getRestaurantForFinalCallApi(@Header("Authorization") Authorization:String,
                                    @Field("meetingId") meetingId:String):Observable<GetRestaurantForFinalCallDataModel>

    @FormUrlEncoded
    @POST("user/removeGroup")
    fun removeGroupApi(@Header("Authorization") Authorization:String,
                                    @Field("groupId") groupId:String):Observable<RemoveGroupDataModel>

    @FormUrlEncoded
    @POST("user/getRestaurantData")
    fun getRestaurantDataApi(@Header("Authorization") Authorization:String,
                                    @Field("restaurantId") restaurantId:String):Observable<GetRestaurantDataDataModel>



    @FormUrlEncoded
    @POST("user/updateLocation")
    fun updateLocation(@Header("Authorization") Authorization:String,
                       @Field("latitude") latitude: Double,
                       @Field("longitude") longitude: Double): Observable<Any>
}