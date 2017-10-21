package com.me.esztertoth.vetclinicapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.dialog.PetBirthayPickerDialog;
import com.me.esztertoth.vetclinicapp.model.BirthDate;
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.ApiInterface;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewPetActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pet_name_edit_text)
    EditText petNameEditText;
    @BindView(R.id.pet_birthday_input)
    TextView petAgeTextView;
    @BindView(R.id.weight_input)
    EditText petWeightEditText;
    @BindView(R.id.pet_type_spinner)
    Spinner typeSpinnerView;

    private ApiInterface apiService;

    private String token;
    private long userId;

    private static final String BIRTHDAY_DIALOG_NAME = "birtdayPickerDialog";
    private static final String BIRTHDAY_DATE_FORMAT = "MM/dd/yyyy";
    private static final String FORMAT = "yyyy-MM-dd";

    private String birthDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        token = VetClinicPreferences.getSessionToken(this);
        userId = VetClinicPreferences.getUserId(this);
        apiService = ApiClient.createService(ApiInterface.class, token);

        typeSpinnerView.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, PetType.values()));
    }

    @OnClick(R.id.save_pet_button)
    public void savePetAndClose() {
        postNewPet();
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        openDontSaveDialog();
    }

    @OnClick(R.id.pet_birthday_input)
    void openBirthdayPicker() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment birthdayPicker = new PetBirthayPickerDialog(this);
        birthdayPicker.show(ft, BIRTHDAY_DIALOG_NAME);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dtfOut = new SimpleDateFormat(BIRTHDAY_DATE_FORMAT);
        SimpleDateFormat formatd = new SimpleDateFormat(FORMAT);
        Date date = calendar.getTime();
        petAgeTextView.setText(dtfOut.format(date));
        birthDate = formatd.format(date);
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

    private void postNewPet() {
        Call<ResponseBody> call = apiService.addPet(token, userId, createNewPet());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    closeFragment();
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // something went completely south (like no internet connection)
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void closeFragment() {
        hideKeyboard();
        onBackPressed();
    }

    private void openDontSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dont_save_pet_dialog_title))
                .setMessage(getString(R.string.dont_save_pet_dialog_description))
                .setPositiveButton(R.string.dont_save_pet_dialog_positive_button, (dialog, which) -> dialog.dismiss())
                .setNegativeButton(R.string.dont_save_pet_dialog_negative_button, (dialog, which) -> {
                    dialog.dismiss();
                    closeFragment();
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            openDontSaveDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
