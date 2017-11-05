package com.me.esztertoth.vetclinicapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.me.esztertoth.vetclinicapp.model.Clinic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FavoriteUtils {

    private VetClinicPreferences prefs;

    @Inject
    public FavoriteUtils(Application application) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("com.me.esztertoth.vetclinicapp", Context.MODE_PRIVATE);
        this.prefs = new VetClinicPreferences(sharedPreferences);
    }

    public void addFavoriteClinic(Clinic clinic) {
        List<Clinic> favorites = prefs.getFavoriteClinics();
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(clinic);
        prefs.saveFavoriteClinics(favorites);
    }

    public void removeFavorite(Clinic clinic) {
        ArrayList<Clinic> favorites = prefs.getFavoriteClinics();
        if (favorites != null) {
            favorites.remove(clinic);
            prefs.saveFavoriteClinics(favorites);
        }
    }

    public boolean isClinicAlreadyInFavorites(Clinic clinic) {
        boolean check = false;
        List<Clinic> favorites = prefs.getFavoriteClinics();
        if (favorites != null) {
            for (Clinic favclinic : favorites) {
                if (favclinic.getId().equals(clinic.getId())) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    public void deleteAllFavorites() {
        List<Clinic> favorites = new ArrayList<>();
        prefs.saveFavoriteClinics(favorites);
    }

}
