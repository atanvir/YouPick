package com.youpic.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.youpic.R;
import com.youpic.databinding.ActivityLocationSharingBinding;
import com.youpic.screens.loginActivity.LoginActivity;
import com.youpic.utils.Utils;


public class LocationSharingActivity extends AppCompatActivity
{
    private ActivityLocationSharingBinding binding;
    private final static int PERMISSION_CODE=121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_location_sharing);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        }


        binding.allowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });


    }

    private void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(LocationSharingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(LocationSharingActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            startActivity(new Intent(LocationSharingActivity.this, LoginActivity.class));
        }else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case PERMISSION_CODE:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED&&
                        grantResults[1]==PackageManager.PERMISSION_GRANTED) {

                        startActivity(new Intent(LocationSharingActivity.this,LoginActivity.class));
                    }else
                    {
                       // Utils.Companion.showMessage(LocationSharingActivity.this, "",MSG_SNACK);
                    }
                break;
        }
    }


}