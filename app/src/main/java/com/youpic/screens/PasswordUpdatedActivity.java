package com.youpic.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.youpic.R;
import com.youpic.databinding.ActivityPasswordUpdatedBinding;
import com.youpic.screens.homeActivity.HomeActivity;
import com.youpic.screens.loginActivity.LoginActivity;

public class PasswordUpdatedActivity extends AppCompatActivity
{

    private ActivityPasswordUpdatedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_password_updated);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.app_green));
        }

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getBooleanExtra("fromReset",false)){
                    startActivity(new Intent(PasswordUpdatedActivity.this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }else{
                    startActivity(new Intent(PasswordUpdatedActivity.this, HomeActivity.class).putExtra("openProfile",true));
                }
            }
        });
    }
}