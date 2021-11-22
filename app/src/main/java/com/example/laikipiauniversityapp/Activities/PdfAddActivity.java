package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.laikipiauniversityapp.databinding.ActivityPdfAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;


public class PdfAddActivity extends AppCompatActivity {

    private ActivityPdfAddBinding binding;

    private Uri pdfUri = null;

    FirebaseAuth firebaseAuth;
    private ArrayList<String> categoryTitleArrayList,categoryIdArrayList;

    private static final String TAG = "ADD_PDF_TAG";
    private  static  final int PDF_PICK_CODE = 1000;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadPdfCategories();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickingDialog();
            }
        });
        binding.attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfPickIntent();
            }
        });
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                validate data
//
                validateData();
            }
        });
    }
    private String description = "", title = "" ;

    private void validateData() {
        Log.d(TAG, "validateData: validating data");

        title = binding.titleEt.getText().toString().trim();
        description= binding.descriptionEt.getText().toString().trim();

        if (TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please enter the description..!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(selectedCategoryTitle)){
            Toast.makeText(this, "Please pick the category", Toast.LENGTH_SHORT).show();
        }
        else if (pdfUri == null){
            Toast.makeText(this, "pick the pdf", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadPdfToStorage();
        }


    }

    private void uploadPdfToStorage() {
        Log.d(TAG, "uploadPdfToStorage: uploading pdf...");

        progressDialog.setMessage("Uploading Pdf");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        String filePathAndName = "Books/" + timestamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: uploaded to storage");
                        Log.d(TAG, "onSuccess: getting  pdf url");
                        Task<Uri> uriTask  = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadPdfUrl = ""+uriTask.getResult();

                        // upload to storgae
                        uploadPdfInfoToDb(uploadPdfUrl,timestamp);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Uploading failed"+e.getMessage());
                Toast.makeText(PdfAddActivity.this, "Uploading Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void uploadPdfInfoToDb(String uploadPdfUrl, long timestamp) {
        progressDialog.dismiss();
        Log.d(TAG, "uploadPdfInfoToDb: uploading pdf info into firebase db");
        progressDialog.setMessage("uploading pdf info..");

        String Uid = firebaseAuth.getUid();

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("Uid", ""+Uid);
        hashMap.put("id",""+timestamp);
        hashMap.put("title", ""+title);
        hashMap.put("description" ,"" +description);
        hashMap.put("categoryId", ""+selectedCategoryId);
        hashMap.put("Url", ""+uploadPdfUrl);
        hashMap.put("timestamp" , timestamp);
        hashMap.put("viewsCount" , 0);
        hashMap.put("downloadsCount" , 0);



        // refence
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onSuccess: Successfully uploaded ...");
                        Toast.makeText(PdfAddActivity.this, "Successfully uploaded ...", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: failed to upload to db");
                Toast.makeText(PdfAddActivity.this, "Failed to Upload"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent: starting pdf Intent");

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select pdf"),PDF_PICK_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PDF_PICK_CODE) {
                Log.d(TAG, "onActivityResult: PDF Picked");

                pdfUri = data.getData();
                Log.d(TAG, "onActivityResult: URI: "+pdfUri);
            }
        }
    }

}