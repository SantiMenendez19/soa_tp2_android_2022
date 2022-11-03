package com.unlam.soa.tp1_2.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unlam.soa.tp1_2.R;
import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.entities.Constants;
import com.unlam.soa.tp1_2.entities.location.CustomLatLng;
import com.unlam.soa.tp1_2.presenter.AndroidPresenter;

public class AndroidActivity extends AppCompatActivity implements Contract.View {
    Contract.Presenter presenter;
    ConstraintLayout constraintLayout;
    ConstraintLayout layoutMap;
    ImageView imgMap;
    ImageView imgPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);
        this.imgMap = findViewById(R.id.imgMap);
        this.imgPin = findViewById(R.id.imgPin);
        this.constraintLayout = findViewById(R.id.constraintLayoutAnd);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.presenter = new AndroidPresenter(this,constraintLayout,locationManager,this);
        this.layoutMap = findViewById(R.id.layoutMap);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.android);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.arduino:
                    startActivity(new Intent(getApplicationContext(), ArduinoActivity.class));
                    overridePendingTransition(0,0);

                    return true;
                case R.id.android:
                    return true;
            }
            return false;
        });
        this.presenter.starBackgroundTask();
    }

    @Override
    public String getInfo(String[] params) {
        switch (params[0]){
            case Constants.RESOURCE_COLOR:
                int idColor = Integer.parseInt(params[1]);
                int resource = getResources().getColor(idColor);
                return  Integer.toString(resource);
            case Constants.MAP_WIDTH:
                int width = this.imgMap.getMeasuredWidth();
                return Integer.toString(width);
            case Constants.MAP_HEIGHT:
                int height = this.imgMap.getMeasuredHeight();
                return Integer.toString(height);
        }
        return "";
    }

    @Override
    public void setInfo(String[] params) {
        switch (params[0]){
            case Constants.NEW_LOCATION_TRANSFORM:
                int lat = Integer.parseInt(params[1]);
                int lng = Integer.parseInt(params[2]);
                this.updateLocation(new CustomLatLng(lat,lng));
            break;
            case Constants.ERROR_MESSAGE:
                this.imgPin.setVisibility(View.INVISIBLE);
                break;
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        this.presenter.onDestroy();

    }
    private void updateLocation(CustomLatLng latLng){
        int x = latLng.longitude;
        int y = latLng.latitude;
        this.imgPin.setVisibility(View.VISIBLE);
        this.imgPin.getLayoutParams();
        int widthPin= this.imgPin.getMeasuredWidth();
        int heightPin = this.imgPin.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.imgPin.getLayoutParams();
        layoutParams.leftMargin = x-widthPin/2;
        layoutParams.topMargin = y-heightPin;
        this.imgPin.setLayoutParams(layoutParams);
    }
}