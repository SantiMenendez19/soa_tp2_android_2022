package com.unlam.soa.tp2.interfaces;

public interface IPresenter {
    void showSuccess(String message);
    void showError(String message);
    void showWarning(String message);
    void onDestroy();
}
