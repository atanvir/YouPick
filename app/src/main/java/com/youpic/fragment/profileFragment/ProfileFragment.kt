package com.youpic.fragment.profileFragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.youpic.BuildConfig
import com.youpic.R
import com.youpic.apiConnection.Body.ChangeNotificationStatusBody
import com.youpic.baseClass.BaseFragment
import com.youpic.screens.getResetCodeActivity.GetResetCodeActivity
import com.youpic.screens.loginActivity.LoginActivity
import com.youpic.utils.Constants.Companion.MSG_DIALOG
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.PREF_COUNTRY_CODE
import com.youpic.utils.Constants.Companion.PREF_COUNTRY_NAME_CODE
import com.youpic.utils.Constants.Companion.PREF_NAME
import com.youpic.utils.Constants.Companion.PREF_NOTIFICATION_STATUS
import com.youpic.utils.Constants.Companion.PREF_SOUND_STATUS
import com.youpic.utils.Constants.Companion.PREF_USER_NAME
import com.youpic.utils.Constants.Companion.PREF_USER_NUMBER
import com.youpic.utils.Constants.Companion.PREF_USER_PROFILE
import com.youpic.utils.Constants.Companion.PREF_USER_TOKEN
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.GetPath
import com.youpic.utils.TakeImage
import com.youpic.utils.Utils
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : BaseFragment(), View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    lateinit var profileViewModel:ProfileViewModel
    val PERMISSION_CODE=121
    val GALLERY_PICTURE=1
    val CAMERA_REQUEST=2
    val REQUEST_IMAGE_CAPTURE=4
    val CROP_IMAGE_REQ_CODE=3
    var  currentPhotoPath=""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel=ViewModelProvider(this).get(ProfileViewModel::class.java)



        observer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state", "profile")

    }

    override fun init() {
        Glide.with(requireActivity()).load(requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_PROFILE, "")).placeholder(R.drawable.default_profile_image).circleCrop().into(profileImageView)
        nameEditText.setText(requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_NAME, ""))
        phoneEditText.setText(requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_NUMBER, ""))

        if(requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_COUNTRY_NAME_CODE, "")!!.isEmpty()){
            countryCodePicker.setCountryForPhoneCode(Integer.parseInt(requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_COUNTRY_CODE, "+0")?.replace("+", "").toString()))
        }else{
            countryCodePicker.setCountryForNameCode(requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_COUNTRY_NAME_CODE, "")!!)
        }


        countryCodePicker.isEnabled=false

        Log.w("testCC","Country Name: "+countryCodePicker.selectedCountryName)
        Log.w("testCC","Country Coude: "+countryCodePicker.selectedCountryCode)

        Log.w("testCC", "test " + requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean(PREF_SOUND_STATUS, false))
        appSoundToggle.isChecked=requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean(PREF_SOUND_STATUS, false)
        notificationToggle.isChecked=requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getBoolean(PREF_NOTIFICATION_STATUS, false)


    }

    override fun onResume() {
        super.onResume()
        doneButton.visibility = View.GONE
        init()
        initCtrl()

    }

    override fun initCtrl() {
        passwordTextView.setOnClickListener(this)
        logoutTextView.setOnClickListener(this)
        terminateTextView.setOnClickListener(this)
        camImageView.setOnClickListener(this)
        doneButton.setOnClickListener(this)

        appSoundToggle.setOnCheckedChangeListener(this)
        notificationToggle.setOnCheckedChangeListener(this)

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                doneButton.visibility = View.VISIBLE
                /*if (s.toString().equals(requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(PREF_USER_NAME, ""), false)) {
                    doneButton.visibility = View.GONE
                } else {
                    doneButton.visibility = View.VISIBLE
                }*/
            }
        })


    }

    override fun observer()
    {
        profileViewModel.logoutResponse.observe(requireActivity(), {
            startActivity(Intent(requireActivity(), LoginActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            Utils.clearSharedPref(requireActivity())
        })

        profileViewModel.terminateAccountDataModel.observe(requireActivity(), {
            startActivity(Intent(requireActivity(), LoginActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            Utils.clearSharedPref(requireActivity())
        })

        profileViewModel.changeNotificationStatusApiResponse.observe(requireActivity(), {

        })

        profileViewModel.userUpdateDetailsResponse.observe(requireActivity(), {
            if (it.status.equals(SUCCESS, true)) {
                requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(PREF_USER_NAME, it.data.name).apply()
                Utils.showMessage(requireActivity(), it.message, MSG_TOAST)
                doneButton.visibility = View.GONE
            } else {
                Utils.showMessage(requireActivity(), it.message, MSG_TOAST)
            }

        })


        profileViewModel.updateUserProfileResponse.observe(requireActivity(), {
            if (it.status.equals(SUCCESS, true)) {
                requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(PREF_USER_PROFILE, it.data).apply()
                Glide.with(requireActivity()).load(it.data).circleCrop().into(profileImageView)
            } else {
                //   Utils.showMessage(requireActivity(), it.message, MSG_TOAST)
            }

        })


    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        val body= ChangeNotificationStatusBody(notificationToggle.isChecked, appSoundToggle.isChecked)


        profileViewModel.changeNotificationStatusApi(requireActivity(), Utils.getUserToken(requireActivity()), body)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.passwordTextView) {
            startActivity(Intent(activity, GetResetCodeActivity::class.java)
                    .putExtra("phoneNumber", phoneEditText.text.toString())
                    .putExtra("countryCode", countryCodePicker.selectedCountryCodeWithPlus)
                    .putExtra("fromProfile", true))
        } else if (v.id == R.id.logoutTextView) {
            val dialog=Utils.showMessage(requireActivity(), "Are you sure you want to logout?", MSG_DIALOG, true)
            (dialog?.findViewById<TextView>(R.id.okTextView))?.setOnClickListener{
                Utils.dismissDialog(dialog)
                profileViewModel.logoutApi(requireActivity(), requireActivity().getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE).getString(PREF_USER_TOKEN, ""))
            }
        }else if(v.id==R.id.terminateTextView){
            //val dialog=Utils.showMessage(requireActivity(), "Are you sure you want to terminate your account?", MSG_DIALOG, true)
            val dialog=Utils.showMessage(requireActivity(), "Are you sure you want to terminate your account once you click on OK button it will be deleted permanently?", MSG_DIALOG, true)
            (dialog?.findViewById<TextView>(R.id.okTextView))?.setOnClickListener{
                Utils.dismissDialog(dialog)
                profileViewModel.terminateAccountApi(requireActivity(), Utils.getUserToken(requireActivity()))
            }
        }else if(v.id==R.id.camImageView){
            //deepLinking2()
            checkPermission()
        }else if(v.id==R.id.doneButton){
            profileViewModel.userUpdateDetailsApi(requireActivity(), nameEditText.text.toString())
        }
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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

                var grant = true

                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        grant = false
                        break
                    }
                }

                if (grant) {
                    profileBottomLayout()
                } else {
                    // Utils.Companion.showMessage(LocationSharingActivity.this, "",MSG_SNACK);
                }

            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            try {
                if (data?.getStringExtra("filePath") != null) {
                    val selectedImagePath = data.getStringExtra("filePath")

                    updateImage(selectedImagePath)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Utils.showMessage(requireActivity(), "Something went wrong", MSG_TOAST)
            }
        }
        else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            try {
                val selectedImageUri: Uri? = data?.data
                val selectedImagePath: String
                selectedImagePath = GetPath.getPath(requireActivity(), selectedImageUri)

                updateImage(selectedImagePath)
            } catch (e: Exception) {
                e.printStackTrace()
                Utils.showMessage(requireActivity(), "Something went wrong", MSG_TOAST)
            }
        }
        else if(resultCode== RESULT_OK && requestCode == CROP_IMAGE_REQ_CODE){
            val imgFile = File(data!!.getStringExtra("url")!!)
            val imagesBody = Compressor(requireActivity()).compressToFile(imgFile).asRequestBody("image/*".toMediaTypeOrNull())
            val imgpart = MultipartBody.Part.createFormData("profilePic", imgFile.name, imagesBody)

            Glide.with(requireActivity()).load(imgFile).circleCrop().into(profileImageView)

            profileViewModel.userUpdateProfileApi(requireActivity(), Utils.getUserToken(requireActivity()), imgpart)

        }
        else if(resultCode== RESULT_OK && requestCode==REQUEST_IMAGE_CAPTURE){

            updateImage(currentPhotoPath)
        }

    }


    fun updateImage(imagePath: String?){
       /* CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);*/

        val imgFile = File(imagePath)
        val imagesBody = Compressor(requireActivity()).compressToFile(imgFile).asRequestBody("image/*".toMediaTypeOrNull())
        val imgpart = MultipartBody.Part.createFormData("profilePic", imgFile.name, imagesBody)

        Glide.with(requireActivity()).load(imgFile).circleCrop().into(profileImageView)

        profileViewModel.userUpdateProfileApi(requireActivity(), Utils.getUserToken(requireActivity()), imgpart)

        //startActivityForResult(Intent(requireActivity(), CropActivity::class.java).putExtra("uri", imagePath), CROP_IMAGE_REQ_CODE)


    }






    private fun profileBottomLayout() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
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
           /* val intent = Intent(requireActivity(), TakeImage::class.java)
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

    private fun deepLinking2() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "")
      //  val shareLing = "https://excursity.page.link/?link=https://excursity.com/" +"21"+ "&apn=com.excursity.excursity&isi=1478618942&ibi=com.Excursity.app"
       // val shareLing = "https://youpick.page.link/?link=https://play.google.com/store/apps/details?id%3Dcom.youpic/"+"21"+"&apn=com.youpic"
      //  val shareLing = "https://youpick.page.link/?link=https://youpick.com"+"/21"
        val shareLing = "https://youpick.page.link/29hQ"+"/21"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareLing)
        startActivity(Intent.createChooser(sharingIntent, "Share using"))
    }

    private fun cameraIntent(){

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
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
                            requireActivity(),
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
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents

         currentPhotoPath = image.absolutePath
        return image
    }


}