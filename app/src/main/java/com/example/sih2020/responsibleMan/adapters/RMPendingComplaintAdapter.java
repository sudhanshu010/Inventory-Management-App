package com.example.sih2020.responsibleMan.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih2020.R;
import com.example.sih2020.model.Complaint;

import java.util.List;

public class RMPendingComplaintAdapter extends  RecyclerView.Adapter<RMPendingComplaintAdapter.MyHolder1>{

    Context c;
    List<Complaint> x ;

    public RMPendingComplaintAdapter(Context c, List<Complaint> x)                                               //Enter the type of data in the space for model
    {
        this.c = c;
        this.x = x;
    }

    @NonNull
    @Override
    public RMPendingComplaintAdapter.MyHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_pending_complaint_item,null);
        return new RMPendingComplaintAdapter.MyHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RMPendingComplaintAdapter.MyHolder1 myholder1, int position) {


        myholder1.pendingComplaintDate.setText("11/01/2020");
        myholder1.pendingComplaintDescription.setText(x.get(position).getComplaintDescription());
        myholder1.pendingComplaintServicemanName.setText(x.get(position).getComplaintAllocatedTo());
        myholder1.pendingComplaintId.setText("1234");

        Log.i("asdf","fgh");


    }

    @Override
    public int getItemCount() {
        return x.size();                                                                                   // Return item count from firebase
    }


    class MyHolder1 extends RecyclerView.ViewHolder{

        TextView pendingComplaintDate, pendingComplaintId, pendingComplaintServicemanName, pendingComplaintDescription;

        public MyHolder1(@NonNull View itemView) {
            super(itemView);

            pendingComplaintDate = itemView.findViewById(R.id.rm_pending_complaint_date);
            pendingComplaintId = itemView.findViewById(R.id.rm_pending_complaint_id);
            pendingComplaintDescription = itemView.findViewById(R.id.rm_pending_complaint_desc);
            pendingComplaintServicemanName = itemView.findViewById(R.id.rm_pending_complaint_serviceman);

        }
    }

}