package com.unlam.soa.tp1_2.entities.backgroundtask;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.entities.Constants;
import com.unlam.soa.tp1_2.entities.location.CustomLatLng;
import com.unlam.soa.tp1_2.entities.UnlamPolygon;

public class LocationTransformRunnable implements Runnable {
    public LatLng point;
    private final UnlamPolygon unlamPolygon;
    private final Contract.Model model;

    public  LocationTransformRunnable(Contract.Model model, UnlamPolygon unlamPolygon, LatLng point){
        this.model= model;
        this.unlamPolygon = unlamPolygon;
        this.point = point;
    }
    @Override
    public void run() {
        try {
            if (!this.unlamPolygon.isReady) {
                return;
            }
            boolean inside = unlamPolygon.pointInside(this.point);
            if (!inside) {
                model.setInfo(new String[]{Constants.ERROR_MESSAGE, "La Ubicación No esta dentro del Rango"});
                return;
            }
            String widthStr = this.model.getInfo(new String[]{Constants.MAP_WIDTH});
            String heightStr = this.model.getInfo(new String[]{Constants.MAP_HEIGHT});
            int width = Integer.parseInt(widthStr);
            int height = Integer.parseInt(heightStr);
            CustomLatLng newLocation = unlamPolygon.transform(width, height, this.point);
            this.model.setInfo(new String[]{Constants.NEW_LOCATION_TRANSFORM,
                    Integer.toString(newLocation.latitude),
                    Integer.toString(newLocation.longitude)});
        }catch (Exception ex){
            Log.d("LocationThread",ex.getMessage());
        }
    }
}
