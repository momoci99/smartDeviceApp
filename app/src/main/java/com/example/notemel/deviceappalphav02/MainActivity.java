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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import adapter.ConnectedDeviceListAdapter;
import service.AlarmService;
import system.BluetoothConnectionMonitor;
import db.DBCommander;
import serverconnection.ServerConnectionHandler;
import system.ThreadManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "MainActivity";
    static final int STATUS_UPDATE = 55;
    private String mIntentKey;
    private String mServiceIntentKey;
    /*UI Components*/
    private ListView connectedDeiceListView;
    static ConnectedDeviceListAdapter listAdapter;
    private TextView tv_ConnectedDeviceList;
    private TextView tv_ServerStatus;

    private ThreadManager mThreadManager = ThreadManager.getInstance();
    private BluetoothConnectionMonitor mBlueToothConnectionMonitor = BluetoothConnectionMonitor.getInstance();
    //private BluetoothReconnector mBluetoothReconnector = new BluetoothReconnector();
    private ServerConnectionHandler mServerConnectionHandler = ServerConnectionHandler.getInstance();
    private static StatusReceiverHandler mStatusReceiverHandler = new StatusReceiverHandler();



    //모든 기기 목록 - MACAddress
    private static CopyOnWriteArrayList<String> mTotalDeviceList = new CopyOnWriteArrayList<>();

    //모든 기기 목록 - Name
    private static CopyOnWriteArrayList<String> mTotalDeviceNameList = new CopyOnWriteArrayList<>();

    //모든 기기 연결 상태 Table
    private static ConcurrentHashMap<String, String> mConnectionStatusTable = new ConcurrentHashMap<>();





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


        mIntentKey = this.getResources().getString(R.string.ToDetailDeviceInfo);
        mServiceIntentKey = this.getResources().getString(R.string.AlarmService_Key);

        tv_ConnectedDeviceList = (TextView)findViewById(R.id.tv_main_deviceList);
        tv_ServerStatus = (TextView)findViewById(R.id.tv_main_server_status);
        setTextUI(mTotalDeviceList.size());

        final String mac = getMacAddr();


        connectedDeiceListView = (ListView) findViewById(R.id.lv_connectionstatus);
        listAdapter = new ConnectedDeviceListAdapter(this);
        connectedDeiceListView.setAdapter(listAdapter);
        connectedDeiceListView.setOnItemClickListener(mItemClickListener);


        //모니터 쓰레드에 메인 액티비티 핸들러등록
        mBlueToothConnectionMonitor.initMonitor();
        mBlueToothConnectionMonitor.setTargetActivityHandler(mStatusReceiverHandler);


        //커넥션 모니터 시작
        mThreadManager.ActiveThread(mBlueToothConnectionMonitor);


        //ServerConnectionManager 시작
        mServerConnectionHandler.setAndroidDeviceMACAddress(mac);
        mThreadManager.ActiveThread(mServerConnectionHandler);


        DBCommander.InitDB(this);
        initService();
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

        if (id == R.id.ConnectSensor) {
            Intent intent = new Intent(MainActivity.this, DeviceSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this, ServerConfigActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this, FullDeviceListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_stats) {
            Intent intent = new Intent(MainActivity.this, ChartOptionSelectActivity.class);
            startActivity(intent);
        }  else if(id==R.id.nav_alert_config){
            Intent intent = new Intent(MainActivity.this, AlertConfigActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mStatusReceiverHandler.updateConnectedDeviceList();
        setTextUI(mTotalDeviceList.size());

    }

    private void initService()
    {
        Intent serviceIntent = new Intent(this, AlarmService.class);
        //serviceIntent.setAction(mServiceIntentKey);
        serviceIntent.setPackage("com.android.service");
        startService(serviceIntent);
    }

    private static class StatusReceiverHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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
            //Log.e(TAG,"기기목록갯수 beforeCopy : "+ mTotalDeviceList.size());

            //Prevent Duplication
            mTotalDeviceList.clear();
            mTotalDeviceNameList.clear();

            for (int i = 0; i < BluetoothConnectionMonitor.getTotalDeviceList().size(); i++) {
                String MACAddress = BluetoothConnectionMonitor.getTotalDeviceList().get(i);
                mTotalDeviceList.add(MACAddress);
                mConnectionStatusTable.put(MACAddress,
                        BluetoothConnectionMonitor.getConnectionStatusTable().get(MACAddress));
                mTotalDeviceNameList.add(BluetoothConnectionMonitor.getTotalDeviceNameList().get(i));

            }
            //Log.e(TAG,"기기목록갯수 endCopy : "+ mTotalDeviceList.size());
        }

        private void updateConnectedDeviceList() {
            //Log.e(TAG,"기기목록갯수 beforeUpdate : "+ mTotalDeviceList.size());
            if (mTotalDeviceList.size() > 0) {
                listAdapter.setData(mTotalDeviceList, mTotalDeviceNameList, mConnectionStatusTable);
                listAdapter.notifyDataSetChanged();

                //Log.e(TAG,"기기목록갯수 : "+ mTotalDeviceList.size());
            }
            //Log.e(TAG,"기기목록갯수 endUpdate : "+ mTotalDeviceList.size());

        }

    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position) {

            Intent intent = new Intent(MainActivity.this, DetailDeviceInfo.class);
            intent.putExtra(mIntentKey, mTotalDeviceNameList.get(position));
            Log.e(TAG, mTotalDeviceNameList.get(position));

            startActivity(intent);

        }
    };

    private static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }
    private void setTextUI(int count)
    {
        if(count == 0)
        {
            tv_ConnectedDeviceList.setText("Please Connect Sensor Device");
        }
        else
        {
            tv_ConnectedDeviceList.setText("Connected Device List");
        }

        if(mServerConnectionHandler.getSendingStatus())
        {
            tv_ServerStatus.setText("Sending");
        }
        else
        {
            tv_ServerStatus.setText("Suspend");
        }

    }
}
