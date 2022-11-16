package com.unlam.soa.tp2.model;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.unlam.soa.tp2.entities.Constants;
import com.unlam.soa.tp2.entities.CustomPermission;
import com.unlam.soa.tp2.backgroundtask.BtCommunicationThread;
import com.unlam.soa.tp2.interfaces.IModel;
import com.unlam.soa.tp2.presenter.BtCommunicationPresenter;

import java.io.IOException;

public class BtCommunicationModel implements IModel {
    private final BtCommunicationPresenter presenter;
    private final BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;
    private BtCommunicationThread btCommunicationThread;
    private final Handler handler;

    public BtCommunicationModel(BtCommunicationPresenter presenter) {
        this.presenter = presenter;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d("HiloPrincipal","Principal id:"+ Thread.currentThread().getId());
        handler = new Handler(message -> {
                Bundle bundle = message.getData();
                String msg = bundle.getString(Constants.MESSAGE_POWER);
                presenter.notifyReading(msg);
            return true;
        }
        );

    }

    public void createConnection(String deviceMacAddress) {
        BluetoothDevice device = btAdapter.getRemoteDevice(deviceMacAddress);
        try {
            if(!checkPermission(Manifest.permission.BLUETOOTH_CONNECT)){
                this.presenter.showError("Faltan Permisos: No se puede conectar el dipositivo");
            }
            btSocket = device.createRfcommSocketToServiceRecord(Constants.BT_MODULE_UUID);
            btSocket.connect();
            btCommunicationThread = new BtCommunicationThread(btSocket,handler);
            //btCommunicationThread.bluetoothRunnableNotification = bluetoothThreadNotification;
            btCommunicationThread.start();

        } catch (IOException e) {
            this.presenter.showError("Error: No se puede conectar al dispositivo "+device.getName());
            this.presenter.notifyError(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        }

    }
    public void sendManualAction(){
        try {
            btCommunicationThread.write(Constants.ARDUINO_MANUAL_ACTION);
            presenter.showSuccess("Acción Enviada");
        }catch (IOException e) {
            presenter.showError("Error: No se pudo enviar la acción");
        }
    }
    public void closeConnection(){
        try {
                btSocket.close();
        }catch (IOException ex){
            presenter.showError("Error al cerrar la conexión");
        }

    }
    private boolean checkPermission(String permission){
        CustomPermission customPermission = Constants.getBTPermission(permission);
        return customPermission!=null && customPermission.granted;
     }

    @Override
    public void onDestroy() {
        try {
            btSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
