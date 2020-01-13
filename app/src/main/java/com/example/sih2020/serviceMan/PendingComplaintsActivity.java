package com.example.sih2020.serviceMan;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih2020.R;
import com.example.sih2020.model.*;
import com.example.sih2020.serviceMan.adapters.PendingComplaintAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingComplaintsActivity extends AppCompatActivity {

    RecyclerView recyclerView_complaints;
    PendingComplaintAdapter myPendingComplaintAdapter;

    List<String> pendingComplaintList;

    List<Complaint> pendingComplaintObjectList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference serviceManReference, pendingComplaintListReference, complaintReference;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complaints);

        recyclerView_complaints = findViewById(R.id.recyclerView_complaints);
        recyclerView_complaints.setLayoutManager(new LinearLayoutManager(this));



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        pendingComplaintList = new ArrayList<String>();
        pendingComplaintObjectList = new ArrayList<Complaint>();

        myPendingComplaintAdapter = new PendingComplaintAdapter(getApplicationContext(),pendingComplaintObjectList);
        recyclerView_complaints.setAdapter(myPendingComplaintAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        serviceManReference = firebaseDatabase.getReference("Users").child("ServiceMan").child(user.getUid());
        pendingComplaintListReference = serviceManReference.child("pendingComplaintList");
        complaintReference = firebaseDatabase.getReference("Complaints");



        pendingComplaintListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getValue().toString();

                complaintReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Complaint complaint = new Complaint();
                        complaint = dataSnapshot.getValue(Complaint.class);
                        pendingComplaintObjectList.add(complaint);
                        myPendingComplaintAdapter.notifyDataSetChanged();
                        Log.i("machine id", complaint.getComplaintMachineId());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}
