package com.me.esztertoth.vetclinicapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Address;
import com.me.esztertoth.vetclinicapp.model.PetOwner;
import com.me.esztertoth.vetclinicapp.model.PetType;
import com.me.esztertoth.vetclinicapp.model.User;
import com.me.esztertoth.vetclinicapp.model.Vet;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.PetOwnerApiInterface;
import com.me.esztertoth.vetclinicapp.rest.VetApiInterface;
import com.me.esztertoth.vetclinicapp.utils.DialogUtils;
import com.me.esztertoth.vetclinicapp.utils.LoginAndSignUpTextWatcher;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.List;

import javax.inject.Inject;

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

    @BindView(R.id.specialitiesContainer) LinearLayout specialitiesContainer;

    @BindView(R.id.dogImage) ImageView dogImage;
    @BindView(R.id.dogOverlay) ImageView dogOverlay;
    @BindView(R.id.catImage) ImageView catImage;
    @BindView(R.id.catOverlay) ImageView catOverlay;
    @BindView(R.id.reptileImage) ImageView reptileImage;
    @BindView(R.id.reptileOverlay) ImageView reptileOverlay;
    @BindView(R.id.rodentImage) ImageView rodentImage;
    @BindView(R.id.rodentOverlay) ImageView rodentOverlay;
    @BindView(R.id.birdImage) ImageView birdImage;
    @BindView(R.id.birdOverlay) ImageView birdOverlay;

    private static final String USER = "user";

    private FloatingActionButton fab;

    private PetOwner petOwner;
    private Vet vet;

    private User user;

    private long userId;
    private String token;
    private boolean isVet;

    @Inject ApiClient apiClient;
    @Inject VetClinicPreferences prefs;
    @Inject DialogUtils dialogUtils;

    private PetOwnerApiInterface petOwnerApiInterface;
    private VetApiInterface vetApiInterface;

    private List<PetType> specialities;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        satisfyDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);

        fab = ButterKnife.findById(getActivity(), R.id.fab);

        token = prefs.getSessionToken();
        userId = prefs.getUserId();
        isVet = prefs.getIsVet();

        if(isVet) {
            vet = (Vet) getArguments().getSerializable(USER);
            vetApiInterface = apiClient.createService(VetApiInterface.class, token);
            specialities = vet.getSpeciality();
            showSpecialitiesEditorForVet();
        } else {
            petOwner = (PetOwner) getArguments().getSerializable(USER);
            petOwnerApiInterface = apiClient.createService(PetOwnerApiInterface.class, token);
        }

        emailEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(emailEditText, emailInputLayout, getContext()));

        hideFloatingActionButton();
        prefillForm();
        return view;
    }

    private void satisfyDependencies() {
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    private void showSpecialitiesEditorForVet() {
        specialitiesContainer.setVisibility(View.VISIBLE);
        prefillSpecialities();
    }

    private void prefillSpecialities() {
        if(specialities.contains(PetType.DOG)) {
            overLayImage(dogImage, dogOverlay);
        }
        if(specialities.contains(PetType.CAT)) {
            overLayImage(catImage, catOverlay);
        }
        if(specialities.contains(PetType.REPTILE)) {
            overLayImage(reptileImage, reptileOverlay);
        }
        if(specialities.contains(PetType.RODENT)) {
            overLayImage(rodentImage, rodentImage);
        }
        if(specialities.contains(PetType.BIRD)) {
            overLayImage(birdImage, birdOverlay);
        }
    }

    @OnClick(R.id.cancel_edit_button)
    public void cancelEdit() {
        showCloseWarningDialog();
    }

    @OnClick(R.id.save_profile_button)
    public void saveProfile() {
            if(isVet) {
                saveVetData();
            } else {
                savePetOwnerData();
            }
            closeFragment();
    }

    private void showCloseWarningDialog() {
        dialogUtils.showWarningDialog(  getContext(),
                getString(R.string.dont_save_pet_dialog_title),
                getString(R.string.dont_save_pet_dialog_description),
                getString(R.string.dont_save_pet_dialog_positive_button),
                getString(R.string.dont_save_pet_dialog_negative_button),
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                },
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    closeFragment();
                });
    }

    private void addOrRemovePetType(PetType petType, ImageView imageView, ImageView overlay) {
        if(specialities.contains(petType)) {
            specialities.remove(petType);
            removeOverlay(imageView, overlay);
        } else {
            specialities.add(petType);
            overLayImage(imageView, overlay);
        }
    }

    private void overLayImage(ImageView imageView, ImageView overlay) {
        imageView.setImageAlpha(50);
        overlay.setVisibility(View.VISIBLE);
    }

    private void removeOverlay(ImageView imageView, ImageView overlay) {
        imageView.setImageAlpha(255);
        overlay.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.dogView)
    void addDogSpecialityForVet() {
        addOrRemovePetType(PetType.DOG, dogImage, dogOverlay);
    }

    @OnClick(R.id.catView)
    void addCatSpecialityForVet() {
        addOrRemovePetType(PetType.CAT, catImage, catOverlay);
    }

    @OnClick(R.id.reptileView)
    void addReptileSpecialityForVet() {
        addOrRemovePetType(PetType.REPTILE, reptileImage, reptileOverlay);
    }

    @OnClick(R.id.rodentView)
    void addRodentSpecialityForVet() {
        addOrRemovePetType(PetType.RODENT, rodentImage, rodentOverlay);
    }

    @OnClick(R.id.birdView)
    void addBirdSpecialityForVet() {
        addOrRemovePetType(PetType.BIRD, birdImage, birdOverlay);
    }

    private void prefillForm() {
        if(isVet) {
            user = vet;
        } else {
            user = petOwner;
        }
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

    private PetOwner createPetOwnerDetails() {
        PetOwner editedUser = new PetOwner();
        editedUser.setFirstName(firstNameEditText.getText().toString());
        editedUser.setLastName(lastNameEditText.getText().toString());
        editedUser.setEmail(emailEditText.getText().toString());
        editedUser.setPhone(phoneEditText.getText().toString());
        editedUser.setAddress(createAddress());

        return editedUser;
    }

    private Vet createVetDetails() {
        Vet editedUser = new Vet();
        editedUser.setFirstName(firstNameEditText.getText().toString());
        editedUser.setLastName(lastNameEditText.getText().toString());
        editedUser.setEmail(emailEditText.getText().toString());
        editedUser.setPhone(phoneEditText.getText().toString());
        editedUser.setAddress(createAddress());
        editedUser.setSpeciality(specialities);

        return editedUser;
    }

    private Address createAddress() {
        Address address;
        if(user.getAddress() != null) {
            address = user.getAddress();
        } else {
            address = new Address();
        }
        address.setStreet(streetEditText.getText().toString());
        address.setCity(cityEditText.getText().toString());
        address.setCountry(countryEditText.getText().toString());
        address.setZip(zipEditText.getText().toString());

        return address;
    }

    private void savePetOwnerData() {
        Call<ResponseBody> call = petOwnerApiInterface.updatePetOwner(token, userId, createPetOwnerDetails());
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
        Call<ResponseBody> call = vetApiInterface.updateVet(token, userId, createVetDetails());
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

    private void hideFloatingActionButton() {
        fab.setVisibility(View.GONE);
    }

    private void showFloatingActionButton() {
        fab.setVisibility(View.VISIBLE);
    }

}
