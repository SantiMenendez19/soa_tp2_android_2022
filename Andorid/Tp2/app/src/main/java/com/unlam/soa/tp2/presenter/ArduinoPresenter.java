package com.unlam.soa.tp2.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.entities.Constants;
import com.unlam.soa.tp2.entities.CustomPermission;
import com.unlam.soa.tp2.interfaces.IBluetoothClickListener;
import com.unlam.soa.tp2.interfaces.IBluetoothDeviceConnection;
import com.unlam.soa.tp2.interfaces.IBluetoothItemClickListener;
import com.unlam.soa.tp2.model.ArduinoModel;
import com.unlam.soa.tp2.view.ArduinoActivity;
import com.unlam.soa.tp2.view.fragment.BtAvailableFragment;
import com.unlam.soa.tp2.view.fragment.BtCommunicationFragment;
import com.unlam.soa.tp2.view.fragment.BtUnavailableFragment;

import java.util.ArrayList;

public class ArduinoPresenter extends BasePresenter {
    private final FragmentManager fragmentManager;
    private final ArduinoModel model;
    private final ArduinoActivity activity;
    public String selectedMacAddress;
    private boolean communicationPaused=false;

    public ArduinoPresenter(ArduinoActivity activity, ConstraintLayout constraintLayout, FragmentManager fragmentManager) {
        super(activity,constraintLayout);
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.model = new ArduinoModel(this);

    }

    public void setBtUnavailableView(){
        BtUnavailableFragment fragment = new BtUnavailableFragment();
        replaceFragment(fragment);
    }
    public void setBtAvailableView(boolean btEnabled, ArrayList<BluetoothDevice> devices){
        BtAvailableFragment  fragment = BtAvailableFragment.newInstance(btEnabled,devices);
        fragment.bluetoothClickListener = bluetoothClickListener;
        fragment.bluetoothItemClickListener  = bluetoothItemClickListener;
        replaceFragment(fragment);
    }

    public void setBtCommunicationView(String deviceMacAddress){
        selectedMacAddress = deviceMacAddress;
        BtCommunicationFragment fragment = BtCommunicationFragment.newInstance(deviceMacAddress);
        fragment.bluetoothDeviceConnection = bluetoothDeviceConnection;
        replaceFragment(fragment);
    }

    public boolean checkPermission(String permission) {
        return this.activity.checkPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void openActivity(Intent intent){
        this.activity.fireActivity(intent);
    }
    public void onBluetoothAdapterChanges(String[] params){
         switch (params[0]){
             case BluetoothAdapter.ACTION_REQUEST_ENABLE:
             case BluetoothAdapter.ACTION_STATE_CHANGED:
             case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                 this.model.updateDevicesInfo();
                 break;
             case Constants.FRAGMENT_PAUSED:
                 this.communicationPaused=true;
                 break;
             case Constants.FRAGMENT_RESUMED:
                 communicationPaused = false;
                 break;
             case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                 if(communicationPaused){
                     return;
                 }
                 if(selectedMacAddress!=null && selectedMacAddress.equals(params[1])){
                    selectedMacAddress = null;
                    this.model.updateDevicesInfo();
                 }
                 break;
         }
    }

    public void requestPermission(ArrayList<String> permission){
        String[] missingPermission = permission.toArray(new String[0]);
        this.activity.requestPermission(missingPermission);
    }

    public void onPermissionChange(String[] permissions, int[] results) {
        boolean oneRejected = false;
        for (int i=0;i<=permissions.length;i++){
            CustomPermission customPermission = Constants.getBTPermission(permissions[i]);
            oneRejected = (oneRejected || results[i]!=PackageManager.PERMISSION_GRANTED);
            if(customPermission!=null){
                customPermission.granted = results[i]== PackageManager.PERMISSION_GRANTED;
            }
            if(oneRejected){
                showWarning("Algunas funcionalidades podrian no funcionar correctamente si no se aceptan todos los permisos");
                return;
            }
            this.model.initializeBluetooth();
        }
    }

    @Override
    public void onDestroy() {
        this.model.onDestroy();
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container,fragment);
        ft.commit();
    }


    public final IBluetoothClickListener bluetoothClickListener = new IBluetoothClickListener() {
        @Override
        public void onClickActivate() { model.activateBluetooth(); }

        @Override
        public void onCLickDeactivate() {
            model.deactivateBluetooth();
        }

        @Override
        public void onClickOpenSettings() {
            model.openBtSettings();
        }
    };

    private final IBluetoothItemClickListener bluetoothItemClickListener = new IBluetoothItemClickListener() {
        @Override
        public void onItemClick(int position) {
            model.connectDevice(position);
        }
    };

    private final IBluetoothDeviceConnection bluetoothDeviceConnection = params -> onBluetoothAdapterChanges(params);

}
