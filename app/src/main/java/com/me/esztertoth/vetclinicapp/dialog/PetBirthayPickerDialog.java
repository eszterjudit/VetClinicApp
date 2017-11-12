package com.me.esztertoth.vetclinicapp.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import org.joda.time.DateTime;

import java.util.Calendar;

public class PetBirthayPickerDialog extends DialogFragment {

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DateTime dateTime;

    public PetBirthayPickerDialog() {
    }

    public PetBirthayPickerDialog(DatePickerDialog.OnDateSetListener callback, DateTime dateTime) {
        this.dateSetListener = callback;
        this.dateTime = dateTime;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog;
        if(dateTime != null) {
            datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        } else {
            datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        return datePickerDialog;
    }

}
