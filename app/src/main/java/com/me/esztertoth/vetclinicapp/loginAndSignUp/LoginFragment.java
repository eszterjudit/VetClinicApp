package com.me.esztertoth.vetclinicapp.loginAndSignUp;

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
import android.widget.Toast;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.StartPageActivity;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextInputLayout emailInputLayout, passwordInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = (EditText) view.findViewById(R.id.email);
        passwordEditText = (EditText) view.findViewById(R.id.password);
        emailInputLayout = (TextInputLayout) view.findViewById(R.id.email_textInput_layout);
        passwordInputLayout = (TextInputLayout) view.findViewById(R.id.password_textInput_layout);
        loginButton = (Button) view.findViewById(R.id.login_button);

        initUI();

        return view;
    }

    private void initUI() {
        setDefaultFontForPasswordField();
        loginButton.setOnClickListener(this);
        emailEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(emailEditText, emailInputLayout, getActivity()));
        passwordEditText.addTextChangedListener(new LoginAndSignUpTextWatcher(passwordEditText, passwordInputLayout, getActivity()));
    }

    private void setDefaultFontForPasswordField() {
        passwordEditText.setTypeface(Typeface.DEFAULT);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void login() {
        Intent i = new Intent(getActivity(), StartPageActivity.class);
        startActivity(i);
        //Toast.makeText(getActivity(), getString(R.string.login), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                login();
                break;
            default:
                break;
        }
    }
}
