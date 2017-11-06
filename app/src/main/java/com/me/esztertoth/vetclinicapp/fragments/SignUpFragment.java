package com.me.esztertoth.vetclinicapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.me.esztertoth.vetclinicapp.App;
import com.me.esztertoth.vetclinicapp.LoginSignUpCallback;
import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.User;
import com.me.esztertoth.vetclinicapp.model.UserDTO;
import com.me.esztertoth.vetclinicapp.rest.ApiClient;
import com.me.esztertoth.vetclinicapp.rest.AuthenticationApiInterface;
import com.me.esztertoth.vetclinicapp.utils.LoginAndSignUpTextWatcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    @BindView(R.id.email_textInput_layout) TextInputLayout emailInputLayout;
    @BindView(R.id.password_textInput_layout) TextInputLayout passwordInputLayout;
    @BindView(R.id.password_again_textInput_layout) TextInputLayout passwordAgainInputLayout;
    @BindView(R.id.email) EditText emailEditText;
    @BindView(R.id.password) EditText passwordEditText;
    @BindView(R.id.password_again) EditText passwordAgainEditText;
    @BindView(R.id.radioButton_is_doctor_yes) RadioButton isVetButton;

    @Inject ApiClient apiClient;

    private String email;
    private String password;
    private boolean isVet;

    private AuthenticationApiInterface authenticationApiInterface;

    private LoginSignUpCallback callback;

    public SignUpFragment(){}

    public SignUpFragment(LoginSignUpCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        satisfyDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        authenticationApiInterface = apiClient.registerVet();
        initUI();
        return view;
    }

    private void satisfyDependencies() {
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
    }

    @OnClick(R.id.signup_button)
    public void signUp() {
        if(!passwordsMatch()) {
            showErrorInToast(getString(R.string.passwords_do_not_match));
        } else if(!isAllFieldsFilled()) {
            showErrorInToast(getString(R.string.fill_form_error));
        } else {
            UserDTO user = createUserFromData();
            if(isVet) {
                registerVet(user);
            } else {
                registerPetOwner(user);
            }
        }
    }

    private boolean isAllFieldsFilled() {
        return  emailEditText.getText() != null &&
                passwordEditText.getText() != null &&
                passwordAgainEditText.getText() != null;
    }

    private boolean passwordsMatch() {
        return passwordEditText.getText().toString().equals(passwordAgainEditText.getText().toString());
    }

    private void showErrorInToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    private UserDTO createUserFromData() {
        email = emailEditText.getText().toString();
        password = passwordAgainEditText.getText().toString();
        isVet = isVetButton.isChecked();

        UserDTO user = new UserDTO();
        user.setEmail(email);
        user.setPassword(password);

        return user;
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

    private void registerVet(UserDTO userDTO) {
        Call<ResponseBody> call = authenticationApiInterface.registerVet(userDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showErrorInToast("Success");
                    callback.openLoginFragment();
                } else {
                    showErrorInToast("Unsuccessful signup");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorInToast("No connection. Tr again later.");
            }
        });
    }

    private void registerPetOwner(UserDTO userDTO) {
        Call<ResponseBody> call = authenticationApiInterface.registerPetOwner(userDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showErrorInToast("Success");
                    callback.openLoginFragment();
                } else {
                    showErrorInToast("Unsuccessful signup");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorInToast("No connection. Tr again later.");
            }
        });
    }

}
