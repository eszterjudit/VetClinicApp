package com.me.esztertoth.vetclinicapp.dialog;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class ClinicTimePickerDialog extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener timeSetListener;

    public ClinicTimePickerDialog() {}

    public ClinicTimePickerDialog(TimePickerDialog.OnTimeSetListener timeSetListener) {
        this.timeSetListener = timeSetListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        return new TimePickerDialog(getActivity(), timeSetListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true);
    }

}
