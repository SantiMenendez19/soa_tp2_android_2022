package com.unlam.soa.tp2.entities.backgroundtask;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.unlam.soa.tp2.entities.Constants;
import com.unlam.soa.tp2.interfaces.IBluetoothRunnableNotification;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BtCommunicationRunnable implements Runnable {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    public IBluetoothRunnableNotification bluetoothRunnableNotification;
    private final Handler handlerTask;

    public BtCommunicationRunnable(BluetoothSocket btSocket) throws IOException {
            inputStream = btSocket.getInputStream();
            outputStream = btSocket.getOutputStream();
            handlerTask = new Handler();

    }
    public void run(){
        try {
            write(Constants.ARDUINO_GET_INFO);
            StringBuilder message = new StringBuilder();
            while (message.indexOf(Constants.BT_END_OF_MESSAGE) <0){
                byte[] buffer = new byte[256];
                int bytes;
                bytes = inputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                message.append(readMessage);
            }
                bluetoothRunnableNotification.notifyModel(message.toString());

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
