package com.unlam.soa.tp2.entities;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.interfaces.IBluetoothItemClickListener;

import java.util.ArrayList;

public class BtDeviceListAdapter extends RecyclerView.Adapter<BtDeviceListAdapter.BtDeviceViewHolder> {
    private final ArrayList<BluetoothDevice> deviceList;
    public IBluetoothItemClickListener bluetoothItemClickListener;

    public BtDeviceListAdapter(ArrayList<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public BtDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device_list, parent, false);
        return new BtDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BtDeviceViewHolder holder, int position) {
        BluetoothDevice device = deviceList.get(position);
        if (!checkPermission(Manifest.permission.BLUETOOTH_CONNECT)){
            return;
        }
        holder.txtName.setText(device.getName());
        holder.txtAddress.setText(device.getAddress());
        holder.itemLayout.setOnClickListener(view -> {
            if(bluetoothItemClickListener!=null){
                bluetoothItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList!=null? deviceList.size():0;
    }
    private boolean checkPermission(String permission){
        CustomPermission customPermission = Constants.getBTPermission(permission);
        return customPermission!=null && customPermission.granted;
    }
    public static class BtDeviceViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtAddress;
        public ConstraintLayout itemLayout;

        public BtDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.item_name);
            txtAddress = itemView.findViewById(R.id.item_address);
            itemLayout =itemView.findViewById(R.id.item_layout);
        }
    }
}
