package com.me.esztertoth.vetclinicapp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.dialog.PetBirthayPickerDialog;
import com.me.esztertoth.vetclinicapp.model.BirthDate;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.utils.FavoriteUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewPetFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.pet_name_edit_text)
    EditText petNameEditText;
    @BindView(R.id.pet_birthday_input)
    TextView petAgeTextView;
    @BindView(R.id.weight_input)
    EditText petWeightEditText;
    @BindView(R.id.pet_type_spinner)
    Spinner typeSpinnerView;

    private static final String BIRTHDAY_DIALOG_NAME = "birtdayPickerDialog";
    private static final String BIRTHDAY_DATE_FORMAT = "MM/dd/yyyy";

    private BirthDate birthDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_pet, container, false);
        ButterKnife.bind(this, view);

        typeSpinnerView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_dropdown_item, PetType.values()));

        return view;
    }

    @OnClick(R.id.save_pet_button)
    public void savePetAndClose() {
        hideKeyboard();
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        openDontSaveDialog();
    }

    @OnClick(R.id.pet_birthday_input)
    void openBirthdayPicker() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment birthdayPicker = new PetBirthayPickerDialog(this);
        birthdayPicker.show(ft, BIRTHDAY_DIALOG_NAME);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        DateTime dateTime = new DateTime(year, month, day, 0, 0, 0, 0);
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(BIRTHDAY_DATE_FORMAT);
        petAgeTextView.setText(dtfOut.print(dateTime));
        birthDate = createBirthDateFromDateTime(dateTime);
    }

    private BirthDate createBirthDateFromDateTime(DateTime dateTime) {
        return new BirthDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
    }

    private Pet createNewPet() {
        Pet newPet = new Pet();
        newPet.setName(petNameEditText.getText().toString());
        newPet.setDateOfBirth(birthDate);
        newPet.setWeight(Double.valueOf(petWeightEditText.getText().toString()));
        newPet.setType((PetType) typeSpinnerView.getItemAtPosition(typeSpinnerView.getSelectedItemPosition()));

        return newPet;
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getActivity().getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void openDontSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dont_save_pet_dialog_title))
                .setMessage(getString(R.string.dont_save_pet_dialog_description))
                .setPositiveButton(R.string.dont_save_pet_dialog_positive_button, (dialog, which) -> dialog.dismiss())
                .setNegativeButton(R.string.dont_save_pet_dialog_negative_button, (dialog, which) -> {
                    dialog.dismiss();
                    hideKeyboard();
                    getActivity().onBackPressed();
                })
                .show();
    }

}
