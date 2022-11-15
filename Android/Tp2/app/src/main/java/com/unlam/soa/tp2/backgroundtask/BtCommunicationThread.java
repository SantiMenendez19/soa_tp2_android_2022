package com.unlam.soa.tp2.backgroundtask;

import static java.lang.System.currentTimeMillis;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.unlam.soa.tp2.entities.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BtCommunicationThread extends  Thread {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Handler handlerTask;

    public BtCommunicationThread(BluetoothSocket btSocket, Handler handler) throws IOException {
            inputStream = btSocket.getInputStream();
            outputStream = btSocket.getOutputStream();
            handlerTask = handler;

    }

    @Override
    public void run(){
        try {
            write(Constants.ARDUINO_GET_INFO);
            StringBuilder message = new StringBuilder();
            long start = currentTimeMillis();
            boolean timeout =false;
            while (message.indexOf(Constants.BT_END_OF_MESSAGE) <0 && !timeout){
                byte[] buffer = new byte[256];
                int bytes;
                bytes = inputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                message.append(readMessage);
                timeout = (currentTimeMillis() - start) > Constants.BT_READ_TIME_OUT;
            }
            if(!timeout){
                Message msg = handlerTask.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MESSAGE_POWER,message.toString());
                msg.setData(bundle);
                Log.d("Hilo","Conexión BT hilo id:"+ this.getId());
                handlerTask.sendMessage(msg);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        handlerTask.postDelayed(this, Constants.BT_DELAY_THREAD);
    }
    public void write(String message) throws IOException {
        byte[] bufferMessage = message.getBytes();
        outputStream.write(bufferMessage);
    }
}
