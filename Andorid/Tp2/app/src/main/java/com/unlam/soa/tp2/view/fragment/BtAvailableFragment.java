package com.unlam.soa.tp2.view.fragment;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.entities.BtDeviceListAdapter;
import com.unlam.soa.tp2.interfaces.IBluetoothClickListener;
import com.unlam.soa.tp2.interfaces.IBluetoothItemClickListener;

import java.util.ArrayList;

public class BtAvailableFragment extends Fragment {
    private static final String  ARG_BT_ENABLED ="BT_ENABLED";
    private static final String ARG_BT_DEVICE_LIST ="BT_DEVICE_LIST";

    private boolean btEnabled;
    private ArrayList<BluetoothDevice> btDeviceList;
    private Button btnBtActivate;
    private Button btnBtOpenSettings;
    public IBluetoothClickListener bluetoothClickListener;
    public IBluetoothItemClickListener bluetoothItemClickListener;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView rvBtDeviceList;


    public BtAvailableFragment() {

    }

   public static BtAvailableFragment newInstance(boolean btEnabled, ArrayList<BluetoothDevice> devices) {
        BtAvailableFragment fragment = new BtAvailableFragment();
        Bundle args = new Bundle();
        args.putBoolean(BtAvailableFragment.ARG_BT_ENABLED, btEnabled);
        args.putParcelableArrayList(BtAvailableFragment.ARG_BT_DEVICE_LIST, devices);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            btEnabled = getArguments().getBoolean(ARG_BT_ENABLED);
            btDeviceList = getArguments().getParcelableArrayList(ARG_BT_DEVICE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_bt_available, container, false);
        btnBtActivate = root.findViewById(R.id.btnBtActivate);
        btnBtOpenSettings = root.findViewById(R.id.btnBtOpenSettings);
        btnBtOpenSettings.setOnClickListener(view -> bluetoothClickListener.onClickOpenSettings());
        rvBtDeviceList = root.findViewById(R.id.rvBtDeviceList);
        this.layoutManager = new LinearLayoutManager(root.getContext());
        this.refreshData();
        return root;
    }

    private void refreshData(){
        if(btEnabled){
            btnBtActivate.setText(getString(R.string.bt_deactivated));
            btnBtActivate.setOnClickListener(view -> bluetoothClickListener.onCLickDeactivate());
        }
        else{
            btnBtActivate.setText(getString(R.string.bt_activate));
            btnBtActivate.setOnClickListener(view -> bluetoothClickListener.onClickActivate());
        }
        BtDeviceListAdapter btDeviceListAdapter =  new BtDeviceListAdapter(btDeviceList);
        btDeviceListAdapter.bluetoothItemClickListener = this.bluetoothItemClickListener;
        rvBtDeviceList.setLayoutManager(layoutManager);
        rvBtDeviceList.setAdapter(btDeviceListAdapter);
    }

}