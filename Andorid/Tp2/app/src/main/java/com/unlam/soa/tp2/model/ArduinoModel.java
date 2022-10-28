package com.unlam.soa.tp2.model;


import com.unlam.soa.tp2.contract.Contract;
import com.unlam.soa.tp2.entities.backgroundtask.RandomNumberRunnable;

public class ArduinoModel implements Contract.Model {
    private RandomNumberRunnable randomNumberRunnable;
    Contract.Presenter presenter;
    public ArduinoModel(Contract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void doInBackground() {
        randomNumberRunnable = new RandomNumberRunnable(this);
        randomNumberRunnable.run();
    }

    @Override
    public void setInfo(String[] params) {
        presenter.setInfo(params);
    }

    @Override
    public void onDestroy() {
        randomNumberRunnable =null;
    }

    @Override
    public String getInfo(String[] params) {
        return "";
    }
}
