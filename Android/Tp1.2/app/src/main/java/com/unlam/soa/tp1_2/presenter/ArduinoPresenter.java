package com.unlam.soa.tp1_2.presenter;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.model.ArduinoModel;

public class ArduinoPresenter extends BasePresenter {
    public ArduinoPresenter(Contract.View view,ConstraintLayout constraintLayout) {
        super(view,constraintLayout);
        this.model = new ArduinoModel(this);
    }

    @Override
    public void doAction() {
        showSuccess( "TODO: Enviar comando de acción a Arduino");
    }

    @Override
    public void setInfo(String[] params) {
        view.setInfo(params);
    }

    @Override
    public void starBackgroundTask() {
        model.doInBackground();
    }

    @Override
    public void onDestroy() {
        this.model.onDestroy();
    }

    @Override
    public String getInfo(String[] params) {
        return null;
    }
}
