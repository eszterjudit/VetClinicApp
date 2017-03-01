package com.me.esztertoth.vetclinicapp.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.utils.FormValidator;

public class LoginAndSignUpTextWatcher implements TextWatcher {

    private EditText editText;
    private TextInputLayout textInputLayout;
    private Context context;

    private String chosenPassword;

    public LoginAndSignUpTextWatcher(EditText editText, TextInputLayout textInputLayout, Context context) {
        this.editText = editText;
        this.textInputLayout = textInputLayout;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        switch(editText.getId()) {
            case R.id.email:
                if(!FormValidator.validateEmail(s.toString())) {
                    showError(context.getResources().getString(R.string.invalid_email));
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
                break;
            case R.id.password:
                if(!FormValidator.validatePassword(s.toString())) {
                    showError(context.getResources().getString(R.string.empty_password));
                } else {
                    textInputLayout.setErrorEnabled(false);
                    chosenPassword = s.toString();
                }
                break;
            case R.id.password_again:
                if(!FormValidator.validatePassword(s.toString())) {
                    showError(context.getResources().getString(R.string.empty_password));
                } else if(!FormValidator.checkIfSecondPasswordMatches(s.toString(),chosenPassword)) {
                    showError(context.getResources().getString(R.string.passwords_do_not_match));
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
                break;
            default:
                break;
        }
    }

    private void showError(String errorText) {
        textInputLayout.setEnabled(true);
        textInputLayout.setError(errorText);
    }

}
