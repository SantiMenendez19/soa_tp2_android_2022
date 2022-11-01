package com.unlam.soa.tp2.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.interfaces.View;
import com.unlam.soa.tp2.presenter.AndroidPresenter;

public class AndroidActivity extends AppCompatActivity implements View {
    private final int NEEDED_PERMISSION =26;
    AndroidPresenter presenter;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);
        this.constraintLayout = findViewById(R.id.constraintLayoutAnd);
        this.presenter = new AndroidPresenter(this,constraintLayout);

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


}