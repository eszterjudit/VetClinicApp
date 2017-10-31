package com.me.esztertoth.vetclinicapp.dagger.component;

import com.me.esztertoth.vetclinicapp.AddNewPetActivity;
import com.me.esztertoth.vetclinicapp.ClinicDetailsActivity;
import com.me.esztertoth.vetclinicapp.MainActivity;
import com.me.esztertoth.vetclinicapp.ProfileActivity;
import com.me.esztertoth.vetclinicapp.StartPageActivity;
import com.me.esztertoth.vetclinicapp.dagger.module.AppModule;
import com.me.esztertoth.vetclinicapp.dialog.MapPerimeterPickerDialog;
import com.me.esztertoth.vetclinicapp.fragments.ClinicDetailsContentFragment;
import com.me.esztertoth.vetclinicapp.fragments.EditProfileFragment;
import com.me.esztertoth.vetclinicapp.fragments.LoginFragment;
import com.me.esztertoth.vetclinicapp.fragments.MapFragment;
import com.me.esztertoth.vetclinicapp.fragments.MyClinicsFragment;
import com.me.esztertoth.vetclinicapp.fragments.MyFavoritesFragment;
import com.me.esztertoth.vetclinicapp.fragments.MyPetsFragment;
import com.me.esztertoth.vetclinicapp.fragments.ProfileContentFragment;
import com.me.esztertoth.vetclinicapp.fragments.SettingsFragment;
import com.me.esztertoth.vetclinicapp.fragments.SignUpFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(AddNewPetActivity addNewPetActivity);
    void inject(ProfileActivity profileActivity);
    void inject(ClinicDetailsActivity clinicDetailsActivity);
    void inject(MainActivity mainActivity);
    void inject(StartPageActivity startPageActivity);

    void inject(ClinicDetailsContentFragment clinicDetailsContentFragment);
    void inject(EditProfileFragment editProfileFragment);
    void inject(LoginFragment loginFragment);
    void inject(MapFragment mapFragment);
    void inject(MyClinicsFragment myClinicsFragment);
    void inject(MyFavoritesFragment myFavoritesFragment);
    void inject(MyPetsFragment myPetsFragment);
    void inject(ProfileContentFragment profileContentFragment);
    void inject(SettingsFragment settingsFragment);
    void inject(SignUpFragment signUpFragment);

    void inject(MapPerimeterPickerDialog mapPerimeterPickerDialog);
}
