package com.example.laikipiauniversityapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityAddAnnouncementsBinding;
import com.example.laikipiauniversityapp.materialdaterangepicker.date.DatePickerDialog;
import com.example.laikipiauniversityapp.materialdaterangepicker.time.RadialPickerLayout;
import com.example.laikipiauniversityapp.materialdaterangepicker.time.TimePickerDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AddAnnouncementsActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ActivityAddAnnouncementsBinding binding;
    //  permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private FirebaseAuth firebaseAuth;
    //
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    //permision array
    private String[] cameraPermissions;
    private String[] storagePermissions;
    //picked image permsiion
    private Uri image_uri = null;
    Toolbar toolbar;

    private ProgressDialog progressDialog;
    private boolean mAutoHighlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddAnnouncementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init array permiss
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        firebaseAuth = FirebaseAuth.getInstance();
        CheckUser();
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //handle click event
        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateChannel();

            }
        });

        // pick image
        binding.groupicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        CheckBox ahl = (CheckBox) findViewById(R.id.autohighlight_checkbox);
        ahl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAutoHighlight = b;
            }
        });
        // Show a datepicker when the dateButton is clicked
        binding.dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.example.laikipiauniversityapp.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        AddAnnouncementsActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAutoHighlight(mAutoHighlight);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        binding.timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddAnnouncementsActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }


    private void startCreateChannel() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("creating Announcement");
        //input descriptions
        String eventTime =binding.timeTextview.getText().toString().trim();
        String eventHost=binding.hostTitleet.getText().toString().trim();
        String eventVenue=binding.venueTitleet.getText().toString().trim();
        String eventDate=binding.dateTextview.getText().toString().trim();
        String eventDescription = binding.EventDescriptionet.getText().toString().trim();
        String eventTitle = binding.GroupTitleet.getText().toString().trim();
        if (TextUtils.isEmpty(eventDescription)) {
            Toast.makeText(getApplicationContext(), "add event name!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();

        //timestamp,grouo id,group image ,time created;
        String g_timestamp = "" + System.currentTimeMillis();
        if (image_uri == null) {
            //creating challennel without icon
            createChannel(
                    "" + g_timestamp,
                    ""+eventDate,
                    ""+ eventHost,
                    "" + eventVenue,
                    ""+ eventTitle,

                    "" + eventDescription,

                    "" ,
                    "" + eventTime
            );

        } else {
            //greating channel with icon
            //upload image
            //icon image and path
            String fileNameAndPath = "Event_imgs/" + "image" + g_timestamp;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(fileNameAndPath);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        //image uploaded,get url
                        {
                            Task<Uri> p_UriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!p_UriTask.isSuccessful()) ;
                            Uri p_downloadUri = p_UriTask.getResult();
                            if (p_UriTask.isSuccessful()) {
                                createChannel(
                                        "" + g_timestamp,
                                        ""+eventDate,
                                        "" + eventHost,
                                        ""+ eventVenue,



                                        ""+ eventTitle,
                                        "" + eventDescription,
                                        "" ,""+eventTime
                                                + p_downloadUri
                                );
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

    }

    private void createChannel(String g_timestamp,String eventDate, String eventHost,String eventVenue,String eventTitle, String eventDescription, String eventIcon,String eventTime) {
        //setup group info
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("eventId", "" + g_timestamp);
        hashMap.put("eventDate", ""+ eventDate);
        hashMap.put("eventTitle", "" + eventTitle);
        hashMap.put("eventDescription", "" + eventDescription);
        hashMap.put("eventIcon", "" + eventIcon);
        hashMap.put("timestamp", "" + g_timestamp);
        hashMap.put("eventVenue","" + eventVenue);
        hashMap.put("eventHost", ""+ eventHost);
        hashMap.put("EventTime","" +eventTime);

        hashMap.put("CreatedBy", "" + (firebaseAuth.getCurrentUser()).getEmail());
        // create group
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child(g_timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // on success
                //setUp members inf (add current in group participants list)
                HashMap<String, String> hashMap1 = new HashMap<>();
                hashMap1.put("uid", firebaseAuth.getUid());
                hashMap1.put( "role", "creator");
                hashMap1.put("timestamp", g_timestamp);
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Events");
                ref1.child(g_timestamp).child("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // on succful
                                progressDialog.dismiss();
                                Toast.makeText(AddAnnouncementsActivity.this, "event has been created successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), AnnouncementsActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding participnts
                        progressDialog.dismiss();
                        Toast.makeText(AddAnnouncementsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed
                Toast.makeText(AddAnnouncementsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void showImagePickDialog() {
        // options to pick image
        String[] options = {"CAMERA", "GALLERY"};

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("pick image:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle clicks
                        if (which == 0) {
                            // camera click
                            if (!checkCameraPermissions()) {
                                requestCameraPermissions();
                            } else {
                                pickFromCamera();
                            }

                        } else {
                            //gallery click
                        }
                        if (!checkStoragePermissions()) {
                            requestStoragePermissions();
                            //gallery click
                        } else {
                            pickFromGallery();
                        }

                    }
                }).show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Event Icon Image Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Event Icon Image Description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissions() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;

    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1 && result;
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //  DISPLAY The current User email on the Subtitle of toolbar
    private void CheckUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
        }
        else {

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        //both or one is denied
                        Toast.makeText(this, "Camera and storage required", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //persmission allowed
                        pickFromGallery();
                    } else {
                        // permissions  denied
                        Toast.makeText(this, " storage permissions required ", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //handle results
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //was picked from gallery
                image_uri = data.getData();
                //set image to imageview
                binding.groupicon.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //was picked from camera
                //set image to imageview
                binding.groupicon.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = " From- "+dayOfMonth+"/"+(++monthOfYear)+"/"+year+" To "+dayOfMonthEnd+"/"+(++monthOfYearEnd)+"/"+yearEnd;
         binding.dateTextview.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String hourStringEnd = hourOfDayEnd < 10 ? "0"+hourOfDayEnd : ""+hourOfDayEnd;
        String minuteStringEnd = minuteEnd < 10 ? "0"+minuteEnd : ""+minuteEnd;
        String time = "From - "+hourString+"h"+minuteString+" To - "+hourStringEnd+"h"+minuteStringEnd;

        binding.timeTextview.setText(time);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}