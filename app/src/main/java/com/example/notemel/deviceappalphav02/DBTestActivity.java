package com.example.notemel.deviceappalphav02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import db.DBHandler;

/**
 * Created by Melchior_S on 2016-09-05.
 */
public class DBTestActivity extends AppCompatActivity {

    private Button mBtn_ShowDeviceList;
    private Button mBtn_ShowData;
    private DBHandler mDbHandler = DBHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);


        mBtn_ShowDeviceList = (Button)findViewById(R.id.btn_showdevicelist);
        mBtn_ShowData = (Button) findViewById(R.id.btn_showdata);

        mBtn_ShowDeviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHandler.showDeviceList();
            }
        });

        mBtn_ShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHandler.showAllDeviceData();
            }
        });




    }
}
