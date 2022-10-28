package com.unlam.soa.tp2.presenter;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.unlam.soa.tp2.contract.Contract;
import com.unlam.soa.tp2.model.AndroidModel;

public class AndroidPresenter extends BasePresenter{
    Contract.Model model;
    public AndroidPresenter(Contract.View view, ConstraintLayout constraintLayout) {
        super(view, constraintLayout);
        this.model = new AndroidModel(this);
    }

    @Override
    public void doAction() {

    }

    @Override
    public void setInfo(String[] params) {

    }

    @Override
    public void starBackgroundTask() {
        this.model.doInBackground();
    }

    @Override
    public void onDestroy() {


    }

    @Override
    public String getInfo(String[] params) {
        return this.view.getInfo(params);
    }
}
