package com.unlam.soa.tp2.entities;

import android.Manifest;

import java.util.UUID;

public class Constants {
    public static final CustomPermission[] BT_PERMISSIONS_NEEDED ={
            new CustomPermission(Manifest.permission.BLUETOOTH, 1),
            new CustomPermission(Manifest.permission.BLUETOOTH_CONNECT, 31),
            new CustomPermission(Manifest.permission.BLUETOOTH_ADMIN, 1)

    };
    public static final String BT_DEVICE_START_NAME ="HC-";
    public static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final String ARDUINO_MANUAL_ACTION ="A";
    public static final String ARDUINO_GET_INFO ="I";
    public static final String BT_END_OF_MESSAGE = "\r\n";
    public static final int BT_DELAY_THREAD = 1000;
    public static final String FRAGMENT_PAUSED = "FRAGMENT_PAUSED";
    public static final String FRAGMENT_RESUMED = "FRAGMENT_RESUMED";
    public static final int BT_READ_TIME_OUT=200;


    public static CustomPermission getBTPermission(String permission){
        for (CustomPermission needed:BT_PERMISSIONS_NEEDED) {
            if(needed.name.equals(permission)){
                return needed;
            }
        }
        return null;
    }
}
