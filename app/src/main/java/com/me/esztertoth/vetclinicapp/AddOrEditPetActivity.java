package com.me.esztertoth.vetclinicapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
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
import com.me.esztertoth.vetclinicapp.model.Pet;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.PetApiInterface;
import com.me.esztertoth.vetclinicapp.rest.PetOwnerApiInterface;
import com.me.esztertoth.vetclinicapp.utils.DialogUtils;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrEditPetActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pet_name_edit_text) EditText petNameEditText;
    @BindView(R.id.pet_birthday_input) TextView petAgeTextView;
    @BindView(R.id.weight_input) EditText petWeightEditText;
    @BindView(R.id.pet_type_spinner) Spinner typeSpinnerView;

    @Inject ApiClient apiClient;
    @Inject VetClinicPreferences prefs;
    @Inject DialogUtils dialogUtils;

    private PetOwnerApiInterface petOwnerApiInterface;
    private PetApiInterface petApiInterface;

    private String token;
    private long userId;

    private Pet pet;

    private static final String BIRTHDAY_DIALOG_NAME = "birtdayPickerDialog";
    private static final String BIRTHDAY_DATE_FORMAT = "MM/dd/yyyy";
    private static final String FORMAT = "yyyy-MM-dd";

    private DateTime birthday;
    private String birthDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);
        ButterKnife.bind(this);

        pet = (Pet) getIntent().getSerializableExtra("pet");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        satisfyDependencies();

        token = prefs.getSessionToken();
        userId = prefs.getUserId();
        petOwnerApiInterface = apiClient.createService(PetOwnerApiInterface.class, token);
        petApiInterface = apiClient.createService(PetApiInterface.class, token);

        typeSpinnerView.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, PetType.values()));

        if(pet != null) {
            setPetDetailsOnStart();
        }
    }

    private void satisfyDependencies() {
        ((App) getApplication()).getNetComponent().inject(this);
        ((App) getApplication()).getAppComponent().inject(this);
    }

    private void setPetDetailsOnStart() {
        int petTypeSelectedIndex = Arrays.asList(PetType.values()).indexOf(pet.getType());
        petNameEditText.setText(pet.getName());
        petWeightEditText.setText(String.valueOf(pet.getWeight()));
        typeSpinnerView.setSelection(petTypeSelectedIndex);
        petAgeTextView.setText(parsePetBirthDay());
    }

    @OnClick(R.id.save_pet_button)
    public void savePetAndClose() {
        if(areAllFieldsFilled()) {
            if(pet != null) {
                updatePet();
            } else {
                postNewPet();
            }
        } else {
            openFieldsNotFilledDialog();
        }
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        openDontSaveDialog();
    }

    @OnClick(R.id.pet_birthday_input)
    void openBirthdayPicker() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment birthdayPicker = new PetBirthayPickerDialog(this, birthday);
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

    private String parsePetBirthDay() {
        DateTimeFormatter format = DateTimeFormat.forPattern(FORMAT);
        birthday = format.parseDateTime(pet.getDateOfBirth());
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(BIRTHDAY_DATE_FORMAT);

        return dtfOut.print(birthday);
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
        Call<ResponseBody> call = petOwnerApiInterface.addPet(token, userId, createNewPet());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    closeFragment();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void updatePet() {
        Call<ResponseBody> call = petApiInterface.updatePet(token, pet.getId(), createNewPet());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    closeFragment();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
        dialogUtils.showWarningDialog( this,
                getString(R.string.dont_save_pet_dialog_title),
                getString(R.string.dont_save_pet_dialog_description),
                getString(R.string.dont_save_pet_dialog_positive_button),
                getString(R.string.dont_save_pet_dialog_negative_button),
                (dialogInterface, i) -> dialogInterface.dismiss(),
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    closeFragment();
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
                    closeFragment();
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            openDontSaveDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean areAllFieldsFilled() {
        return  !petNameEditText.getText().toString().isEmpty() &&
                !petAgeTextView.getText().toString().isEmpty() &&
                !petWeightEditText.getText().toString().isEmpty();

    }

}
