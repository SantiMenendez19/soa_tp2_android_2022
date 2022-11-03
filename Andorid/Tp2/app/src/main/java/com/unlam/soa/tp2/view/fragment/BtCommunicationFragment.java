package com.unlam.soa.tp2.view.fragment;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.entities.Constants;
import com.unlam.soa.tp2.interfaces.IBluetoothDeviceConnection;
import com.unlam.soa.tp2.interfaces.IView;
import com.unlam.soa.tp2.presenter.BtCommunicationPresenter;

public class BtCommunicationFragment extends Fragment implements IView {

    private static final String ARG_BT_DV_MAC_ADDRESS = "ARG_BT_DV_MAC_ADDRESS";
    private String deviceMacAddress;
    public IBluetoothDeviceConnection bluetoothDeviceConnection;
    private BtCommunicationPresenter presenter;
    private TextView txtPowerMeter;
    private Button btnAction;
    private Button btnBack;
    private ConstraintLayout constraintLayout;

    public BtCommunicationFragment() {

    }
    public static BtCommunicationFragment newInstance(String deviceMacAddress) {
        BtCommunicationFragment fragment = new BtCommunicationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BT_DV_MAC_ADDRESS, deviceMacAddress);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceMacAddress = getArguments().getString(ARG_BT_DV_MAC_ADDRESS);
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
        bluetoothDeviceConnection.onChange(new String[]{Constants.FRAGMENT_PAUSED});
        this.presenter.stopCommunication();
    }
    @Override
    public void onResume(){
        super.onResume();
        bluetoothDeviceConnection.onChange(new String[]{Constants.FRAGMENT_RESUMED});
        this.presenter.startCommunication(deviceMacAddress);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bt_communication, container, false);
        txtPowerMeter = root.findViewById(R.id.txtPowerMeter);
        btnAction = root.findViewById(R.id.btnAction);
        btnAction.setOnClickListener(view->this.presenter.sendAction());
        btnBack = root.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view-> this.bluetoothDeviceConnection.onChange(new String[]{BluetoothDevice.ACTION_ACL_DISCONNECTED,deviceMacAddress}));
        constraintLayout = root.findViewById(R.id.btConstraintLayout);
        this.presenter = new BtCommunicationPresenter(this,constraintLayout);
        return root;
    }
    public void updateTextValue(String message){
        txtPowerMeter.setText(message);
    }
    @Override
    public int getResourceColor(int colorId) {
        return getResources().getColor(colorId);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        this.presenter.onDestroy();
    }
}
    