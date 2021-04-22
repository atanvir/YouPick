package com.youpic.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.youpic.R;
import com.youpic.databinding.ActivityDoneBinding;

public class DoneActivity extends AppCompatActivity
{

    private ActivityDoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_done);
        findViewById(R.id.backTextView).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.titleTextView)).setText("Done!");

        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoneActivity.this,NotiScreenActivity.class).putExtra("fromDone",true));
            }
        });


    }
}