package com.example.notemel.deviceappalphav02;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import adapter.ConnectedDeviceListAdapter;
import bluetoothconnection.BluetoothConnectionMonitor;
import bluetoothconnection.BluetoothReconnector;
import system.ThreadManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MainActivity";
    static final int STATUS_UPDATE = 55;

    /*UI Components*/
    static ListView connectedDeiceListView;
    static ConnectedDeviceListAdapter listAdapter;

    private ThreadManager mThreadManager = ThreadManager.getInstance();
    private BluetoothConnectionMonitor mBlueToothConnectionMonitor = BluetoothConnectionMonitor.getInstance();
    private BluetoothReconnector mBluetoothReconnector = new BluetoothReconnector();


    static StatusReceiverHandler mStatusReceiverHandler = new StatusReceiverHandler();

    //모든 기기 목록(연결이 살아있든 죽어있든간에 일단 등록)
    static CopyOnWriteArrayList<String> mTotalDeviceList = new CopyOnWriteArrayList<>();

    //모든 기기 연결 상태 Table
    static ConcurrentHashMap<String, String> mConnectionStatusTable = new ConcurrentHashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        connectedDeiceListView = (ListView) findViewById(R.id.lv_connectionstatus);
        listAdapter = new ConnectedDeviceListAdapter(this);
        connectedDeiceListView.setAdapter(listAdapter);


        //모니터 쓰레드에 메인 액티비티 핸들러등록
        mBlueToothConnectionMonitor.setTargetActivityHandler(mStatusReceiverHandler);


        //커넥션 모니터 시작
        mThreadManager.ActiveThread(mBlueToothConnectionMonitor);


        //리커넥터 시작
        //mThreadManager.ActiveThread(mBluetoothReconnector);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this, DeviceSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        mStatusReceiverHandler.updateConnectedDeviceList();

    }
    private static class StatusReceiverHandler extends Handler
    {

        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what){
                case 0:
                    break;
                case STATUS_UPDATE:
                    copyData();
                    updateConnectedDeviceList();
                    break;
                default:
                    break;

            }
        }
        private void copyData() {
            for (int i = 0; i < BluetoothConnectionMonitor.getTotalDeviceList().size(); i++) {
                String MACAddress = BluetoothConnectionMonitor.getTotalDeviceList().get(i);
                mTotalDeviceList.add(MACAddress);
                mConnectionStatusTable.put(MACAddress,
                        BluetoothConnectionMonitor.getConnectionStatusTable().get(MACAddress));

            }
        }

        public void updateConnectedDeviceList() {
            if(mTotalDeviceList.size()>0)
            {
                listAdapter.setData(mTotalDeviceList, mConnectionStatusTable);
                listAdapter.notifyDataSetChanged();
            }

        }
    }

}
