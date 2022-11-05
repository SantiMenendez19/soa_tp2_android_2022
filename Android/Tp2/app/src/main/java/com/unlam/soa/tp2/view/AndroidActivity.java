package com.unlam.soa.tp2.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.interfaces.IView;
import com.unlam.soa.tp2.presenter.AndroidPresenter;

public class AndroidActivity extends AppCompatActivity implements IView, SensorEventListener {
    AndroidPresenter presenter;
    ConstraintLayout constraintLayout;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;

    private TextView accelerometerX;
    private TextView accelerometerY;
    private TextView accelerometerZ;
    private TextView positionX;
    private TextView positionY;
    private ImageView circle;

    private int height;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);
        this.constraintLayout = findViewById(R.id.constraintLayoutAnd);
        this.presenter = new AndroidPresenter(this,constraintLayout);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Button resetButton = findViewById(R.id.button);

        accelerometerX = findViewById(R.id.accelerometerX);
        accelerometerY = findViewById(R.id.accelerometerY);
        accelerometerZ = findViewById(R.id.accelerometerZ);

        positionX = findViewById(R.id.positionX);
        positionY = findViewById(R.id.positionY);

        circle = findViewById(R.id.circle);
        this.presenter.createBitMap(circle);

        width = this.presenter.getWidth();
        height = this.presenter.getHeight();

        resetButton.setOnClickListener(view -> this.presenter.resetCirclePosition(circle, width, height));

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.android);
        bottomNavigation.setOnItemSelectedListener(navBarItemSelectedListener);
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

    private final NavigationBarView.OnItemSelectedListener navBarItemSelectedListener = item->{
        switch(item.getItemId()){
            case R.id.arduino:
                startActivity(new Intent(getApplicationContext(), ArduinoActivity.class));
                overridePendingTransition(0,0);

                return true;
            case R.id.android:
                return true;
        }
        return false;
    };


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float accelerometerX = sensorEvent.values[0];
        float accelerometerY = sensorEvent.values[1];
        float accelerometerZ = sensorEvent.values[2];

        if (accelerometerY > 1 || accelerometerY < -1) {
            if (circle.getY() >= 0 && circle.getY() - 30 <= this.height) {
                circle.setY(circle.getY() + (5 * accelerometerY));
            }
            else {
                circle.setY(circle.getY() - (5 * accelerometerY));
            }
        }

        if (accelerometerX > 1 || accelerometerX < -1) {
            if (circle.getX() >= 0 && circle.getX() - 30 <= this.width) {
                circle.setX(circle.getX() - (5 * accelerometerX));
            }
            else {
                circle.setX(circle.getX() + (5 * accelerometerX));
            }
        }

        this.accelerometerX.setText(String.valueOf(accelerometerX));
        this.accelerometerY.setText(String.valueOf(accelerometerY));
        this.accelerometerZ.setText(String.valueOf(accelerometerZ));

        this.positionX.setText(String.valueOf(circle.getX()));
        this.positionY.setText(String.valueOf(circle.getY()));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}