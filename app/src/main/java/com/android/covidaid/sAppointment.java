package com.android.covidaid;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class sAppointment extends Fragment implements IAppointmentInfoLoadListener {
    @BindView(R.id.card_appointment_info)
    CardView card_apointment_info;
    @BindView(R.id.txt_hospital_address)
    TextView txt_hospital_address;
    @BindView(R.id.txt_hospital_doctor)
    TextView txt_hospital_doctor;
    @BindView(R.id.txt_hospital)
    TextView txt_hospital;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_time_remain)
    TextView txt_time_remain;

    Unbinder unbinder;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, fullname, phone, userEmail;

    IAppointmentInfoLoadListener iAppointmentInfoLoadListener;

    @Override
    public void onResume() {
        super.onResume();
        loadUserAppointment();
    }

    private void loadUserAppointment() {
        CollectionReference userAppointment = FirebaseFirestore.getInstance()
                .collection("User")
                .document(userID)
                .collection("Appointment");

        //get current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Timestamp todayTimeStamp = new Timestamp(calendar.getTime());

        userAppointment
                .whereGreaterThanOrEqualTo("timestamp", todayTimeStamp)
                .whereEqualTo("done", false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            if (!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                    AppointmentInformation appointmentInformation = queryDocumentSnapshot.toObject(AppointmentInformation.class);
                                    iAppointmentInfoLoadListener.onAppointmentInfoLoadSuccess(appointmentInformation);
                                    break;
                                }
                            }

                            else {
                                iAppointmentInfoLoadListener.onAppointmentInfoLoadEmpty();
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAppointmentInfoLoadListener.onAppointmentInfoLoadFailed(e.getMessage());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView =  inflater.inflate(R.layout.s_appointment,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        iAppointmentInfoLoadListener = this;

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    fullname = userProfile.fullName;
                    userEmail = userProfile.email;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "database error", Toast.LENGTH_LONG).show();
            }
        });

        loadUserAppointment();

        return itemView;
    }

    @Override
    public void onAppointmentInfoLoadEmpty() {
        card_apointment_info.setVisibility(View.GONE);
    }

    @Override
    public void onAppointmentInfoLoadSuccess(AppointmentInformation appointmentInformation) {
        txt_hospital_address.setText(appointmentInformation.getHospitalAddress());
        txt_hospital_doctor.setText(appointmentInformation.getDoctorName());
        txt_hospital.setText(appointmentInformation.getHospitalName());
        txt_time.setText(appointmentInformation.getTime());
        String dateRemain = DateUtils.getRelativeTimeSpanString(
                Long.valueOf(appointmentInformation.getTimestamp().toDate().getTime()),
                Calendar.getInstance().getTimeInMillis(), 0).toString();

        txt_time_remain.setText(dateRemain);
    }

    @Override
    public void onAppointmentInfoLoadFailed(String message) {
        Toast.makeText(getContext(), message,Toast.LENGTH_SHORT).show();
    }
}
