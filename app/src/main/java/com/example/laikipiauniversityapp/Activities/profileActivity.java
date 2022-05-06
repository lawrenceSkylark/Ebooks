package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.NonNull;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import com.example.laikipiauniversityapp.Adapter.AdapterPdfFavorite;
import com.example.laikipiauniversityapp.models.ModelPdf;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private  String categoryId, categoryTitle;

    private AdapterPdfFavorite adapterPdfFavorite;
    private ArrayList <ModelPdf> pdfArrayList;
    private  static  final String TAG = "PROFILE_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();
        loadFavoriteBooks();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            binding.editProfile.setVisibility(View.GONE);
            binding.titleTv.setText("click here to Access your profile");
            binding.titleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(profileActivity.this,LoginActivity.class));
                }
            });
        }
        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(profileActivity.this,EditProfileActivity.class));
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    startActivity(new Intent(profileActivity.this,DashboardUserActivity.class));
                } else {
                // chck in db
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // get user type
                                String userType = "" + snapshot.child("userType").getValue();
                                //  check user
                                if (userType.equals("user")) {
                                    startActivity(new Intent(profileActivity.this,DashboardUserActivity.class));

                                } else if (userType.equals("admin")) {
                                    startActivity(new Intent(profileActivity.this,DashboardAdminActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
            }
        });

    }
    private  void loadFavoriteBooks() {
        pdfArrayList = new ArrayList<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            binding.emailTv.setText("Not Logged in");
        } else {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.keepSynced(true);
        ref.child(firebaseAuth.getUid()).child("Favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String bookId = "" + ds.child("bookId").getValue();
                            ModelPdf model = new ModelPdf();
                            model.setId(bookId);
                            pdfArrayList.add(model);
                        }
                        binding.favoriteBookCountTv.setText("" + pdfArrayList.size());
                        adapterPdfFavorite = new AdapterPdfFavorite(profileActivity.this, pdfArrayList);
                        binding.booksRv.setAdapter(adapterPdfFavorite);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    }
    private  void loadUserInfo(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            binding.emailTv.setText("Not Logged in");
        } else {
            Log.d(TAG, "loadUserInfo: loading user Information ");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.keepSynced(true);
            reference.child(firebaseAuth.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String email = "" + snapshot.child("email").getValue();
                            String phoneNo = "" + snapshot.child("phoneNo").getValue();
                            String name = "" + snapshot.child("name").getValue();
                            String userType = "" + snapshot.child("userType").getValue();
                            String profileImage = "" + snapshot.child("profileImage").getValue();
                            String timestamp = "" + snapshot.child("timestamp").getValue();
                            String course = " "+ snapshot.child("Course").getValue();

                            String formattedDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                            binding.memberDateTv.setText(formattedDate);
                            binding.phoneNumber.setText(phoneNo);
                            binding.emailTv.setText(email);
                            binding.courseTv.setText(course);
                            binding.nameTv.setText(name);
                            binding.accountTypeTv.setText(userType);


                            Glide.with(getApplicationContext())
                                    .load(profileImage)
                                    .placeholder(R.drawable.ic_person)
                                    .into(binding.profileIv);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }   }
}