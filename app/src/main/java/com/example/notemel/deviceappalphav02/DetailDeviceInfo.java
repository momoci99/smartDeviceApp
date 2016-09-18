package com.example.notemel.deviceappalphav02;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import db.DBHandler;
import format.DBResultForm;
import fragment.SensorDataTableFragment;

/**
 * Created by Melchior_S on 2016-09-06.
 */
public class DetailDeviceInfo extends AppCompatActivity {


    private static SensorDataTableFragment mSensorDataTableFragment;

    TextView boardVerTV;
    String mSelectedDevice;
    String mIntentKey;
    Button loadDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentKey= this.getResources().getString(R.string.ToDetailDeviceInfo);
        mSelectedDevice = getIntent().getStringExtra(mIntentKey);

        setContentView(R.layout.activity_detaildeviceinfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mSelectedDevice + " Info");
        setSupportActionBar(toolbar);

        boardVerTV = (TextView)findViewById(R.id.tv_board_ver);

        loadDataBtn = (Button) findViewById(R.id.btn_loaddata) ;
        loadDataBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorDataTableFragment = new SensorDataTableFragment();
                mSensorDataTableFragment.setDeviceName(mSelectedDevice);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.sensordata_container,mSensorDataTableFragment);
                fragmentTransaction.commit();

            }
        });
    }
}
