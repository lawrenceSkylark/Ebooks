package com.example.laikipiauniversityapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityRegister2Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Register2Activity extends AppCompatActivity {
    private static final String TAG = "Add module";
    private ActivityRegister2Binding binding;
    String email,password,username,fullName;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    FirebaseAuth firebaseAuth;
    private ArrayList<String> categoryTitleArrayList,categoryIdArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegister2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        loadPdfCategories();

        fullName =getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        datePicker= findViewById(R.id.age_picker);
        radioGroup= findViewById(R.id.radio_group);

     binding.categoryTv.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             categoryPickingDialog();
         }
     });

        binding.signupNextButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                if (!validateAge()|!validateGender()){
                    return;

                }
                selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
                String _gender =selectedGender.getText().toString().trim();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                String _date  = day+"/"+month+"/"+year;
                Intent intent = new Intent(getApplicationContext(), Register3Activity.class);
                intent.putExtra("password",password);
                intent.putExtra("email",email);
                intent.putExtra("username",username);
                intent.putExtra("course", ""+selectedCategoryTitle);
                intent.putExtra("fullName", fullName);
                intent.putExtra("gender", _gender);
                intent.putExtra("date", _date);
                startActivity(intent);
            }
        });
    }

    private void loadPdfCategories() {
        Log.d (TAG, "loadPdfCategories: loading Categories..");
        categoryTitleArrayList = new ArrayList<>();
        categoryIdArrayList = new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Categories");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArrayList.clear();
                categoryIdArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){

                    String categoryId = "" +ds.child("id").getValue();
                    String  categoryTitle = ""+ds.child("category").getValue();

//                    add to respective array list
                    categoryIdArrayList.add(categoryId);
                    categoryTitleArrayList.add(categoryTitle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private  String selectedCategoryId,selectedCategoryTitle;
    private void categoryPickingDialog() {
        Log.d(TAG, "categoryPickingDialog: showing categories..");
        String [] categoriesArray = new String[categoryTitleArrayList.size()];
        for (int i = 0; i< categoryTitleArrayList.size(); i++){
            categoriesArray[i] = categoryTitleArrayList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectedCategoryTitle= categoryTitleArrayList.get(which);
                        selectedCategoryId= categoryIdArrayList.get(which);

                        binding.categoryTv.setText(selectedCategoryTitle);
                        Log.d(TAG, "onClick: selected category"+selectedCategoryTitle+""+selectedCategoryTitle);

                    }
                }).show();

    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 14) {
            Toast.makeText(this, "You are not eligible to apply", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

}