package com.unlam.soa.tp1_2.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.entities.Constants;
import com.unlam.soa.tp1_2.entities.location.CustomLocationListener;
import com.unlam.soa.tp1_2.entities.UnlamPolygon;
import com.unlam.soa.tp1_2.entities.backgroundtask.LocationTransformRunnable;


public class AndroidModel implements Contract.Model{
    Contract.Presenter presenter;
    UnlamPolygon unlamPolygon;
    CustomLocationListener locationListener;
    LatLng location;
    LocationManager locationManager;
    Context context;
    public AndroidModel(Contract.Presenter presenter,LocationManager locationManager,Context context){
        this.context = context;
        this.locationManager = locationManager;
        this.presenter = presenter;
        this.unlamPolygon = new UnlamPolygon();
        this.locationListener = new CustomLocationListener(this);
    }
    @Override
    public void doInBackground() {
        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gps_enabled){
            this.presenter.setInfo(new String[]{Constants.ERROR_MESSAGE,"Debe Activar la Ubicacion"});
            return;
        }
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.presenter.setInfo(new String[]{Constants.ERROR_MESSAGE,"Debe Brindar permisos de Ubicacion"});
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void setInfo(String[] params) {
        switch (params[0]){
            case Constants.NEW_LOCATION:
                double lat =Double.parseDouble(params[1]);
                double lng =Double.parseDouble(params[2]);
                location = new LatLng(lat,lng);
                LocationTransformRunnable transformRunnable = new LocationTransformRunnable(this,this.unlamPolygon,location);
                transformRunnable.run();
                break;
            case Constants.NEW_LOCATION_TRANSFORM:
                this.presenter.setInfo(params);
            case Constants.ERROR_MESSAGE:
                this.presenter.setInfo(params);
            default:

        }
    }

    @Override
    public void onDestroy() {
        this.locationListener =null;
    }

    @Override
    public String getInfo(String[] params) {
        return  this.presenter.getInfo(params);
    }

}
