package com.me.esztertoth.vetclinicapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;

public class DialogUtils {

    @Inject
    public DialogUtils() {
    }

    public void showErrorMessage(Context context, String message, String positiveButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(positiveButton, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void showWarningDialog(Context context, String title, String message, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveListener)
                .setNegativeButton(negativeButton, negativeListener)
                .show();
    }

}
