package com.example.notemel.deviceappalphav02;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fragment.SensorDataTableFragment;

/**
 * Created by Melchior_S on 2016-09-06.
 */
public class DetailDeviceInfo extends AppCompatActivity {


    private SensorDataTableFragment mSensorDataTableFragment;
    private FragmentManager mFragmentManager ;

    TextView boardVerTV;
    String mSelectedDevice;
    String mIntentKey;
    Button loadDataBtn;

    private boolean mDoReload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentKey= this.getResources().getString(R.string.ToDetailDeviceInfo);
        mSelectedDevice = getIntent().getStringExtra(mIntentKey);

        setContentView(R.layout.activity_detaildeviceinfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mSelectedDevice + " Log");
        setSupportActionBar(toolbar);



        mFragmentManager = getFragmentManager();

        loadDataBtn = (Button) findViewById(R.id.btn_loaddata);

        mSensorDataTableFragment = new SensorDataTableFragment();
        mSensorDataTableFragment.setDeviceName(mSelectedDevice);



        loadDataBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                if(!mDoReload)
                {
                    fragmentTransaction.add(R.id.sensordata_container,mSensorDataTableFragment);
                    fragmentTransaction.commit();
                    mDoReload =true;
                }
                else
                {
                    fragmentTransaction.detach(mSensorDataTableFragment);
                    fragmentTransaction.attach(mSensorDataTableFragment);
                    fragmentTransaction.commit();
                }



            }
        });
    }
}
