package com.android.covidaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class statusAdapter extends FirebaseRecyclerAdapter<UserHelperClass,statusAdapter.statusViewHolder> {

    public statusAdapter(@NonNull FirebaseRecyclerOptions<UserHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull statusViewHolder holder, int position, @NonNull UserHelperClass model) {
        holder.name.setText(model.getFullName());
        holder.ic.setText(model.getIcNo());
        holder.phone.setText(model.getPhoneNo());
        holder.address.setText(model.getUserAddress());
        holder.aid.setText(model.getUserAid());
    }

    @NonNull
    @Override
    public statusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlestatus,parent,false);

        return new statusViewHolder(view);

    }

    class statusViewHolder extends RecyclerView.ViewHolder{
        TextView name, ic, address, aid, phone;

        public statusViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.namaPemohon);
            ic = (TextView)itemView.findViewById(R.id.ICPemohon);
            address = (TextView)itemView.findViewById(R.id.alamatPemohon);
            aid = (TextView)itemView.findViewById(R.id.sebabPemohon);
            phone = (TextView)itemView.findViewById(R.id.phonePemohon);
        }
    }
}
