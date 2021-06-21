package com.android.covidaid;

public interface IAppointmentInfoLoadListener {
    void onAppointmentInfoLoadEmpty();
    void onAppointmentInfoLoadSuccess(AppointmentInformation appointmentInformation);
    void onAppointmentInfoLoadFailed(String message);
}
