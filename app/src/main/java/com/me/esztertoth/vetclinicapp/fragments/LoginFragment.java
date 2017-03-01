package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.StartPageActivity;
import com.me.esztertoth.vetclinicapp.utils.LoginAndSignUpTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    @BindView(R.id.email) EditText emailEditText;
    @BindView(R.id.password) EditText passwordEditText;
    @BindView(R.id.login_button) Button loginButton;
    @BindView(R.id.email_textInput_layout) TextInputLayout emailInputLayout;
    @BindView(R.id.password_textInput_layout) TextInputLayout passwordInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    @OnClick(R.id.login_button)
    public void login() {
        Intent i = new Intent(getActivity(), StartPageActivity.class);
        startActivity(i);
    }

    private void initUI() {
        setDefaultFontForPasswordField();
        emailEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(emailEditText, emailInputLayout, getActivity()));
        passwordEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(passwordEditText, passwordInputLayout, getActivity()));
    }

    private void setDefaultFontForPasswordField() {
        passwordEditText.setTypeface(Typeface.DEFAULT);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
    }

}
