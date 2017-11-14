package com.me.esztertoth.vetclinicapp.utils;

import android.text.TextUtils;

public class FormValidator {

    public static boolean validateEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        return !TextUtils.isEmpty(password);
    }

    public static boolean checkIfSecondPasswordMatches(String password1, String password2) {
        return validatePassword(password1) && validatePassword(password2) && password2.equals(password1);
    }
}
