package com.unlam.soa.tp2.presenter;

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
import com.unlam.soa.tp2.interfaces.BluetoothClickListener;
import com.unlam.soa.tp2.interfaces.BluetoothItemClickListener;
import com.unlam.soa.tp2.model.ArduinoModel;
import com.unlam.soa.tp2.view.ArduinoActivity;
import com.unlam.soa.tp2.view.fragment.BtAvailableFragment;
import com.unlam.soa.tp2.view.fragment.BtUnavailableFragment;

import java.util.ArrayList;

public class ArduinoPresenter extends BasePresenter {
    private final String TAG_BT_UNAVAILABLE = "BT_UNAVAILABLE";
    private final String TAG_BT_AVAILABLE = "BT_AVAILABLE";
    private final String TAG_BT_COMMUNICATION = "BT_COMMUNICATION";
    private final FragmentManager fragmentManager;
    private final ArduinoModel model;
    private final ArduinoActivity activity;

    public ArduinoPresenter(ArduinoActivity activity, ConstraintLayout constraintLayout, FragmentManager fragmentManager) {
        super(activity,constraintLayout);
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.model = new ArduinoModel(this);
    }

    public void setBtUnavailable(){
        BtUnavailableFragment fragment = new BtUnavailableFragment();
        replaceFragment(fragment,TAG_BT_UNAVAILABLE);
    }
    public void setBtAvailable(boolean btEnabled, ArrayList<BluetoothDevice> devices){
        BtAvailableFragment fragment = (BtAvailableFragment) fragmentManager.findFragmentByTag(TAG_BT_AVAILABLE);
        if(fragment==null){
            fragment = BtAvailableFragment.newInstance(btEnabled,devices);
            fragment.bluetoothClickListener = bluetoothClickListener;
            fragment.bluetoothItemClickListener  = bluetoothItemClickListener;
        }
        else{
            fragment.updateData(btEnabled,devices);
        }
        if(!fragment.isVisible()){
            replaceFragment(fragment,TAG_BT_AVAILABLE);
        }
    }

    public boolean checkPermission(String permission) {
        return this.activity.checkPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void openActivity(Intent intent, String action){
        this.activity.fireActivity(intent,action);
    }
    public void notify(String action){
         switch (action){
             case Constants.BT_CONNECT_REQUEST:
                 this.model.updateDevicesInfo();
                 break;
         }
    }

    public void requestPermission(ArrayList<String> permission){
        String[] missingPermission = permission.toArray(new String[permission.size()]);
        this.activity.requestPermission(missingPermission);
    }
    public void updatePermission(String[] permissions, int[] results) {
        boolean oneRejected = false;
        for (int i=0;i<=permissions.length;i++){
            CustomPermission customPermission = Constants.getBTPermission(permissions[i]);
            oneRejected = oneRejected && results[i]!=PackageManager.PERMISSION_GRANTED;
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
    private void replaceFragment(Fragment fragment,String tag){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container,fragment,tag);
        ft.commit();
    }
    public final BluetoothClickListener bluetoothClickListener = new BluetoothClickListener() {
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
    private final BluetoothItemClickListener bluetoothItemClickListener = new BluetoothItemClickListener() {
        @Override
        public void onItemClick(int position) {
            model.connectDevice(position);
        }
    };

}
