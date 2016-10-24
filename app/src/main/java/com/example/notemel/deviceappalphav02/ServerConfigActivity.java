package com.example.notemel.deviceappalphav02;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import serverconnection.ServerChecker;
import serverconnection.ServerConnectionHandler;
import system.BluetoothConnectionMonitor;
import system.ThreadManager;

/**
 * Created by Melchior_S on 2016-09-05.
 */
public class ServerConfigActivity extends AppCompatActivity {

    private static final String TAG = "ServerConfigActivity";


    private ThreadManager mThreadManager = ThreadManager.getInstance();
    private ServerConnectionHandler mServerConnectionHandler = ServerConnectionHandler.getInstance();


    private Button btn_ServerCheck;
    private Switch sw_ServerConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);



        sw_ServerConnect = (Switch) findViewById(R.id.sw_server_connect);


        initConnectSwitch();



        sw_ServerConnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mServerConnectionHandler.setSendFlag(true);
                    Toast.makeText(getApplicationContext(), "ConnectionResume", Toast.LENGTH_LONG).show();
                } else {
                    mServerConnectionHandler.setSendFlag(false);
                    Toast.makeText(getApplicationContext(), "ConnectionSuspend", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        initConnectSwitch();
        super.onResume();
    }

    private void initConnectSwitch() {
        sw_ServerConnect.setChecked(mServerConnectionHandler.getSendFlagStatus());
    }

}
