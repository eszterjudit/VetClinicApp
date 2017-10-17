package com.me.esztertoth.vetclinicapp.utils;

import android.content.Context;

import com.me.esztertoth.vetclinicapp.model.Clinic;

import java.util.ArrayList;
import java.util.List;

public class FavoriteUtils {

    public static void addFavoriteClinic(Context context, Clinic clinic) {
        List<Clinic> favorites = VetClinicPreferences.getFavoriteClinics(context);
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(clinic);
        VetClinicPreferences.saveFavoriteClinics(context, favorites);
    }

    public static void removeFavorite(Context context, Clinic clinic) {
        ArrayList<Clinic> favorites = VetClinicPreferences.getFavoriteClinics(context);
        if (favorites != null) {
            favorites.remove(clinic);
            VetClinicPreferences.saveFavoriteClinics(context, favorites);
        }
    }

    public static boolean isClinicAlreadyInFavorites(Context context, Clinic clinic) {
        boolean check = false;
        List<Clinic> favorites = VetClinicPreferences.getFavoriteClinics(context);
        if (favorites != null) {
            for (Clinic favclinic : favorites) {
                if (favclinic.equals(clinic)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

}
