package com.unlam.soa.tp1_2.entities.location;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.entities.Constants;

public class CustomLocationListener implements LocationListener {
    private final Contract.Model model;
    public CustomLocationListener(Contract.Model model) {
        this.model =model;
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latLong = new LatLng(location.getLatitude(),location.getLongitude());
        model.setInfo(new String[]{Constants.NEW_LOCATION, Double.toString(latLong.latitude),Double.toString(latLong.longitude)});
    }
}
