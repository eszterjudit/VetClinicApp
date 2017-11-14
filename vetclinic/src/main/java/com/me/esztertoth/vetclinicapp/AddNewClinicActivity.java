package com.me.esztertoth.vetclinicapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.me.esztertoth.vetclinicapp.dialog.ClinicTimePickerDialog;
import com.me.esztertoth.vetclinicapp.model.Address;
import com.me.esztertoth.vetclinicapp.model.Clinic;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.ClinicApiInterface;
import com.me.esztertoth.vetclinicapp.utils.DialogUtils;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewClinicActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.clinic_name_edit_text) EditText clinicName;
    @BindView(R.id.opening_hour) TextView openingHour;
    @BindView(R.id.closing_hour) TextView closingHour;
    @BindView(R.id.street) EditText street;
    @BindView(R.id.country) EditText country;
    @BindView(R.id.city) EditText city;
    @BindView(R.id.zip) EditText zip;

    @Inject ApiClient apiClient;
    @Inject VetClinicPreferences prefs;
    @Inject DialogUtils dialogUtils;

    private static final String OPENING_HOUR_PICKER = "timePicker";

    private ClinicApiInterface clinicApiInterface;

    private String token;
    private Long userId;

    private TextView pickedTimeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_clinic);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        satisfyDependencies();

        token = prefs.getSessionToken();
        userId = prefs.getUserId();
        clinicApiInterface = apiClient.createService(ClinicApiInterface.class, token);
    }

    private void satisfyDependencies() {
        ((App) getApplication()).getNetComponent().inject(this);
        ((App) getApplication()).getAppComponent().inject(this);
    }

    @OnClick(R.id.save_clinic_button)
    public void saveClinicAndClose() {
        if(areAllFieldsFilled()) {
            postNewClinic();
        } else {
            openFieldsNotFilledDialog();
        }
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        openDontSaveDialog();
    }

    @OnClick(R.id.opening_hour)
    public void openOpeningHourTimePicker() {
        pickedTimeView = openingHour;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment openingHourPicker = new ClinicTimePickerDialog(this);
        openingHourPicker.show(ft, OPENING_HOUR_PICKER);
    }

    @OnClick(R.id.closing_hour)
    public void openClosingHourTimePicker() {
        pickedTimeView = closingHour;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment closingHourPicker = new ClinicTimePickerDialog(this);
        closingHourPicker.show(ft, OPENING_HOUR_PICKER);
    }

    private boolean areAllFieldsFilled() {
        return  !clinicName.getText().toString().isEmpty() &&
                !openingHour.getText().toString().isEmpty() &&
                !closingHour.getText().toString().isEmpty() &&
                !street.getText().toString().isEmpty() &&
                !city.getText().toString().isEmpty() &&
                !country.getText().toString().isEmpty() &&
                !zip.getText().toString().isEmpty();
    }

    private Clinic createClinic() {
        Clinic newClinic = new Clinic();
        newClinic.setName(clinicName.getText().toString());
        newClinic.setOpeningHour(openingHour.getText().toString());
        newClinic.setClosingHour(closingHour.getText().toString());
        newClinic.setAddress(createAddress());

        return newClinic;
    }

    private Address createAddress() {
        Address newAddress = new Address();
        newAddress.setStreet(street.getText().toString());
        newAddress.setCity(city.getText().toString());
        newAddress.setCountry(country.getText().toString());
        newAddress.setZip(zip.getText().toString());

        return newAddress;
    }

    private void postNewClinic() {
        Call<ResponseBody> call = clinicApiInterface.addClinic(token, userId, createClinic());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    closeActivity();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void openDontSaveDialog() {
        dialogUtils.showWarningDialog( this,
                getString(R.string.dont_save_pet_dialog_title),
                getString(R.string.dont_save_pet_dialog_description),
                getString(R.string.dont_save_pet_dialog_positive_button),
                getString(R.string.dont_save_pet_dialog_negative_button),
                (dialogInterface, i) -> dialogInterface.dismiss(),
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    closeActivity();
                });
    }

    private void openFieldsNotFilledDialog() {
        dialogUtils.showWarningDialog(  this,
                getString(R.string.fields_not_filled_dialog_title),
                getString(R.string.fields_not_filled_dialog_description),
                getString(R.string.fields_not_filled_dialog_positive_button),
                getString(R.string.fields_not_filled_dialog_negative_button),
                (dialogInterface, i) -> dialogInterface.dismiss(),
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    closeActivity();
                });
    }

    private void closeActivity() {
        hideKeyboard();
        onBackPressed();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time = String.format("%02d:%02d", hour, minute);
        pickedTimeView.setText(time);
    }
}
