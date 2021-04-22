package com.youpic.screens

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.naver.android.helloyako.imagecrop.view.ImageCropView
import com.youpic.BuildConfig
import com.youpic.R
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.notificationActivity.NotificationViewModel
import com.youpic.utils.Constants
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.GetPath
import com.youpic.utils.TakeImage
import com.youpic.utils.Utils
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_take_image.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class TakeImageActivity : BaseActivity()
{

    lateinit var viewModel:NotificationViewModel
    val FRIEND_REQ_NOTIFICATION_REQ_CODE=121;
    val PERMISSION_CODE=121
    val GALLERY_PICTURE=1
    val CAMERA_REQUEST=2
    val REQUEST_IMAGE_CAPTURE=4
    var  currentPhotoPath=""
    var selectedImage:String?=null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_image)

        init()
        initControl()
        myObserver()

    }

    override fun init() {

        viewModel=ViewModelProvider(this).get(NotificationViewModel::class.java)




    }

    override fun initControl() {

        camImageView.setOnClickListener {
            checkPermission()
        }

        submitButton.setOnClickListener {
            if(selectedImage==null){
                Utils.showMessage(this,"Please select image!", MSG_TOAST)
            }else{

                val imgFile = File(selectedImage)
                val imagesBody = Compressor(this).compressToFile(imgFile).asRequestBody("image/*".toMediaTypeOrNull())
                val imgpart = MultipartBody.Part.createFormData("image", imgFile.name, imagesBody)

                viewModel.takeAPictureApi(this,imgpart,
                        intent.getStringExtra("notificationId")!!,
                        intent.getStringExtra("restroId")!!)

            }

        }



    }

    override fun myObserver() {

        viewModel.takeAPictureApiResponse.observe(this,{
            if(it.status==SUCCESS){
                startActivity(Intent(this,ThankYouActivity::class.java))
                finish()
            }else{
                Utils.showMessage(this, it.message, Constants.MSG_TOAST)
            }

        })

        viewModel.filePath.observe(this,{
            uploadImage(it)
        })
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
            /*val intent = Intent(this, TakeImage::class.java)
            intent.putExtra("from", "camera")
            startActivityForResult(intent, CAMERA_REQUEST)*/
            cameraIntent()
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

                    croppedDialog(this,selectedImagePath!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Utils.showMessage(this, "Something went wrong", Constants.MSG_TOAST)
            }
        }
        else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            try {
                val selectedImageUri: Uri? = data?.data
                val selectedImagePath: String
                selectedImagePath = GetPath.getPath(this, selectedImageUri)

                croppedDialog(this,selectedImagePath)
            } catch (e: Exception) {
                e.printStackTrace()
                Utils.showMessage(this, "Something went wrong", Constants.MSG_TOAST)
            }
        }
        else if(requestCode==FRIEND_REQ_NOTIFICATION_REQ_CODE){
            viewModel.getNotificationListApi(this, Utils.getUserToken(this))
        }else if(resultCode== RESULT_OK && requestCode==REQUEST_IMAGE_CAPTURE){

            val selectedImagePath = currentPhotoPath

            croppedDialog(this,selectedImagePath)
        }


    }

    private fun cameraIntent(){

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }


    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents

        currentPhotoPath = image.absolutePath
        return image
    }

    fun croppedDialog(context: Context?, path: String?) {
        val dialog = Dialog(context!!, android.R.style.Theme_Black)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dailo_crop_image)
        val cropButton = dialog.findViewById<ImageView>(R.id.closeIv)
        val backIv = dialog.findViewById<ImageView>(R.id.backIv)
        val imageCropView: ImageCropView = dialog.findViewById(R.id.imageCropView)
        imageCropView.setImageFilePath(path)
        imageCropView.setAspectRatio(4, 3)
        cropButton.setOnClickListener {
            imageCropView.saveState()

            viewModel.saveImage(context,imageCropView.croppedImage)


            dialog.dismiss()
        }
        backIv.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun uploadImage(path:String){

        Log.w("filePath",path)
        selectedImage=path
        Glide.with(this).load(path).placeholder(R.drawable.round_corner_grey).into(selectedImageView)

    }



}