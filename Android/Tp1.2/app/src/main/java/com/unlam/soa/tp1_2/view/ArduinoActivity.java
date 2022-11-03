package com.unlam.soa.tp1_2.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.unlam.soa.tp1_2.R;
import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.entities.Constants;
import com.unlam.soa.tp1_2.presenter.ArduinoPresenter;

public class ArduinoActivity extends AppCompatActivity implements Contract.View {
    ConstraintLayout constraintLayout;
    Contract.Presenter presenter;
    TextInputEditText textInput;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);
        constraintLayout = findViewById(R.id.constraintLayoutArd);
        presenter = new ArduinoPresenter(this,constraintLayout);
        Button actionBtn = findViewById(R.id.btnAction);
        textInput = findViewById(R.id.text_power);
        presenter.starBackgroundTask();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.arduino);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.arduino:
                    return true;
                case R.id.android:
                    startActivity(new Intent(getApplicationContext(), AndroidActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
        actionBtn.setOnClickListener(view -> presenter.doAction());

    }

    @Override
    public String getInfo(String[] params){
        switch (params[0]){
            case Constants.RESOURCE_COLOR:
                int idColor = Integer.parseInt(params[1]);
                int resource = getResources().getColor(idColor);
                return  Integer.toString(resource);
        }
        return "";
    }
    @Override
    public void setInfo(String[] params){
        textInput.setText(params[0]);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.presenter.onDestroy();
    }

}