package com.youpic.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.youpic.R;
import com.youpic.adapter.OccasionAdapter;
import com.youpic.databinding.ActivityOccasionBinding;

public class OccasionActivity extends AppCompatActivity {

    private ActivityOccasionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_occasion);


        binding.occasionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.occasionRecyclerView.setAdapter(new OccasionAdapter(this));

        (findViewById(R.id.backTextView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView)findViewById(R.id.titleTextView)).setText("Occasion");


    }
}