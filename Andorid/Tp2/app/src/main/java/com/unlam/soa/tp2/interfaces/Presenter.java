package com.unlam.soa.tp2.interfaces;

public interface Presenter {
    void showSuccess(String message);
    void showError(String message);
    void showWarning(String message);
    void onDestroy();
}
