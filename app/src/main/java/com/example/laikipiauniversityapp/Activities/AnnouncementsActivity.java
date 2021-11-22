package com.example.laikipiauniversityapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laikipiauniversityapp.Adapter.AdapterEventList;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.databinding.ActivityAnnouncementsBinding;
import com.example.laikipiauniversityapp.models.ModelEventList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class AnnouncementsActivity extends AppCompatActivity {

    private RecyclerView groupsRs;
    private FirebaseAuth firebaseAuth;
    private ActivityAnnouncementsBinding binding;

    private ArrayList<ModelEventList> groupChatlists;
    private AdapterEventList adapterGroupChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnnouncementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        loadGRoupChatList();
        checkUser();

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // get current user
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
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
                                        startActivity(new Intent(AnnouncementsActivity.this,DashboardUserActivity.class));

                                    } else if (userType.equals("admin")) {
                                        startActivity(new Intent(AnnouncementsActivity.this,DashboardAdminActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

        });

        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnnouncementsActivity.this, AddAnnouncementsActivity.class));
            }
        });
    }

    private void checkUser() {
        // get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // get current user
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
                            binding.btnCreate.setVisibility(View.GONE);

                        } else if (userType.equals("admin")) {
                            binding.btnCreate.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadGRoupChatList() {
        groupChatlists = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatlists.size();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // if current user"s uid xist in participants list of groups thenshow that grp
                    ModelEventList model = ds.getValue(ModelEventList.class);
                        groupChatlists.add(model);
                }
                Collections.sort(groupChatlists, new Comparator<ModelEventList>() {
                    @Override
                    public int compare(ModelEventList o1, ModelEventList o2) {
                        return o1.eventId.compareToIgnoreCase(o2.eventId);
                    }
                });
                 Collections.reverse(groupChatlists);
                adapterGroupChatList = new AdapterEventList(getApplicationContext(), groupChatlists);
                binding.groupsRs.setAdapter(adapterGroupChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private long pressedTime;
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}