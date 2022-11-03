package com.unlam.soa.tp1_2.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.unlam.soa.tp1_2.entities.location.CustomLatLng;

import java.util.ArrayList;
import java.util.List;

public class UnlamPolygon {
    private final List<LatLng> polygon = new ArrayList<>();
    private final LatLng west = new LatLng(-34.668405,-58.567452 );
    private final LatLng south =new LatLng(-34.671788,-58.562982);
    private final LatLng east = new LatLng(-34.669175,-58.560067 );
    private final  LatLng north =new LatLng(-34.665801,-58.564541 );
    private double sin;
    private double cos;
    private double distanceLng;
    private double distanceLat;
    public boolean isReady=false;

    public UnlamPolygon(){
        polygon.add(west);
        polygon.add(south);
        polygon.add(east);
        polygon.add(north);
        polygon.add(west);
        this.buildTransformation();
    }

    public boolean pointInside(LatLng point){
        return PolyUtil.containsLocation(point, polygon,false);
    }
    private void buildTransformation(){
        double gradient = (north.latitude- west.latitude) /(north.longitude- west.longitude);
        double angle =Math.atan(gradient);
        this.sin = Math.sin(-angle);
        this.cos = Math.cos(-angle);
        this.distanceLng = Math.sqrt(Math.pow((north.longitude- west.longitude),2) + (Math.pow((north.latitude- west.latitude),2)));
        this.distanceLat = Math.sqrt(Math.pow((south.longitude- west.longitude),2) + (Math.pow((south.latitude- west.latitude),2)));
        this.isReady=true;
    }
    public CustomLatLng transform(int width, int height, LatLng point){
        CustomLatLng newPoint= transformation(width,height,point);

        //Correccion
        CustomLatLng  refTransform= transformation(width,height, east);
        double lngCoef =  ((double) width-refTransform.longitude)/refTransform.latitude;
        double latCoef = ((double)height/refTransform.latitude)/refTransform.longitude;
        double lng = newPoint.longitude + (newPoint.latitude*lngCoef);
        double lat =newPoint.latitude +(newPoint.longitude * latCoef);
        return new CustomLatLng((int) lat,(int)lng);

    }
    private CustomLatLng transformation(int width, int height, LatLng point){
        double coefLng =width /distanceLng;
        double coefLat =height/distanceLat;
        //Rotacion
        double rotationLat = (point.longitude*this.sin) + (point.latitude*this.cos);
        double rotationLng = (point.longitude*this.cos) - (point.latitude*this.sin);
        double rotationLat0 = (this.west.longitude*this.sin) + (this.west.latitude*this.cos);
        double rotationLng0 = (this.west.longitude*this.cos) - (this.west.latitude*this.sin);
        //Deplazar a punto 0
        double latO = rotationLat -  rotationLat0;
        double lngO = rotationLng - rotationLng0;
        //cambiar estacala;
        double scaleLat = latO * coefLat;
        double scaleLng = lngO * coefLng;
        double latAbs = Math.abs(scaleLat);
        double lngAbs =Math.abs(scaleLng);
        return new CustomLatLng((int) latAbs,(int)lngAbs);
    }



}
