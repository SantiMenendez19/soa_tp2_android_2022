package com.unlam.soa.tp2.presenter;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import com.unlam.soa.tp2.R;
import com.unlam.soa.tp2.interfaces.IPresenter;
import com.unlam.soa.tp2.interfaces.IView;

public abstract class BasePresenter implements IPresenter {
    ConstraintLayout constraintLayout;
    IView view;

    private final int colorSuccess;
    private final int colorError;
    private final int colorWithe;
    private final int colorWarning;
    public BasePresenter(IView view, ConstraintLayout constraintLayout){
        this.view = view;
        this.constraintLayout =constraintLayout;
        this.colorSuccess = this.getColor(R.color.success);
        this.colorWithe =this.getColor(R.color.white);
        this.colorError = this.getColor(R.color.error);
        this.colorWarning = this.getColor(R.color.warning);
    }

    public void showSuccess(String message){
        Snackbar snackbar = getSnackbar(message);
        snackbar.setBackgroundTint(colorSuccess).show();
    }
    public void showError(String message){
        Snackbar snackbar = getSnackbar(message);
        snackbar.setBackgroundTint(colorError).show();
    }
    public void showWarning(String message){
        Snackbar snackbar = getSnackbar(message);
        snackbar.setBackgroundTint(colorWarning).show();
    }

    private Snackbar getSnackbar(String message){
        Snackbar snackbar =  Snackbar.make(constraintLayout,message,Snackbar.LENGTH_LONG)
                .setTextColor(colorWithe)
                .setActionTextColor(colorWithe)
                .setAnchorView(R.id.bottom_navigation);
        snackbar.setAction(R.string.close,view->snackbar.dismiss());
        return snackbar;

    }
    private int getColor(int colorId){
        return view.getResourceColor(colorId);
    }
}
