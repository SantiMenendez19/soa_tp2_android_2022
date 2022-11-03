package com.unlam.soa.tp1_2.contract;

public interface Contract {
    interface View{
        String getInfo(String[] params);
        void setInfo(String[] params);
    }
    interface Model{
        void doInBackground();
        void setInfo(String[] params);
        void onDestroy();
        String getInfo(String[] params);
    }
    interface Presenter{
        void doAction();
        void setInfo(String[] params);
        void starBackgroundTask();
        void onDestroy();
        String getInfo(String[] params);
    }
}
