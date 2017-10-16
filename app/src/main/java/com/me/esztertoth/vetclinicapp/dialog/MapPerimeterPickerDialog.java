package com.me.esztertoth.vetclinicapp.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

public class MapPerimeterPickerDialog extends DialogFragment {

    private NumberPicker numberPicker;
    private static int KILOMETERS = 1000;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View numberPickerDialogView = inflater.inflate(R.layout.dialog_numberpicker, null);

        numberPicker = (NumberPicker) numberPickerDialogView.findViewById(R.id.number_picker);

        initPicker();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.map_perimeter_dialog_title);
        alertDialogBuilder.setView(numberPickerDialogView);
        alertDialogBuilder.setMessage(R.string.map_perimeter_dialog_description);
        alertDialogBuilder.setPositiveButton(R.string.map_perimeter_dialog_positive_button, (dialog, which) -> {
            int chosenPerimeter = getChosenPerimeterForDisplayedValue(numberPicker.getValue());
            VetClinicPreferences.setPerimeter(getActivity(), chosenPerimeter * KILOMETERS);
        });
        alertDialogBuilder.setNegativeButton(R.string.map_perimeter_dialog_negative_button, (dialog, which) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

    private void initPicker() {
        String[] perimeterValues = {"1", "5", "10", "15", "20", "25"};
        setDisplayedValuesOfPicker(perimeterValues);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setValue(getValueForPerimeter());
    }

    protected void setDisplayedValuesOfPicker(String[] displayedValues) {
        int minValue = Integer.parseInt(displayedValues[0]);
        int maxValue = displayedValues.length;
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setDisplayedValues(displayedValues);
    }

    private int getChosenPerimeterForDisplayedValue(int picked) {
        switch (picked) {
            case 1:
                return 1;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 15;
            case 5:
                return 20;
            case 6:
                return 25;
        }
        return 1;
    }

    private int getValueForPerimeter() {
        switch (VetClinicPreferences.getPerimeter(getActivity())) {
            case 1000:
                return 1;
            case 5000:
                return 2;
            case 10000:
                return 3;
            case 15000:
                return 4;
            case 20000:
                return 5;
            case 25000:
                return 6;
        }
        return 2;
    }

}
