package com.example.laikipiauniversityapp.Activities;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


import com.example.laikipiauniversityapp.Adapter.AdapterComment;
import com.example.laikipiauniversityapp.models.ModelComment;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityPdfDetailsBinding;
import com.example.laikipiauniversityapp.databinding.DialogCommentAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfDetailsActivity extends AppCompatActivity {
    private ActivityPdfDetailsBinding binding;

    private ArrayList<ModelComment> commentArrayList;
    private AdapterComment adapterComments;

    public  static  final String TAG_DOWNLOAD="DOWNLOAD_TAG";

    String bookId,bookTitle,bookUrl;

    boolean isInMyFavorite = false;
    private   FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        get id  from intent
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        binding.downloadBookBtn.setVisibility(View.GONE );

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            checkIfFavorite();
        }

        loadBookDetails();
        loadComments();

        MyApplication.incrementBookViewCount(bookId);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.readBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PdfDetailsActivity.this, PdfViewActivity.class);
                intent1.putExtra("bookId",bookId);
                startActivity(intent1);
            }
        });
        binding.downloadBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG_DOWNLOAD, "onClick: checking permission");

                if (ContextCompat.checkSelfPermission(PdfDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG_DOWNLOAD, "onClick: permission already granted can download the book");
                    MyApplication.downloadBook(PdfDetailsActivity.this,""+bookId,
                            ""+bookTitle,
                            ""+bookUrl);
                }
                else
                {
                    Log.d(TAG_DOWNLOAD, "onClick: permission was not granted request permission...");
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

            }
        });
        binding.addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser()==null){
                    Toast.makeText(PdfDetailsActivity.this, "you're not logged in", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    addCommentDialog();
                }
            }
        });

        binding.favoriteBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser()==null){
                    Toast.makeText(PdfDetailsActivity.this, "you're not logged in", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if (isInMyFavorite){
                        MyApplication.removeFromFavorite(PdfDetailsActivity.this,bookId);

                    }
                    else {
                        MyApplication.addFavorite(PdfDetailsActivity.this,bookId);

                    }
                }
            }
        });

    }

    private void loadComments() {
        commentArrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
        reference.child("bookId").child("Comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentArrayList.clear();
                        for (DataSnapshot ds :snapshot.getChildren()){
                            ModelComment model= ds.getValue(ModelComment.class);
                            commentArrayList.add(model);

                        }
                        binding.commentsRv.setAdapter(adapterComments);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private String comment ="null";
    private void addCommentDialog(){
        DialogCommentAddBinding commentAddBinding  = DialogCommentAddBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setView(commentAddBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        commentAddBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        commentAddBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentAddBinding.commentEt.getText().toString().trim();
                if (TextUtils.isEmpty(comment)){
                    Toast.makeText(PdfDetailsActivity.this, "add comment", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    alertDialog.dismiss();
                    addComment();
                }

            }
        });

    }
    private void addComment(){
        progressDialog.setMessage("Adding Comment");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("bookId",""+bookId);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("Comments", ""+comment);
        hashMap.put("uid",""+firebaseAuth.getUid());


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");

        ref.child(bookId).child("Comments")
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PdfDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PdfDetailsActivity.this, "Failed to add comment due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }

    private ActivityResultLauncher <String>  requestPermissionLauncher=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->
            {
                if (isGranted){

                    Log.d(TAG_DOWNLOAD, "Permission Granted ");

                    MyApplication.downloadBook(PdfDetailsActivity.this,""+bookId,""+bookTitle,""+bookUrl);
                }
                else {
                    Log.d(TAG_DOWNLOAD, "Permission was denied: ");
                    Toast.makeText(this, "permission was denied", Toast.LENGTH_SHORT).show();

                }
            });

    private void loadBookDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books").child(bookId);
        reference.keepSynced(true);
        reference.child(bookId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        get data
                bookTitle = "" + snapshot.child("title").getValue();
                String categoryId = "" + snapshot.child("categoryId").getValue();
                String description = "" + snapshot.child("description").getValue();
                String downloadsCount = "" + snapshot.child("downloadsCount").getValue();
                String viewsCount = "" + snapshot.child("viewsCount").getValue();
                String bookUrl = "" + snapshot.child("Url").getValue();
                String timestamp = "" + snapshot.child("timestamp").getValue();

                binding.downloadBookBtn.setVisibility(View.VISIBLE);


                MyApplication.loadCategory(
                        "" + categoryId,
                        binding.categoryTv);
                MyApplication.loadPdfFromUrlSinglePage(
                        ""+bookUrl,
                        ""+bookTitle,
                        binding.pdfView,
                        binding.progressBar);
                MyApplication.loadPdfSize(
                        "" + bookUrl,
                        "" + bookTitle,
                        binding.sizeTv);


//                        set data
                binding.titleTv.setText(bookTitle);

                binding.dateTv.setText(timestamp);
                binding.categoryTv.setText(categoryId);
                binding.descriptionTv.setText(description);
                binding.downloadsTv.setText(downloadsCount.replace("null", "N/A"));
                binding.viewsTv.setText(viewsCount.replace("null", "N/A"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void   checkIfFavorite(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.keepSynced(true);
        reference.child(firebaseAuth.getUid()).child("Favorites").child(bookId)
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInMyFavorite = snapshot.exists();
                        if (isInMyFavorite){
                            binding.favoriteBookBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite_white,0,0);
                            binding.favoriteBookBtn.setText("Remove Favorite ");
                        }
                        else {
                            binding.favoriteBookBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite,0,0);
                            binding.favoriteBookBtn.setText("Add Favorite ");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}