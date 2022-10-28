package com.unlam.soa.tp2.model;

import com.unlam.soa.tp2.contract.Contract;


public class AndroidModel implements Contract.Model {
    Contract.Presenter presenter;
    public AndroidModel(Contract.Presenter presenter){
        this.presenter = presenter;
    }
    @Override
    public void doInBackground() {

    }

    @Override
    public void setInfo(String[] params) {

    }

    @Override
    public void onDestroy() {


    }

    @Override
    public String getInfo(String[] params) {

        return  this.presenter.getInfo(params);
    }

}
