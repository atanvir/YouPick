package com.youpic.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.youpic.R


object CustomProgressBar{

    var dialog:Dialog?=null
    var cancelable=false



    fun getCustomDialog(context: Context):Dialog{

        if(dialog!=null && dialog!!.isShowing){
            return dialog!!
        }else{

            dialog = Dialog(context, android.R.style.Theme_Black)

            val view: View = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)

            // LottieAnimationView lottieAnimationView=view.findViewById(R.id.lottie_animation_view);

            //lottieAnimationView.setAnimation("reloader.json");
            //  lottieAnimationView.playAnimation();


            // LottieAnimationView lottieAnimationView=view.findViewById(R.id.lottie_animation_view);

            //lottieAnimationView.setAnimation("reloader.json");
            //  lottieAnimationView.playAnimation();
            dialog!!.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setBackgroundDrawableResource(R.color.transp)
            dialog!!.setContentView(view)
            dialog!!.setCancelable(false)
            dialog!!.show()

        }



        return dialog!!
    }


    fun dismissDialog(){
        if(dialog!=null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

}