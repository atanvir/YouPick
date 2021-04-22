package com.youpic.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.youpic.R
import com.youpic.databinding.ActivityGoodWorkBinding
import com.youpic.screens.homeActivity.HomeActivity
import kotlinx.android.synthetic.main.activity_good_work.*

class GoodWorkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_good_work)
        doneButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))


            /*startActivity(new Intent(GoodWorkActivity.this,NotiScreenActivity.class)
                        .putExtra("fromDone",getIntent().getBooleanExtra("fromDone",false))
                        .putExtra("locationDetail",getIntent().getBooleanExtra("locationDetail",false))
                        .putExtra("fromInvites",getIntent().getBooleanExtra("fromInvites",false)));*/
        }
    }
}