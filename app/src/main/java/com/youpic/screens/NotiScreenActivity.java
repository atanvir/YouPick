package com.youpic.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.youpic.R;
import com.youpic.screens.invitedActivity.InvitedActivity;
import com.youpic.screens.whatActivity.WhatActivity;

public class NotiScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_screen);

        if(getIntent().getBooleanExtra("fromDone",false)){
            ((ImageView)findViewById(R.id.imageView27)).setImageResource(R.drawable.final_call);
        }

        findViewById(R.id.imageView27).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getBooleanExtra("fromDone",false)){
                    startActivity(new Intent(NotiScreenActivity.this, WhatActivity.class)
                            .putExtra("fromDone",getIntent().getBooleanExtra("fromDone",false)));
                }else{
                    startActivity(new Intent(NotiScreenActivity.this, InvitedActivity.class)
                            .putExtra("locationDetail",getIntent().getBooleanExtra("locationDetail",false))
                            .putExtra("fromInvites",true));
                }

            }
        });
    }
}