package com.me.esztertoth.vetclinicapp;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.fragments.LoginFragment;
import com.me.esztertoth.vetclinicapp.fragments.SignUpFragment;
import com.vansuita.gaussianblur.GaussianBlur;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.background) ImageView backgroundImageView;
    @BindView(R.id.login_or_signUp_link) TextView loginOrSignUpButton;
    @BindView(R.id.login_or_signUp_question) TextView loginOrSignUpQuestion;

    private boolean isOnLoginScreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreenMode();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        openLoginFragment();
        GaussianBlur.with(this).radius(5).put(getRandomBackgroundPicture(), backgroundImageView);
    }

    private void setFullscreenMode() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private int getRandomBackgroundPicture() {
        TypedArray images = getResources().obtainTypedArray(R.array.login_backgrounds);
        int choice = (int) (Math.random() * images.length());
        int drawable_res_id = images.getResourceId(choice, R.drawable.login_bg1);
        images.recycle();
        return drawable_res_id;
    }

    private void changeTextForLoginOrSignupButton() {
        if (isOnLoginScreen) {
            loginOrSignUpQuestion.setText(getString(R.string.not_a_member));
            loginOrSignUpButton.setText(getString(R.string.signup));
        } else {
            loginOrSignUpQuestion.setText(getString(R.string.already_a_member));
            loginOrSignUpButton.setText(getString(R.string.login));
        }
    }

    private void openLoginFragment() {
        isOnLoginScreen = true;
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.loginAndSignUp_container, loginFragment);
        ft.commit();
    }

    private void openSignupFragment() {
        isOnLoginScreen = false;
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.loginAndSignUp_container, signUpFragment);
        ft.commit();
    }

    @OnClick(R.id.login_or_signUp_link)
    public void switchForm() {
        if (isOnLoginScreen) {
            openSignupFragment();
            changeTextForLoginOrSignupButton();
        } else {
            openLoginFragment();
            changeTextForLoginOrSignupButton();
        }
    }

}
