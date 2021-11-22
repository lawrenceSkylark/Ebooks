package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.laikipiauniversityapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    // decleare wars

    private ActivityRegisterBinding binding;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        init firebase
        firebaseAuth = FirebaseAuth.getInstance();
//        set up progDilog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please Wait ....");
        progressDialog.setCanceledOnTouchOutside(false);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        binding.imgPick.setOnClickListener(view -> {



        });
    }
    private void pickImage() {
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }
    private  String name = "", email ="",fullName ="",password ="";
    //    before creating user lets validate data
    private void validateData() {
//         lets get data
        fullName = binding.fullNameEt.getText().toString().trim();
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password =binding.passwordEt.getText().toString().trim();
        String checkSpaces = "\\A\\w{4,20}\\z";
//    lets validate
        if (TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "enter your Name full name", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "enter your Name username", Toast.LENGTH_SHORT).show();

        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid mail monkey", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "please enter the passsword", Toast.LENGTH_SHORT).show();
        }

        else if (!password.equals(password)){
            Toast.makeText(this, "passwords don't Match", Toast.LENGTH_SHORT).show();

        }
        else
            binding.registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Register2Activity.class);
                    intent.putExtra("password",password);
                    intent.putExtra("email",email);
                    intent.putExtra("username",name);
                    intent.putExtra("fullName", fullName);
                    startActivity(intent);
                }
            });


    }




}