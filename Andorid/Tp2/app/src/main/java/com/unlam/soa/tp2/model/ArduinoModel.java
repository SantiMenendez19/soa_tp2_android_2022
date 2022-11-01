package com.unlam.soa.tp2.model;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.unlam.soa.tp2.entities.Constants;
import com.unlam.soa.tp2.entities.CustomPermission;
import com.unlam.soa.tp2.interfaces.BluetoothClickListener;
import com.unlam.soa.tp2.interfaces.Model;
import com.unlam.soa.tp2.presenter.ArduinoPresenter;

import java.util.ArrayList;
import java.util.Set;

public class ArduinoModel implements Model {

    private final ArduinoPresenter presenter;
    private BluetoothAdapter btAdapter;
    private ArrayList<BluetoothDevice> btDeviceList = new ArrayList<>();

    public ArduinoModel(ArduinoPresenter presenter) {
        this.presenter = presenter;
        if (checkPermissions()) {
            initializeBluetooth();
        }
    }

    public void initializeBluetooth() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            this.presenter.setBtUnavailable();
            return;
        }
        updateDevicesInfo();
    }

    public void updateDevicesInfo() {
        btDeviceList = new ArrayList<>();
        if (btAdapter.isEnabled()) {
            if (checkPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                btDeviceList.addAll(pairedDevices);
            } else {
                this.presenter.showWarning("Faltan Permisos: No se puede obtener el listado de dipositivos");
            }
        }
        this.presenter.setBtAvailable(btAdapter.isEnabled(), btDeviceList);
    }

    @Override
    public void onDestroy() {

    }

    private boolean checkPermissions() {
        int sdk = Build.VERSION.SDK_INT;
        ArrayList<String> missingPermissions = new ArrayList<>();
        for (CustomPermission permission : Constants.BT_PERMISSIONS_NEEDED) {
            permission.granted = sdk <= permission.minSdk || this.presenter.checkPermission(permission.name);
            if (!permission.granted) {
                missingPermissions.add(permission.name);
            }
        }
        if (missingPermissions.isEmpty()) {
            return true;
        }
        this.presenter.requestPermission(missingPermissions);
        return false;
    }

    private boolean checkPermission(String permission) {
        CustomPermission customPermission = Constants.getBTPermission(permission);
        return customPermission != null && customPermission.granted;
    }

    public void activateBluetooth() {
        try {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.presenter.openActivity(enableBtIntent, Constants.BT_CONNECT_REQUEST);
        } catch (Exception ex) {
            this.presenter.showError("Error al activar Bluetooth");
        }
    }

    public void deactivateBluetooth() {
        if(checkPermission(Manifest.permission.BLUETOOTH_CONNECT)){
            btAdapter.disable();
            this.presenter.showWarning("Desactivando Bluetooth..");
            return;
        }
        this.presenter.showWarning("Faltan Permisos: No se desactivar el bluetooth");
    }
    public void openBtSettings(){
        Intent openBtIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        this.presenter.openActivity(openBtIntent,null);
    }
    public void connectDevice(int position){
        this.presenter.showWarning("Todavia no implementado");
    }


}
