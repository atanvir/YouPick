package com.youpic.screens.cropActivity

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.youpic.R
import kotlinx.android.synthetic.main.activity_crop.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class CropActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        }

        /*crop_view.setBitmap(BitmapFactory.decodeFile(intent.getStringExtra("uri")))


        crop_view.addOnCropListener(object : OnCropListener {
            override fun onSuccess(bitmap: Bitmap) {

                var path=saveImage(bitmap,"YouPick")
                val intent = Intent()
                intent.putExtra("url", path)
                setResult(RESULT_OK,intent)
                finish()

            }

            override fun onFailure(e: Exception) {
                e.printStackTrace()
                finish()
            }
        })

        okTextView.setOnClickListener {
            crop_view.crop()
        }

        cancelTextView.setOnClickListener {
            finish()
        }
*/
    }

    fun saveImage(bitmap: Bitmap,name:String):String{
        var saved:Boolean
        var fos:OutputStream
        var imagePath=""

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            var resolver=contentResolver
            var contentValue=ContentValues()
            contentValue.put(MediaStore.MediaColumns.DISPLAY_NAME,name)
            contentValue.put(MediaStore.MediaColumns.MIME_TYPE,"image/png")
            contentValue.put(MediaStore.MediaColumns.RELATIVE_PATH,"DCIM/Youpic")

            val imageUri=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValue)
            fos=resolver.openOutputStream(imageUri!!)!!
            imagePath=imageUri.path!!
        }else{

            val imageDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+File.separator+"Youpic"
            val file=File(imageDir)

            if(!file.exists())
                file.mkdir()
            var image=File(imageDir,name+".png")
            fos=FileOutputStream(image)

            imagePath=file.absolutePath

        }
        saved=bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
        fos.flush()
        fos.close()

        return imagePath




    }

}