package com.me.esztertoth.vetclinicapp.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationConverter {

    public static LatLng getLocationFromAddress(String input, Context context) {

        Geocoder coder = new Geocoder(context, Locale.ENGLISH);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            address = coder.getFromLocationName(input, 5);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return resLatLng;
    }

}
