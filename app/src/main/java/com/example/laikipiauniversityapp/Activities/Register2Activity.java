package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityRegister2Binding;

import java.util.Calendar;

public class Register2Activity extends AppCompatActivity {
    private ActivityRegister2Binding binding;
    String email,password,username,fullName;

    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegister2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fullName =getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        datePicker= findViewById(R.id.age_picker);
        radioGroup= findViewById(R.id.radio_group);



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
                intent.putExtra("fullName", fullName);
                intent.putExtra("gender", _gender);
                intent.putExtra("date", _date);
                startActivity(intent);
            }
        });
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