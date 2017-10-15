package com.me.esztertoth.vetclinicapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class VetClinicPreferences {

    private static final String PERIMETER = "perimeter";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("com.me.esztertoth.vetclinicapp", Context.MODE_PRIVATE);
    }

    public static int getPerimeter(Context context) {
        return getPreferences(context).getInt(PERIMETER, 5000);
    }

    public static void setPerimeter(Context context, int perimeter) {
        SharedPreferences.Editor edit = getPreferences(context).edit();
        edit.putInt(PERIMETER, perimeter);
        edit.commit();
    }


}
