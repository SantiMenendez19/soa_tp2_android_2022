package com.unlam.soa.tp2.presenter;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.unlam.soa.tp2.model.BtCommunicationModel;
import com.unlam.soa.tp2.view.fragment.BtCommunicationFragment;

public class BtCommunicationPresenter extends BasePresenter {
    private final BtCommunicationFragment fragment;
    private final BtCommunicationModel model;
    private String deviceMacAddress;

    public BtCommunicationPresenter(BtCommunicationFragment fragment, ConstraintLayout constraintLayout) {
        super(fragment, constraintLayout);
        this.fragment = fragment;
        this.model = new BtCommunicationModel(this);
    }

    public void startCommunication(String deviceMacAddress){
        this.deviceMacAddress = deviceMacAddress;
        this.model.createConnection(deviceMacAddress);
    }
    public void stopCommunication(){
        this.model.closeConnection();
    }
    public void notifyError(String action){
        fragment.bluetoothDeviceConnection.onChange(new String[]{action, deviceMacAddress});
    }
    
    public void sendAction(){
        this.model.sendManualAction();
    }
    public void notifyReading(String message){
        fragment.updateTextValue(message);
    }
    @Override
    public void onDestroy() {
        this.model.onDestroy();
    }
}
