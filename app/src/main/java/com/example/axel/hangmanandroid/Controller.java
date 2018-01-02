package com.example.axel.hangmanandroid;

/**
 * Created by axel on 2018-01-02.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import android.os.Handler;

import com.example.axel.hangmanandroid.net.ServerConnection;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author axel
 */
public class Controller implements Runnable  {

    public static final ServerConnection serverConnection = new ServerConnection();

    public void connect(Handler outputHandler) {
        this.serverConnection.connect(outputHandler);
    }

    public void disconnect() {
        send("quit");
        this.serverConnection.disconnect();
    }

    public void send(String input) {
        this.serverConnection.send(input);
    }

    @Override
    public void run() {

    }
}
