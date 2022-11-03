package com.unlam.soa.tp1_2.entities.backgroundtask;

import android.os.Handler;

import com.unlam.soa.tp1_2.contract.Contract;
import com.unlam.soa.tp1_2.entities.Constants;

import java.util.Random;

public class RandomNumberRunnable implements Runnable {
    private final Handler handlerTask;
    private final Contract.Model model;

    public RandomNumberRunnable(Contract.Model model){
        this.model=model;
        handlerTask = new Handler();
    }

    @Override
    public void run() {
        String data = getRandomNumber();
        model.setInfo(new String[]{data});
        handlerTask.postDelayed(this,Constants.SECONDS_FIVE);
    }
    private String getRandomNumber(){
        Random random = new Random();
        int power = random.nextInt(Constants.MAX_POWER_METER);
        return Integer.toString(power);
    }
}
