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
    private ServerChecker mServerChecker;

    private StatusReceiverHandler mStatusReceiverHandler = new StatusReceiverHandler();

    private Button btn_ServerCheck  ;
    private Switch sw_ServerConnect ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_config);
        mServerChecker = new ServerChecker(mStatusReceiverHandler);

        btn_ServerCheck  = (Button) findViewById(R.id.btn_server_check);
        sw_ServerConnect = (Switch) findViewById(R.id.sw_server_connect);


        btn_ServerCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mThreadManager.ActiveThread(mServerChecker);


            }
        });

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

    private void connectSwitchInit(boolean flag)
    {
        sw_ServerConnect.setChecked(flag);
    }

    private static class StatusReceiverHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 99:
                    //TODO:LISTVIEW 로 갱신시켜야지뭐...ㅅㅂ
                    break;
                default:
                    break;

            }
        }


    }
}
