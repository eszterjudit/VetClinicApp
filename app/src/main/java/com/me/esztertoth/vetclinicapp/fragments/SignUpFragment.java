package com.me.esztertoth.vetclinicapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.utils.LoginAndSignUpTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends Fragment {

    @BindView(R.id.email_textInput_layout) TextInputLayout emailInputLayout;
    @BindView(R.id.password_textInput_layout) TextInputLayout passwordInputLayout;
    @BindView(R.id.password_again_textInput_layout) TextInputLayout passwordAgainInputLayout;
    @BindView(R.id.email) EditText emailEditText;
    @BindView(R.id.password) EditText passwordEditText;
    @BindView(R.id.password_again) EditText passwordAgainEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    @OnClick(R.id.signup_button)
    public void signUp() {
        Toast.makeText(getActivity(), getString(R.string.signup), Toast.LENGTH_SHORT).show();
    }

    private void initUI() {
        setDefaultFontForPasswordField();
        emailEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(emailEditText, emailInputLayout, getActivity()));
        passwordEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(passwordEditText, passwordInputLayout, getActivity()));
        passwordAgainEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(passwordAgainEditText, passwordAgainInputLayout, getActivity()));
    }

    private void setDefaultFontForPasswordField() {
        passwordEditText.setTypeface(Typeface.DEFAULT);
        passwordAgainEditText.setTypeface(Typeface.DEFAULT);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
        passwordAgainEditText.setTransformationMethod(new PasswordTransformationMethod());
    }

}
