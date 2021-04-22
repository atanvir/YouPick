package com.youpic.screens.takeImageOptionActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.youpic.R
import com.youpic.baseClass.BaseActivity
import com.youpic.screens.TakeImageActivity
import com.youpic.screens.homeActivity.HomeActivity
import com.youpic.utils.Constants.Companion.MSG_TOAST
import com.youpic.utils.Constants.Companion.SUCCESS
import com.youpic.utils.Utils
import kotlinx.android.synthetic.main.activity_take_image_option.*

class TakeImageOptionActivity : BaseActivity() {

    lateinit var viewModel: TakeImageOptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_image_option)

        init()
        initControl()
        myObserver()

    }


    override fun init() {
        viewModel=ViewModelProvider(this).get(TakeImageOptionViewModel::class.java)

        viewModel.getRestaurantDataApi(this,intent.getStringExtra("restroId")!!)
    }

    override fun initControl() {

        yesButton.setOnClickListener {
            startActivity(Intent(this,TakeImageActivity::class.java).putExtras(intent.extras!!))
        }

        noButton.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }

    }

    override fun myObserver() {
        viewModel.getRestaurantDataResponse.observe(this,{

            if(it.status==SUCCESS){
                topLayout.visibility= View.VISIBLE
                nameTextView.text="Would you like to add a photo while you're at ${it.data.name}?"
            }else{
                Utils.showMessage(this,it.message, MSG_TOAST)
            }
        })
    }


}