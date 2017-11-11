package com.me.esztertoth.vetclinicapp.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.StartPageActivity;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.AuthenticationApiInterface;
import com.me.esztertoth.vetclinicapp.utils.FormValidator;
import com.me.esztertoth.vetclinicapp.utils.VetClinicPreferences;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    @BindView(R.id.email) EditText emailEditText;
    @BindView(R.id.password) EditText passwordEditText;
    @BindView(R.id.login_button) Button loginButton;
    @BindView(R.id.email_textInput_layout) TextInputLayout emailInputLayout;
    @BindView(R.id.password_textInput_layout) TextInputLayout passwordInputLayout;
    @BindView(R.id.login_error_message) TextView loginErrorMessage;
    @BindView(R.id.progressbar) ProgressBar progressBar;

    @Inject ApiClient apiClient;
    @Inject VetClinicPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        satisfyDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        setDefaultFontForPasswordField();
        return view;
    }

    @OnClick(R.id.login_button)
    public void login() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()) {
            loginErrorMessage.setVisibility(View.GONE);
            doLogin(email, password);
        } else if(password.isEmpty()){
            showError(getString(R.string.fields_cannot_be_empty), emailInputLayout);
        } else if(email.isEmpty()) {
            showError(getString(R.string.fields_cannot_be_empty), passwordInputLayout);
        }
    }

    private void satisfyDependencies() {
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    private void doLogin(String email, String password) {
        showProgress(true);
        AuthenticationApiInterface authenticationApiInterface = apiClient.createService(AuthenticationApiInterface.class, email, password);
        Call<Map<String, Object>> call = authenticationApiInterface.getToken();
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    openStartPageActivity(response);
                } else {
                    showProgress(false);
                    loginErrorMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                showProgress(false);
                loginErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showProgress(boolean show) {
        if(show) {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    private void openStartPageActivity(Response<Map<String, Object>> response) {
        prefs.setSessionToken((String) response.body().get("session"));
        prefs.setUserId(((Double)response.body().get("id")).longValue());
        prefs.setIsVet((boolean) response.body().get("isVet"));
        Intent i = new Intent(getActivity(), StartPageActivity.class);
        startActivity(i);
    }

    private void setDefaultFontForPasswordField() {
        passwordEditText.setTypeface(Typeface.DEFAULT);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
    }

    @OnFocusChange(R.id.email)
    void validateEmail() {
        if (!emailEditText.hasFocus()) {
            if (!FormValidator.validateEmail(emailEditText.getText().toString())) {
                showError(getResources().getString(R.string.invalid_email), emailInputLayout);
            } else {
                emailInputLayout.setErrorEnabled(false);
            }
        }
    }

    private void showError(String errorText, TextInputLayout inputLayout) {
        inputLayout.setEnabled(true);
        inputLayout.setError(errorText);
    }

}
