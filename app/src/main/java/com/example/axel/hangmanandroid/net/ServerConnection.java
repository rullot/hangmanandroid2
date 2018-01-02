package com.example.axel.hangmanandroid.net;

/**
 * Created by axel on 2017-12-29.
 */

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

    /**
     *
     * @author axel
     */
    public class ServerConnection implements Runnable {

        private static final int PORT = 6970;
        private static final String HOST_ADDRESS = "10.0.2.2";
        private static final int TIMEOUT = 150000;
        private Socket socket;
        private DataOutputStream toServer;
        private DataInputStream fromServer;
        private boolean connected;
        private Handler outputHandler;

        public void connect(Handler outputHandler) {
            try {
                this.socket = new Socket(HOST_ADDRESS, PORT);
                this.socket.setSoTimeout(TIMEOUT);
                this.fromServer = new DataInputStream(socket.getInputStream());
                this.toServer = new DataOutputStream(socket.getOutputStream());
                this.connected = true;

            } catch (IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void disconnect() {
            try {
                this.socket.close();
                this.connected = false;
                this.socket = null;
            } catch (IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void send(String input) {
            try {
                this.toServer.writeUTF(input);
                this.toServer.flush();
            } catch (IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void createListener(Handler handler){
            new Thread(new Listener(handler)).start();
        }

        public void run() {
        }

        private class Listener implements Runnable {

            private final Handler out;

            private Listener(Handler out) {
                this.out = out;
            }

            @Override
            public void run() {
                try {
                    while (true) {
                        String reply = (String) fromServer.readUTF();
                        Bundle bundle = new Bundle();
                        Message msg = new Message();
                        bundle.putString("KEY", reply);
                        msg.setData(bundle);
                        out.handleMessage(msg);
                    }
                } catch (IOException ex) {
                    if (connected) {
                        Bundle bundle = new Bundle();
                        Message msg = new Message();
                        bundle.putString("KEY", "CONNECTION LOST");
                        msg.setData(bundle);
                        out.handleMessage(msg);
                    }
                }
            }
        }
    }
