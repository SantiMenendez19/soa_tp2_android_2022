package com.unlam.soa.tp2.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.contract.Contract;
import com.unlam.soa.tp2.entities.Constants;
import com.unlam.soa.tp2.presenter.AndroidPresenter;

public class AndroidActivity extends AppCompatActivity implements Contract.View {
    Contract.Presenter presenter;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);
        this.constraintLayout = findViewById(R.id.constraintLayoutAnd);
        this.presenter = new AndroidPresenter(this,constraintLayout);

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
    }

    @Override
    public String getInfo(String[] params) {
        switch (params[0]){
            case Constants.RESOURCE_COLOR:
                int idColor = Integer.parseInt(params[1]);
                int resource = getResources().getColor(idColor);
                return  Integer.toString(resource);
        }
        return "";
    }

    @Override
    public void setInfo(String[] params) {
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        this.presenter.onDestroy();
    }
}