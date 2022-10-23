package com.unlam.soa.tp1_2.presenter;

import android.content.Context;
import android.location.LocationManager;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.entities.Constants;
import com.unlam.soa.tp1_2.model.AndroidModel;

public class AndroidPresenter extends BasePresenter{
    Contract.Model model;
    public AndroidPresenter(Contract.View view, ConstraintLayout constraintLayout, LocationManager locationManager, Context context) {
        super(view, constraintLayout);
        this.model = new AndroidModel(this,locationManager,context);
    }

    @Override
    public void doAction() {

    }

    @Override
    public void setInfo(String[] params) {
        switch (params[0]){
            case Constants.ERROR_MESSAGE:
                this.showError(params[1]);
                this.view.setInfo(params);
                break;
            case Constants.NEW_LOCATION_TRANSFORM:
                this.view.setInfo(params);
                break;
        }

    }

    @Override
    public void starBackgroundTask() {
        this.model.doInBackground();
    }

    @Override
    public void onDestroy() {

        this.model.onDestroy();
    }

    @Override
    public String getInfo(String[] params) {
        return this.view.getInfo(params);
    }
}
