package com.me.esztertoth.vetclinicapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.me.esztertoth.vetclinicapp.model.Clinic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VetClinicPreferences {

    private static final String PERIMETER = "perimeter";
    private static final String FAVORITES = "favorites";

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

    public static void saveFavoriteClinics(Context context, List<Clinic> favorites) {
        SharedPreferences.Editor edit = getPreferences(context).edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        edit.putString(FAVORITES, jsonFavorites);
        edit.commit();
    }

    public static ArrayList<Clinic> getFavoriteClinics(Context context) {
        List<Clinic> favorites;
        if (getPreferences(context).contains(FAVORITES)) {
            String jsonFavorites = getPreferences(context).getString(FAVORITES, null);
            Gson gson = new Gson();
            Clinic[] favoriteItems = gson.fromJson(jsonFavorites, Clinic[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else {
            return new ArrayList<>();
        }

        return (ArrayList<Clinic>) favorites;
    }

}
