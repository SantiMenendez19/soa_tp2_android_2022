package com.unlam.soa.tp1_2.presenter;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import com.unlam.soa.tp1_2.R;
import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.entities.Constants;

public abstract class BasePresenter implements Contract.Presenter {
    ConstraintLayout constraintLayout;
    Contract.View view;
    Contract.Model model;
    private final int colorSuccess;
    private final int colorError;
    private final int colorWithe;
    public BasePresenter(Contract.View view,ConstraintLayout constraintLayout){
        this.view = view;
        this.constraintLayout =constraintLayout;
        this.colorSuccess = this.getColor(R.color.success);
        this.colorWithe =this.getColor(R.color.white);
        this.colorError = this.getColor(R.color.error);
    }

    public void showSuccess(String msg){
        Snackbar.make(constraintLayout, msg, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(colorSuccess)
                .setTextColor(colorWithe)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }
    public void showError(String msg){
        Snackbar.make(constraintLayout, msg, Snackbar.LENGTH_LONG)
                .setBackgroundTint(colorError)
                .setTextColor(colorWithe)
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }

    private int getColor(int colorId){
        String color = this.view.getInfo(new String[]{Constants.RESOURCE_COLOR, Integer.toString(colorId)});
        return Integer.parseInt(color);
    }
}
