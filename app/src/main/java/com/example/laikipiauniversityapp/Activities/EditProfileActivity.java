package com.example.laikipiauniversityapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityEditProfileBinding;



import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private  static  final String TAG = "PROFILE_EDIT_TAG";
    private Uri imageUri= null;
    private ProgressDialog progressDialog;
    private  String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        loadUserInfo();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAttachMenu();
            }
        });
        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }
    private  void validateData(){
        name = binding.nameEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Input Name ...", Toast.LENGTH_SHORT).show();

        }
        else {
            if (imageUri== null){
                updateProfile(" ");

            }
            else {
                uploadImage();

            }
        }

    }
    private  void updateProfile( String imageUrl){
        Log.d(TAG, "updateProfile: updating user profile");
        progressDialog.setMessage("updating user profile");
        progressDialog.show();

        HashMap<String ,Object> hashMap = new HashMap<>();
        hashMap.put("name",""+name);
        if (imageUri !=null)
        {
            hashMap.put("profileImage", ""+imageUrl);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: profile updated successfully");
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "profile Updated", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to update db due to "+e.getMessage());
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, "failed to update db due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private  void uploadImage(){
        Log.d(TAG, "uploadImage: uploading profile Image.... ");
        progressDialog.setMessage("updating profile image");
        progressDialog.show();

        String  filePathAndName = "profileImages/"+firebaseAuth.getUid();

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: profile Uploaded");
                Log.d(TAG, "onSuccess: getting url of uploaded image");
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                String UploadedImageUrl = ""+uriTask.getResult();
                Log.d(TAG, "onSuccess: uploaded image url : "+UploadedImageUrl);
                updateProfile(UploadedImageUrl);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to upload image due to "+e.getMessage());
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, "failed to upload image due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showImageAttachMenu(){
        PopupMenu popupMenu = new PopupMenu(this,binding.profileIv);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Camera");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Gallery");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int which = item.getItemId();
                if (which==0)
                {
                    pickImageCamera();

                }
                else if (which==1){
                    pickImageGallery();
                }
                return false;
            }
        });

    }
    private  void pickImageCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Sample Image Descriptions");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        cameraActivityResultLauncher.launch(intent);

    }
    private  void pickImageGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultsLauncher.launch(intent);

    }
    private ActivityResultLauncher <Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // get uri for image
                    if (result.getResultCode()== Activity.RESULT_OK){
                        Log.d(TAG, "onActivityResult: picked from Camera  "+imageUri);
                        Intent data  = result.getData();
                        binding.profileIv.setImageURI(imageUri);
                    }
                    else {
                        Toast.makeText(EditProfileActivity.this, "Cancelled ", Toast.LENGTH_SHORT).show();

                    }

                }
            }
    );
    private ActivityResultLauncher<Intent> galleryActivityResultsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //used to handle results for gallery intent
                    if (result.getResultCode()== Activity.RESULT_OK){
                        Log.d(TAG, "onActivityResult: "+imageUri);
                        Intent data  = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: picked fro  Gallery"+imageUri);
                        binding.profileIv.setImageURI(imageUri);
                    }
                    else {
                        Toast.makeText(EditProfileActivity.this, "Cancelled ", Toast.LENGTH_SHORT).show();

                    }
                }
            }
    );
    private  void loadUserInfo(){
        Log.d(TAG, "loadUserInfo: loading user Information ");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = ""+snapshot.child("name").getValue();
                        String  profileImage = ""+snapshot.child("profileImage").getValue();
                        binding.nameEt.setText(name);

                        Glide.with(EditProfileActivity.this)
                                .load(profileImage)
                                .placeholder(R.drawable.ic_person)
                                .into(binding.profileIv);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}