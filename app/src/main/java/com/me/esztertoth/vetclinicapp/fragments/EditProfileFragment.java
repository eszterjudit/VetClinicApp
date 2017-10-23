package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Address;
import com.me.esztertoth.vetclinicapp.model.User;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.PetOwnerApiInterface;
import com.me.esztertoth.vetclinicapp.rest.VetApiInterface;
import com.me.esztertoth.vetclinicapp.utils.LoginAndSignUpTextWatcher;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    @BindView(R.id.firstName) TextInputEditText firstNameEditText;
    @BindView(R.id.lastName) TextInputEditText lastNameEditText;
    @BindView(R.id.email_textInput_layout) TextInputLayout emailInputLayout;
    @BindView(R.id.email) TextInputEditText emailEditText;
    @BindView(R.id.phone) TextInputEditText phoneEditText;
    @BindView(R.id.street) TextInputEditText streetEditText;
    @BindView(R.id.city) TextInputEditText cityEditText;
    @BindView(R.id.country) TextInputEditText countryEditText;
    @BindView(R.id.zip) TextInputEditText zipEditText;

    private static final String USER = "user";

    private FloatingActionButton fab;
    private User user;

    private long userId;
    private String token;
    private boolean isVet;

    private PetOwnerApiInterface petOwnerApiInterface;
    private VetApiInterface vetApiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);

        user = (User) getArguments().getSerializable(USER);
        fab = ButterKnife.findById(getActivity(), R.id.fab);

        token = VetClinicPreferences.getSessionToken(getContext());
        userId = VetClinicPreferences.getUserId(getContext());
        isVet = VetClinicPreferences.getIsVet(getContext());

        if(isVet == true) {
            vetApiInterface = ApiClient.createService(VetApiInterface.class, token);
        } else {
            petOwnerApiInterface = ApiClient.createService(PetOwnerApiInterface.class, token);
        }

        emailEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(emailEditText, emailInputLayout, getContext()));

        hideFloatingActionButton();
        prefillForm();
        return view;
    }

    @OnClick(R.id.cancel_edit_button)
    public void cancelEdit() {
        openDontSaveDialog();
    }

    @OnClick(R.id.save_profile_button)
    public void saveProfile() {
        if(!TextUtils.isEmpty(emailEditText.getText().toString())) {
            if(isVet) {
                saveVetData();
            } else {
                savePetOwnerData();
            }
            closeFragment();
        } else {
            openEmailCannotBeEmptyDialog();
        }
    }

    private void prefillForm() {
        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        emailEditText.setText(user.getEmail());
        phoneEditText.setText(user.getPhone());
        if(user.getAddress() != null) {
            streetEditText.setText(user.getAddress().getStreet());
            cityEditText.setText(user.getAddress().getCity());
            countryEditText.setText(user.getAddress().getCountry());
            zipEditText.setText(String.valueOf(user.getAddress().getZip()));
        }
    }

    private User createUserDetails() {
        User editedUser = new User();
        editedUser.setFirstName(firstNameEditText.getText().toString());
        editedUser.setLastName(lastNameEditText.getText().toString());
        editedUser.setEmail(emailEditText.getText().toString());
        editedUser.setPhone(phoneEditText.getText().toString());
        editedUser.setAddress(createAddress());

        return editedUser;
    }

    private Address createAddress() {
        Address address = new Address();
        address.setStreet(streetEditText.getText().toString());
        address.setCity(cityEditText.getText().toString());
        address.setCountry(countryEditText.getText().toString());
        address.setZip(zipEditText.getText().toString());

        return address;
    }

    private void savePetOwnerData() {
        Call<ResponseBody> call = petOwnerApiInterface.updatePetOwner(token, userId, createUserDetails());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void saveVetData() {
        Call<ResponseBody> call = vetApiInterface.updateVet(token, userId, createUserDetails());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void closeFragment() {
        showFloatingActionButton();
        getActivity().onBackPressed();
    }

    private void openDontSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dont_save_pet_dialog_title))
                .setMessage(getString(R.string.dont_save_pet_dialog_description))
                .setPositiveButton(R.string.dont_save_pet_dialog_positive_button, (dialog, which) -> dialog.dismiss())
                .setNegativeButton(R.string.dont_save_pet_dialog_negative_button, (dialog, which) -> {
                    dialog.dismiss();
                    closeFragment();
                })
                .show();
    }

    private void openEmailCannotBeEmptyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.email_cant_be_empty_dialog_title))
                .setMessage(getString(R.string.email_cant_be_empty_dialog_description))
                .setPositiveButton(R.string.email_cant_be_empty_dialog_positive_button, (dialog, which) -> dialog.dismiss())
                .setNegativeButton(R.string.email_cant_be_empty_dialog_negative_button, (dialog, which) -> {
                    dialog.dismiss();
                    closeFragment();
                })
                .show();
    }

    private void hideFloatingActionButton() {
        fab.setVisibility(View.GONE);
    }

    private void showFloatingActionButton() {
        fab.setVisibility(View.VISIBLE);
    }

}
