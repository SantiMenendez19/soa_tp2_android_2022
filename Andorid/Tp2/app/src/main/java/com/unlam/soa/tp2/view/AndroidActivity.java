package com.unlam.soa.tp2.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private Sensor sensorGyroscope;

    private TextView gyroscopeX;
    private TextView gyroscopeY;
    private TextView gyroscopeZ;
    private Button button;
    private ImageView circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);
        this.constraintLayout = findViewById(R.id.constraintLayoutAnd);
        this.presenter = new AndroidPresenter(this,constraintLayout);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        button = findViewById(R.id.button);
        gyroscopeX = findViewById(R.id.gyroscopeX);
        gyroscopeY = findViewById(R.id.gyroscopeY);
        gyroscopeZ = findViewById(R.id.gyroscopeZ);

        circle = findViewById(R.id.circle);
        createBitMap();
        circle.setY(900);
        circle.setX(0);
        button.setOnClickListener(view -> resetCirclePosition());

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
        sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void resetCirclePosition() {
        circle.setY(900);
        circle.setX(550);
    }

    private void createBitMap() {
        Bitmap bitMap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        bitMap = bitMap.copy(bitMap.getConfig(), true);
        Canvas canvas = new Canvas(bitMap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(60, 60, 30, paint);
        circle.setImageBitmap(bitMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float gyroscopeX = sensorEvent.values[0];
        float gyroscopeY = sensorEvent.values[1];
        float gyroscopeZ = sensorEvent.values[2];

        if (gyroscopeX > 0) {
            circle.setY(circle.getY() + 50);
        }
        else if (gyroscopeX < 0) {
            circle.setY(circle.getY() - 50);
        }

        if (gyroscopeY > 0) {
            circle.setX(circle.getX() + 50);
        }
        else if (gyroscopeY < 0) {
            circle.setX(circle.getX() - 50);
        }

        this.gyroscopeX.setText(String.valueOf(gyroscopeX));
        this.gyroscopeY.setText(String.valueOf(gyroscopeY));
        this.gyroscopeZ.setText(String.valueOf(gyroscopeZ));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}