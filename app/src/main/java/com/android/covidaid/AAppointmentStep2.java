package com.android.covidaid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AAppointmentStep2 extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_doctor)
    RecyclerView recycler_doctor;

    private BroadcastReceiver doctorDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Doctor> doctorArrayList = intent.getParcelableArrayListExtra(Common.KEY_DOCTOR_LOAD_DONE);
            MyDoctorAdapter adapter = new MyDoctorAdapter(getContext(),doctorArrayList);
            recycler_doctor.setAdapter(adapter);
        }
    };

    static AAppointmentStep2 instance;
    public static AAppointmentStep2 getInstance() {
        if (instance == null)
            instance = new AAppointmentStep2();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(doctorDoneReceiver, new IntentFilter(Common.KEY_DOCTOR_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(doctorDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_apointment_step2, container, false);
        unbinder = ButterKnife.bind(this,itemView);

        initView();

        return itemView;
    }

    private void initView() {
        recycler_doctor.setHasFixedSize(true);
        recycler_doctor.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_doctor.addItemDecoration(new SpacesItemDecoration(4));
    }
}
