package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityGroupInfoBinding;

public class GroupInfoActivity extends AppCompatActivity {
 ActivityGroupInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
    }
}