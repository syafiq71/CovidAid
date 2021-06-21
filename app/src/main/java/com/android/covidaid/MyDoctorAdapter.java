package com.android.covidaid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyDoctorAdapter extends RecyclerView.Adapter<MyDoctorAdapter.MyViewHolder> {

    Context context;
    List<Doctor> doctorList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyDoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        cardViewList = new ArrayList<>();
        localBroadcastManager= LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_doctor,parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.txt_doctor_name.setText(doctorList.get(i).getName());
        holder.ratingBar.setRating((float)doctorList.get(i).getRating());

        if (!cardViewList.contains(holder.card_doctor))
            cardViewList.add(holder.card_doctor);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for (CardView cardView : cardViewList){
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                }

                holder.card_doctor.setCardBackgroundColor(context.getResources().getColor(R.color.orange_200));

                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_DOCTOR_SELECTED,doctorList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);
            };
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_doctor_name;
        RatingBar ratingBar;
        CardView card_doctor;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_doctor = (CardView) itemView.findViewById(R.id.card_doctor);
            txt_doctor_name = (TextView) itemView.findViewById(R.id.txt_doctor_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rtb_doctor);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}
