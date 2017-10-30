package com.me.esztertoth.vetclinicapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.me.esztertoth.vetclinicapp.fragments.EditProfileFragment;
import com.me.esztertoth.vetclinicapp.fragments.ProfileContentFragment;
import com.me.esztertoth.vetclinicapp.model.User;
import com.me.esztertoth.vetclinicapp.model.Vet;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.PetOwnerApiInterface;
import com.me.esztertoth.vetclinicapp.rest.VetApiInterface;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject ApiClient apiClient;

    private static final String USER = "user";

    private User user;

    private String token;
    private long userId;

    private PetOwnerApiInterface petOwnerApiInterface;
    private VetApiInterface vetApiInterface;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        satisfyDependencies();

        token = VetClinicPreferences.getSessionToken(this);
        userId = VetClinicPreferences.getUserId(this);

        if(VetClinicPreferences.getIsVet(this)) {
            vetApiInterface = apiClient.createService(VetApiInterface.class, token);
            getVetDetails();
        } else {
            petOwnerApiInterface = apiClient.createService(PetOwnerApiInterface.class, token);
            getPetOwnerDetails();
        }
    }

    private void satisfyDependencies() {
        ((App) getApplication()).getNetComponent().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private void getPetOwnerDetails() {
        subscription = petOwnerApiInterface.getPetOwner(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public final void onCompleted() {
                        if(user != null) {
                            setToolbarWithName();
                            openProfileContent();
                        }
                    }

                    @Override
                    public final void onError(Throwable e) {
                    }

                    @Override
                    public final void onNext(User response) {
                        user = response;
                    }

                });
    }

    private void getVetDetails() {
        subscription = vetApiInterface.getVet(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Vet>() {
                    @Override
                    public final void onCompleted() {
                        if(user != null) {
                            setToolbarWithName();
                            openProfileContent();
                        }
                    }

                    @Override
                    public final void onError(Throwable e) {
                    }

                    @Override
                    public final void onNext(Vet response) {
                        user = response;
                    }

                });
    }

    private void setToolbarWithName() {
        setSupportActionBar(toolbar);
        if(!TextUtils.isEmpty(user.getFirstName()) || !TextUtils.isEmpty(user.getLastName()))
            getSupportActionBar().setTitle(user.getFirstName() + " " + user.getLastName());
        else
            getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void openProfileContent() {
        ProfileContentFragment profileContentFragment = new ProfileContentFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        profileContentFragment.setArguments(bundle);
        ft.replace(R.id.profile_container, profileContentFragment);
        ft.commit();
    }


    @OnClick(R.id.fab)
    public void openEditProfile() {
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        editProfileFragment.setArguments(bundle);
        ft.replace(R.id.profile_container, editProfileFragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}