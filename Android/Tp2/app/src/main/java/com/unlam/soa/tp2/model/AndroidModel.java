package com.unlam.soa.tp2.model;

import com.unlam.soa.tp2.interfaces.IModel;
import com.unlam.soa.tp2.presenter.AndroidPresenter;


public class AndroidModel implements IModel {

    AndroidPresenter presenter;

    public AndroidModel(AndroidPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {

    }
}
