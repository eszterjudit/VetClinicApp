package com.me.esztertoth.vetclinicapp.dialog;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

public class ClinicTimePickerDialog extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener timeSetListener;

    public ClinicTimePickerDialog() {}

    public ClinicTimePickerDialog(TimePickerDialog.OnTimeSetListener timeSetListener) {
        this.timeSetListener = timeSetListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), timeSetListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true);
        return timePickerDialog;
    }

}
