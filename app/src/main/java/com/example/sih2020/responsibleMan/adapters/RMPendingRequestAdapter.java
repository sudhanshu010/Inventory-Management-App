package com.example.sih2020.responsibleMan.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih2020.R;
import com.example.sih2020.model.PastRecord;
import com.example.sih2020.model.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RMPendingRequestAdapter extends RecyclerView.Adapter<RMPendingRequestAdapter.MyHolder> {

    Context c;
    public List<Request> x;

    DatabaseReference serviceMan;


    public RMPendingRequestAdapter(Context c, List<Request> x) {
        this.c = c;
        this.x = x;
//        serviceMan = FirebaseDatabase.getInstance().getReference("Users").child("ServiceMan");
    }

    public RMPendingRequestAdapter(Context c) {
        this.c = c;
    }

    public void setx(List<Request> x) {
        this.x = x;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_pending_request_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myholder, int position) {


//        serviceMan.child(x.get(position).getServiceMan()).child("email").addValueEventListener(new ValueEventListener() {
//            String name;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                myholder.serviceman1.setText(dataSnapshot.getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        myholder.serviceman1.setText(x.get(position).getServiceMan());
        myholder.requestid1.setText(x.get(position).getRequestid());
        myholder.complain_id.setText(x.get(position).getComplaintId());
        myholder.description.setText(x.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        Log.i("size",String.valueOf(x.size()));
        return x.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {


        TextView serviceman1,requestid1,complain_id,description,accept_button,decline_button;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            requestid1 = itemView.findViewById(R.id.request_id1);
            serviceman1 = itemView.findViewById(R.id.serviceman1);
            complain_id = itemView.findViewById(R.id.complain_id);
            description = itemView.findViewById(R.id.description);
            accept_button = itemView.findViewById(R.id.accept_button);
            decline_button = itemView.findViewById(R.id.decline_button);


        }


    }





}