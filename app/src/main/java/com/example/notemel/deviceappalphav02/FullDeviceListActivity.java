package com.example.notemel.deviceappalphav02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.concurrent.CopyOnWriteArrayList;

import adapter.FullDeviceListAdapter;
import db.DBCommander;

/**
 * Created by Melchior_S on 2016-09-07.
 */
public class FullDeviceListActivity extends AppCompatActivity {

    private static String TAG = "FullDeviceListActivity";
    private DBCommander mDBHandler = DBCommander.getInstance();
    private CopyOnWriteArrayList<String> mFullDeviceList = new CopyOnWriteArrayList<>();

    static ListView mFullDeviceListView;
    static FullDeviceListAdapter mFullDeviceListAdapter;
    private String mIntentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_device_list);
        mIntentKey = this.getResources().getString(R.string.ToDetailDeviceInfo);

        /*Init UI*/
        mFullDeviceListView = (ListView) findViewById(R.id.lv_full_device_list);
        mFullDeviceListAdapter = new FullDeviceListAdapter(this);
        mFullDeviceListView.setAdapter(mFullDeviceListAdapter);
        mFullDeviceListView.setOnItemClickListener(mItemClickListener);


        copyFullDeviceList();
        mFullDeviceListAdapter.setData(mFullDeviceList);
        mFullDeviceListAdapter.notifyDataSetChanged();

    }

    //Copy FullDeviceList From Monitor
    public void copyFullDeviceList() {
        CopyOnWriteArrayList<String> fullDeviceList = mDBHandler.getFullDeviceList();
        for (int i = 0; i < fullDeviceList.size(); i++) {
            mFullDeviceList.add(fullDeviceList.get(i));
        }

    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position) {

            Intent intent = new Intent(FullDeviceListActivity.this, DetailDeviceInfo.class);
            intent.putExtra(mIntentKey, mFullDeviceList.get(position));
            Log.e(TAG, mFullDeviceList.get(position));

            startActivity(intent);

        }
    };
}
