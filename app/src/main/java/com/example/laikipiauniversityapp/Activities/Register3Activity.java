package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ScrollView;

import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityRegister3Binding;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class Register3Activity extends AppCompatActivity {
    ScrollView scrollView;
    ActivityRegister3Binding binding;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;
    String gender,email,password,username,date,fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegister3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        scrollView = findViewById(R.id.signup_3rd_screen_scroll_view);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.signup_phone_number);

        fullName = getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        date = getIntent().getStringExtra("date");
        gender = getIntent().getStringExtra("gender");
        binding.signupNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber()) {
                    return;
                }
                String _getCountryCode = countryCodePicker.getSelectedCountryCode().toString().trim();
                String _getUserEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim();
                //Remove first zero if entered!
                if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
                    _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
                }
                //Complete phone number
                final String phoneNo = "+"+_getCountryCode+_getUserEnteredPhoneNumber;
                Intent i = new Intent(getApplicationContext(), OTPActivity.class);

                //Pass all fields to the next activity
                i.putExtra("fullName", fullName);
                i.putExtra("email", email);
                i.putExtra("username", username);
                i.putExtra("password", password);
                i.putExtra("date", date);
                i.putExtra("gender", gender);
                i.putExtra("phoneNo", phoneNo);
                startActivity(i);
            }

        });
    }
    private boolean validatePhoneNumber () {
        String val = phoneNumber.getEditText().getText().toString().trim();
        String checkspaces = "Aw{1,20}z";
        if (val.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        }  else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }

    }
}
