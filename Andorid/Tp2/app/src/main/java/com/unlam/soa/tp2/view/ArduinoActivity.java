package com.unlam.soa.tp2.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.entities.Constants;
import com.unlam.soa.tp2.interfaces.View;
import com.unlam.soa.tp2.presenter.ArduinoPresenter;

import java.util.ArrayList;

public class ArduinoActivity extends AppCompatActivity implements View {
    private final int BT_PERMISSION_REQUEST=25;
    ConstraintLayout constraintLayout;
    ArduinoPresenter presenter;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);
        constraintLayout = findViewById(R.id.constraintLayoutArd);
        FragmentManager fragmentManager = getSupportFragmentManager();
        presenter = new ArduinoPresenter(this,constraintLayout,fragmentManager);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.arduino);
        bottomNavigation.setOnItemSelectedListener(navBarItemSelectedListener);
        this.btRegisterReceiver();
    }

    @Override
    public int getResourceColor(int colorId) {
        return getResources().getColor(colorId);
    }

    public void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this,permissions,BT_PERMISSION_REQUEST);
    }

    public int checkPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this,permission);
    }

    public void fireActivity(Intent intent, String action) {
        if(Constants.BT_CONNECT_REQUEST.equals(action)){
            btActivityResultLauncher.launch(intent);
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
        this.presenter.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == BT_PERMISSION_REQUEST){
            this.presenter.updatePermission(permissions,grantResults);
        }
    }
    private void btRegisterReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver, filter);

    }
    private final ActivityResultLauncher<Intent> btActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    this.presenter.showWarning("Activando Bluetooth...");
                    this.presenter.notify(Constants.BT_CONNECT_REQUEST);
                }
            });
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action =intent.getAction();
            switch (action){
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    final int state =intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
                    if(state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_OFF){
                        presenter.notify(Constants.BT_CONNECT_REQUEST);
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                     presenter.notify(Constants.BT_CONNECT_REQUEST);
                    break;
            }
        }
    };
    private final NavigationBarView.OnItemSelectedListener navBarItemSelectedListener = item->{
        switch(item.getItemId()){
            case R.id.arduino:
                return true;
            case R.id.android:
                startActivity(new Intent(getApplicationContext(), AndroidActivity.class));
                overridePendingTransition(0,0);
                return true;
        }
        return false;
    };


}