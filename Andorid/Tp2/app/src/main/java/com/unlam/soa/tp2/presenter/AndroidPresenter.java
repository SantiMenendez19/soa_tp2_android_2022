package com.unlam.soa.tp2.presenter;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.unlam.soa.tp2.model.AndroidModel;
import com.unlam.soa.tp2.view.AndroidActivity;

public class AndroidPresenter extends BasePresenter{
    private final AndroidModel model;
    private final AndroidActivity activity;

    public AndroidPresenter(AndroidActivity activity, ConstraintLayout constraintLayout) {
        super(activity, constraintLayout);
        this.activity = activity;
        this.model = new AndroidModel(this);
    }

    @Override
    public void onDestroy() {
        this.model.onDestroy();
    }

}
