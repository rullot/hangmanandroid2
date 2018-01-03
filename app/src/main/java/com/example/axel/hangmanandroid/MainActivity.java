package com.example.axel.hangmanandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.axel.hangmanandroid.net.ServerConnection;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    TextView txt;
    private ServerConnection serverConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        txt = (TextView) findViewById(R.id.textView);
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle b = msg.getData();
                        String key = b.getString("KEY");
                        System.out.println(key);
                        txt.setText(key);
                    }
                });
            }
        };
        new ConnectServer().execute();
    }

    public void sendMessage(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        editText.setText(null);
        new SendToServer().execute(message);
    }

    private class ConnectServer extends AsyncTask<Void, Void, ServerConnection> {

        @Override
        protected ServerConnection doInBackground(Void...voids ) {
            ServerConnection serverConnection = new ServerConnection();
            serverConnection.connect(handler);
            serverConnection.createListener(handler);
            return serverConnection;
        }

        @Override
        protected void onPostExecute(ServerConnection serverConnection){
            MainActivity.this.serverConnection = serverConnection;
        }
    }

    private class SendToServer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            serverConnection.send(params[0]);
            return null;
        }
    }
}
