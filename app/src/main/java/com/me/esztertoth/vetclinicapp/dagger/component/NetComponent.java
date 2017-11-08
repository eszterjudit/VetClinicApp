package com.me.esztertoth.vetclinicapp.dagger.component;

import com.me.esztertoth.vetclinicapp.AddNewClinicActivity;
import com.me.esztertoth.vetclinicapp.AddNewPetActivity;
import com.me.esztertoth.vetclinicapp.ProfileActivity;
import com.me.esztertoth.vetclinicapp.dagger.module.AppModule;
import com.me.esztertoth.vetclinicapp.dagger.module.NetModule;
import com.me.esztertoth.vetclinicapp.fragments.AllVetsFragment;
import com.me.esztertoth.vetclinicapp.fragments.ClinicDetailsContentFragment;
import com.me.esztertoth.vetclinicapp.fragments.EditProfileFragment;
import com.me.esztertoth.vetclinicapp.fragments.LoginFragment;
import com.me.esztertoth.vetclinicapp.fragments.MapFragment;
import com.me.esztertoth.vetclinicapp.fragments.MyClinicsFragment;
import com.me.esztertoth.vetclinicapp.fragments.MyPetsFragment;
import com.me.esztertoth.vetclinicapp.fragments.SignUpFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {

    void inject(AddNewPetActivity addNewPetActivity);
    void inject(ProfileActivity profileActivity);
    void inject(AddNewClinicActivity addNewClinicActivity);

    void inject(ClinicDetailsContentFragment clinicDetailsContentFragment);
    void inject(EditProfileFragment editProfileFragment);
    void inject(LoginFragment loginFragment);
    void inject(MapFragment mapFragment);
    void inject(MyClinicsFragment myClinicsFragment);
    void inject(MyPetsFragment myPetsFragment);
    void inject(SignUpFragment signUpFragment);
    void inject(AllVetsFragment allVetsFragment);

}
