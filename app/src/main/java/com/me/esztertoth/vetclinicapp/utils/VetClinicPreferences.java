package com.me.esztertoth.vetclinicapp.utils;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.me.esztertoth.vetclinicapp.model.Clinic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class VetClinicPreferences {

    private static final String PERIMETER = "perimeter";
    private static final String FAVORITES = "favorites";
    private static final String SESSION_TOKEN = "sessionToken";
    private static final String USER_ID = "userId";
    private static final String IS_VET = "isVet";

    private SharedPreferences sharedPreferences;

    @Inject
    public VetClinicPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getSessionToken() {
        return sharedPreferences.getString(SESSION_TOKEN, null);
    }

    public  void setSessionToken(String token) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(SESSION_TOKEN, token);
        edit.apply();
    }

    public long getUserId() {
        return sharedPreferences.getLong(USER_ID, 0);
    }

    public void setUserId(long id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(USER_ID, id);
        edit.apply();
    }

    public boolean getIsVet() {
        return sharedPreferences.getBoolean(IS_VET, false);
    }

    public void setIsVet(boolean isVet) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(IS_VET, isVet);
        edit.apply();
    }

    public int getPerimeter() {
        return sharedPreferences.getInt(PERIMETER, 10000);
    }

    public void setPerimeter(int perimeter) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(PERIMETER, perimeter);
        edit.apply();
    }

    public void saveFavoriteClinics(List<Clinic> favorites) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        edit.putString(FAVORITES, jsonFavorites);
        edit.apply();
    }

    public ArrayList<Clinic> getFavoriteClinics() {
        List<Clinic> favorites;
        if (sharedPreferences.contains(FAVORITES)) {
            String jsonFavorites = sharedPreferences.getString(FAVORITES, null);
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
