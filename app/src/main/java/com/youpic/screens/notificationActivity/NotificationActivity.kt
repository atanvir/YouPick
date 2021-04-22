package com.youpic.screens.notificationActivity

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.youpic.R
import com.youpic.adapter.NotificationAdapter
import com.youpic.baseClass.BaseActivity
import com.youpic.utils.Constants.Companion.MSG_DIALOG
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_NOTI_COUNT
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.GetPath
import com.youpic.utils.TakeImage
import com.youpic.utils.Utils
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.common_titlebar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class NotificationActivity : BaseActivity(),NotificationAdapter.OnTakePicture
{

    var viewModel=NotificationViewModel()
    val FRIEND_REQ_NOTIFICATION_REQ_CODE=121;
    val PERMISSION_CODE=121
    val GALLERY_PICTURE=1
    val CAMERA_REQUEST=2
    var selectedNotificationId=""
    var selectedRestroId=""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()


        init()
        initControl()
        myObserver()

    }

    override fun init() {
        viewModel=ViewModelProvider(this).get(NotificationViewModel::class.java)

        titleTextView.text = "Notifications"

        viewModel.getNotificationListApi(this, Utils.getUserToken(this))

    }

    override fun initControl() {
        backTextView.setOnClickListener { finish() }
        button3.setOnClickListener{finish()}
    }

    override fun myObserver()
    {
        viewModel.notificationListResponse.observe(this, {
            if (it.status == SUCCESS) {
                getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().remove(PREF_NOTI_COUNT).apply()
                notificationRecyclerView.layoutManager = LinearLayoutManager(this@NotificationActivity)
                notificationRecyclerView.adapter = NotificationAdapter(this, it.data,this)
            } else {
                Utils.showMessage(this, it.message, MSG_TOAST)
            }
        })



    }



    override fun takePicture(notificationId: String, restroId: String, position: Int) {


        selectedNotificationId=notificationId
        selectedRestroId=restroId
        checkPermission()


    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            profileBottomLayout()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {

                var grant=true

                for(result in grantResults){
                    if(result!= PackageManager.PERMISSION_GRANTED){
                        grant=false
                        break
                    }
                }

                if(grant){
                    profileBottomLayout()
                }else {
                    // Utils.Companion.showMessage(LocationSharingActivity.this, "",MSG_SNACK);
                }

            }


        }
    }

    private fun profileBottomLayout() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val sheetView: View = this.layoutInflater.inflate(R.layout.bottom_dialog, null)
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
        val TakePhotoTextView = sheetView.findViewById<TextView>(R.id.TakePhotoTextView)
        val CameraRollTextView = sheetView.findViewById<TextView>(R.id.cameraRollTextView)
        val CancelTextView = sheetView.findViewById<TextView>(R.id.cancelTextView)

        CameraRollTextView.setOnClickListener {
            val mimeTypes = arrayOf("image/*")
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "ChooseFile"), GALLERY_PICTURE)
            bottomSheetDialog.dismiss()
        }

        TakePhotoTextView.setOnClickListener {
            val intent = Intent(this, TakeImage::class.java)
            intent.putExtra("from", "camera")
            startActivityForResult(intent, CAMERA_REQUEST)
            bottomSheetDialog.dismiss()
        }

        CancelTextView.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            try {
                if (data?.getStringExtra("filePath") != null) {
                    val selectedImagePath = data.getStringExtra("filePath")

                    uploadImage(selectedImagePath!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Utils.showMessage(this, "Something went wrong", MSG_TOAST)
            }
        }
        else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            try {
                val selectedImageUri: Uri? = data?.data
                val selectedImagePath: String
                selectedImagePath = GetPath.getPath(this, selectedImageUri)

                uploadImage(selectedImagePath)
            } catch (e: Exception) {
                e.printStackTrace()
                Utils.showMessage(this, "Something went wrong", MSG_TOAST)
            }
        }
        else if(requestCode==FRIEND_REQ_NOTIFICATION_REQ_CODE){
            viewModel.getNotificationListApi(this, Utils.getUserToken(this))
        }


    }

    fun uploadImage(path:String){

        val imgFile = File(path)
        val imagesBody = Compressor(this).compressToFile(imgFile).asRequestBody("image/*".toMediaTypeOrNull())
        val imgpart = MultipartBody.Part.createFormData("profilePic", imgFile.name, imagesBody)

        viewModel.takeAPictureApi(this,imgpart,selectedNotificationId,selectedRestroId)
    }

}