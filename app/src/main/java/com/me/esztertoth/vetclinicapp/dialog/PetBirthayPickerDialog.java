package com.me.esztertoth.vetclinicapp.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class PetBirthayPickerDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener dateSetListener;

    public PetBirthayPickerDialog() {
    }

    public PetBirthayPickerDialog(DatePickerDialog.OnDateSetListener callback) {
        dateSetListener = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        return datePickerDialog;
    }

}
