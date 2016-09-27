package com.example.notemel.deviceappalphav02;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

import db.DBCommander;
import format.DBResultForm;
import json.JsonHandler;

/**
 * Created by Melchior_S on 2016-09-05.
 */
public class DBTestActivity extends AppCompatActivity {

    private Button mBtn_ShowDeviceList;
    private Button mBtn_ShowData;
    private Button mBtn_TestJson;

    private DBCommander mDbHandler = DBCommander.getInstance();
    private JsonHandler mJsonHandler = new JsonHandler();

    private CopyOnWriteArrayList<String> mFullDeviceList = new CopyOnWriteArrayList<>();

    CopyOnWriteArrayList<CopyOnWriteArrayList> allDeviceSensorData = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<DBResultForm> mDBResultFormList = new CopyOnWriteArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        WifiManager mng = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = mng.getConnectionInfo();
        final String mac = info.getMacAddress();




        mBtn_ShowDeviceList = (Button)findViewById(R.id.btn_showdevicelist);
        mBtn_ShowData = (Button) findViewById(R.id.btn_showdata);
        mBtn_TestJson = (Button) findViewById(R.id.btn_jsontest);

        mBtn_ShowDeviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDbHandler.showDeviceList();
            }
        });

        mBtn_ShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHandler.showAllDeviceData();
            }
        });

        mBtn_TestJson.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                mFullDeviceList = mDbHandler.getFullDeviceList();
                for(int i=0; i < mFullDeviceList.size(); i++)
                {
                    mDBResultFormList =mDbHandler.getSensorDataList(mFullDeviceList.get(i));
                    for(int j=0; j<mDBResultFormList.size(); j++)
                    {
                        JSONObject testJSONObject;
                        testJSONObject = mJsonHandler.createJSONObjectForServer(mac,mDBResultFormList.get(j));
                        Log.d("Test",testJSONObject.toString());
                    }

                }
            }
        });



    }
}
