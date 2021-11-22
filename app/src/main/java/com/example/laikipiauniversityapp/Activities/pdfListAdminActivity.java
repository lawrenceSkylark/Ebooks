package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;


import com.example.laikipiauniversityapp.Adapter.AdapterPdfAdmin;
import com.example.laikipiauniversityapp.models.ModelPdf;
import com.example.laikipiauniversityapp.databinding.ActivityPdfListAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pdfListAdminActivity extends AppCompatActivity {
    // Arrray list to  hold List of data type ModelPDf

    //    binding
    private ActivityPdfListAdminBinding binding;
    private ArrayList<ModelPdf> pdfArrayList;
    //    adapeters
    private AdapterPdfAdmin adapterPdfAdmin;
    private  String categoryId, categoryTitle;
    private  static  final  String TAG = "PDF_LIST_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // get date from intent
        Intent intent  = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryTitle =intent.getStringExtra("categoryTitle");

        binding.subTitleTv.setText(categoryTitle);


        loadPdfList();
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterPdfAdmin.getFilter().filter(s);

                }
                catch (Exception e){
                    Log.d(TAG, "onTextChanged: "+e.getMessage());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //
    }

    private void loadPdfList() {
        pdfArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelPdf model =ds.getValue(ModelPdf.class);
                            // add to list
                            pdfArrayList.add(model);
                            Log.d(TAG, "onDataChange: "+ model.categoryId +" "+model.getTitle());
                        }
                        /// set up adapeter
                        adapterPdfAdmin = new AdapterPdfAdmin(pdfListAdminActivity.this,pdfArrayList);
                        binding.bookRv.setAdapter(adapterPdfAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}