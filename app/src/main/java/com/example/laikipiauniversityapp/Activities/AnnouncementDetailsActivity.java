package com.example.laikipiauniversityapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityAnnouncementDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnnouncementDetailsActivity extends AppCompatActivity {
    private ActivityAnnouncementDetailsBinding binding;
    private  String eventId,eventDate,eventIcon,eventDescription,eventHost,eventTitle,eventVenue,eventTime;
  private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding= ActivityAnnouncementDetailsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        firebaseAuth =FirebaseAuth.getInstance();
         loadEventDetails();
        eventId = getIntent().getStringExtra("eventId");
        eventDate=getIntent().getStringExtra("eventDate");
        eventIcon=getIntent().getStringExtra("eventIcon");
        eventDescription=getIntent().getStringExtra("eventDescription");
        eventHost=getIntent().getStringExtra("eventHost");
        eventTitle=getIntent().getStringExtra("eventTitle");
        eventTime=getIntent().getStringExtra("eventTime");
        eventDate=getIntent().getStringExtra("eventDate");
        eventVenue=getIntent().getStringExtra("eventVenue");

       //setData
    }

    private void loadEventDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
        reference.keepSynced(true);
        reference.child("Events")
                .addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String eventDate = "" + snapshot.child("eventDate").getValue();
                String eventIcon= "" + snapshot.child("eventIcon").getValue();
                String eventHost =""+ snapshot.child("eventHost").getValue();
                String eventVenue=""+snapshot.child("eventVenue").getValue();
                String eventTitle = "" + snapshot.child("eventTitle").getValue();
                String eventDescription = "" + snapshot.child("eventDescription").getValue();
                String eventTime = "" + snapshot.child("timestamp").getValue();
                String timestamp=  ""+ snapshot.child("timestamp").getValue();

                binding.dateTv.setText(eventDate);
                binding.eventTitleTv.setText(eventTitle);
                binding.hostTv.setText(eventHost);
                binding.aboutTv.setText(eventDescription);
                binding.venueTv.setText(eventVenue);
                binding.timeTv.setText(timestamp);
                binding.timeTv.setText(eventTime);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}