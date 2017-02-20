package com.me.esztertoth.vetclinicapp.loginAndSignUp;

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
import android.widget.Toast;

import com.me.esztertoth.vetclinicapp.R;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout emailInputLayout, passwordInputLayout, passwordAgainInputLayout;
    private EditText emailEditText, passwordEditText, passwordAgainEditText;
    private Button signUpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        emailInputLayout = (TextInputLayout) view.findViewById(R.id.email_textInput_layout);
        passwordInputLayout = (TextInputLayout) view.findViewById(R.id.password_textInput_layout);
        passwordAgainInputLayout = (TextInputLayout) view.findViewById(R.id.password_again_textInput_layout);

        emailEditText = (EditText) view.findViewById(R.id.email);
        passwordEditText = (EditText) view.findViewById(R.id.password);
        passwordAgainEditText = (EditText) view.findViewById(R.id.password_again);

        signUpButton = (Button) view.findViewById(R.id.signup_button);

        initUI();

        return view;
    }

    private void initUI() {
        setDefaultFontForPasswordField();
        signUpButton.setOnClickListener(this);
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

    private void signUp() {
        Toast.makeText(getActivity(), getString(R.string.signup), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_button:
                signUp();
                break;
            default:
                break;
        }
    }
}
