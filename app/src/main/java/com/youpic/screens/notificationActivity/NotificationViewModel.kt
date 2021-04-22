package com.youpic.screens.notificationActivity

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.Settings.Global.getString
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.youpic.BuildConfig
import com.youpic.R
import com.youpic.apiConnection.DataModel.GetNotificationListDataModel
import com.youpic.apiConnection.DataModel.TakeAPictureDataModel
import com.youpic.baseClass.ApiBaseViewModel
import com.youpic.utils.CustomProgressBar
import com.youpic.utils.ErrorHandler
import com.youpic.utils.GetPath
import com.youpic.utils.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class NotificationViewModel:ApiBaseViewModel()
{
    var notificationListResponse=MutableLiveData<GetNotificationListDataModel>()
    var takeAPictureApiResponse=MutableLiveData<TakeAPictureDataModel>()
    var error=MutableLiveData<Throwable>()
    var filePath=MutableLiveData<String>()



    fun getNotificationListApi(context: Context, userToken: String){
        CustomProgressBar.getCustomDialog(context)

        apiInterface.getNotificationListApi(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()
                            notificationListResponse.value = it
                        },
                        {
                            CustomProgressBar.dismissDialog()
                            error.value = it
                            ErrorHandler.handlerError(context, it)
                        })

    }

    fun takeAPictureApi(context: Context, image: MultipartBody.Part, notificationId: String, restaurantId: String){
        CustomProgressBar.getCustomDialog(context)

        apiInterface.takeAPictureApi(Utils.getUserToken(context), image, TakePicBody(notificationId, restaurantId).body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            CustomProgressBar.dismissDialog()
                            takeAPictureApiResponse.value = it

                        },
                        {
                            CustomProgressBar.dismissDialog()
                            ErrorHandler.handlerError(context, it)
                        })

    }


    fun saveImage(context: Context, bmp: Bitmap){
        CustomProgressBar.getCustomDialog(context)

        convertImage(context, bmp).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    filePath.value = it
                    CustomProgressBar.dismissDialog()
                }, {
                    ErrorHandler.handlerError(context, it)
                    CustomProgressBar.dismissDialog()
                })


    }

    fun convertImage(context: Context, bitmap: Bitmap): Observable<String> {

        return Observable.create {

            emit->

            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            val image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir /* directory */
            )


            try {
                val out = FileOutputStream(image)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            emit.onNext(image!!.absolutePath)
           // emit.onNext("")
        }

    }

   /* fun convertImage(context: Context, bitmap: Bitmap): Observable<String> {

        return Observable.create {

            emit->
            val root = Environment.getExternalStorageDirectory().absolutePath
            val myDir = File("$root/saved_images")
            myDir.mkdirs()

            val fname = "YouPick_"+SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().time)+ ".jpg"
            val file = File(myDir, fname)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            emit.onNext(file!!.absolutePath)
           // emit.onNext("")
        }

    }*/

    fun convertImage2(context: Context, bitmap: Bitmap): Observable<String> {

        return Observable.create {

            emit->

            val folder = File("${context.getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
            folder.mkdirs()
            val fname = "YouPick_"+SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().time)+ ".jpg"

            val file = File(folder, fname)
            if (file.exists())
                file.delete()
            file.createNewFile()
            val imageUri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
            )

           // emit.onNext(imageUri)
            emit.onNext(file.absolutePath)
            /*return imageUri!!


            val root = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
            val myDir = File("$root/saved_images")
            myDir.mkdirs()

            val fname = "YouPick_"+SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().time)+ ".jpg"
            val file = File(myDir, fname)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            emit.onNext(file!!.absolutePath)
            */// emit.onNext("")

        }

    }

    class TakePicBody {
        private val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
        private val requestBodyMap: MutableMap<String, RequestBody> = HashMap()

        constructor(notificationId: String, restaurantId: String) {

            requestBodyMap.put("notificationId", notificationId.toRequestBody(mediaType))
            requestBodyMap.put("restaurantId", restaurantId.toRequestBody(mediaType))

        }



        val body: Map<String, RequestBody>
            get() = requestBodyMap
    }


}