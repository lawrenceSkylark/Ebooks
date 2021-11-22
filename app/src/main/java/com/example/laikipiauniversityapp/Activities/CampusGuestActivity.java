package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityCampusGuestBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CampusGuestActivity extends AppCompatActivity {
    private ActivityCampusGuestBinding binding;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCampusGuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkUser();
        firebaseAuth = FirebaseAuth.getInstance();


        binding.evntsRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CampusGuestActivity.this,UpcomingEventsActivity.class));
            }
        });
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        binding.notificationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth= FirebaseAuth.getInstance();
                if (firebaseAuth.getCurrentUser()!=null){
                    startActivity (new Intent(CampusGuestActivity.this,AnnouncementsActivity.class));
                }
                else
                {
                    Toast.makeText(CampusGuestActivity.this, "logged in", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.eventstx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CampusGuestActivity.this,UpcomingEventsActivity.class));
            }
        });
    }

    private void checkUser() {

    }
}