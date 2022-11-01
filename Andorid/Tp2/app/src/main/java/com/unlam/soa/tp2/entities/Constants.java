package com.unlam.soa.tp2.entities;

import android.Manifest;

public class Constants {

    public static final CustomPermission[] BT_PERMISSIONS_NEEDED ={
            new CustomPermission(Manifest.permission.BLUETOOTH, 1),
            new CustomPermission(Manifest.permission.BLUETOOTH_CONNECT, 31),
            new CustomPermission(Manifest.permission.BLUETOOTH_ADMIN, 1)

    };
    public static final String BT_CONNECT_REQUEST = "BT_CONNECT";
    public static final int MAX_POWER_METER = 1024;
    public static final int SECONDS_FIVE = 5000;

    public static CustomPermission getBTPermission(String permission){
        for (CustomPermission needed:BT_PERMISSIONS_NEEDED) {
            if(needed.name.equals(permission)){
                return needed;
            }
        }
        return null;
    }
}
