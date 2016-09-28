package serverconnection;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import db.DBCommander;
import format.DBResultForm;
import json.JsonHandler;
import system.BluetoothConnectionMonitor;

/**
 * Created by Melchior_S on 2016-09-16.
 */
public class ServerDataHandler implements Runnable {

    private final long mSendInterval = 5000;
    private long pastTime = 0;
    private String pastTimeString;

    private HttpURLConnector mHttpURLConnection = new HttpURLConnector();
    private BluetoothConnectionMonitor mBluetoothConnectionMonitor = BluetoothConnectionMonitor.getInstance();
    private DBCommander mDBHandler = DBCommander.getInstance();
    private JsonHandler jsonHandler = new JsonHandler();

    private String mAndroidDeviceMACAddress;
    //현재 살아있는 장치 목록
    private static CopyOnWriteArrayList<String> mAliveDeviceList = new CopyOnWriteArrayList<String>(); //현재 살아있는 장치 목록

    private volatile static ServerDataHandler objectInstance;


    public static ServerDataHandler getInstance() {
        if (objectInstance == null) {
            synchronized (ServerDataHandler.class) {
                if (objectInstance == null) {
                    objectInstance = new ServerDataHandler();

                }
            }
        }
        return objectInstance;
    }

    public void setAndroidDeviceMACAddress(String mac) {
        mAndroidDeviceMACAddress = mac;
    }


    public JSONObject transactionCreator(String currentTime, String pastTime) {
        CopyOnWriteArrayList<JSONObject> JSONObjectList = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<CopyOnWriteArrayList<DBResultForm>> dbResultList = new CopyOnWriteArrayList<CopyOnWriteArrayList<DBResultForm>>();

        mAliveDeviceList = mBluetoothConnectionMonitor.getAliveDeviceNameList();

        if (mAliveDeviceList.size() > 0) {

            for (int i = 0; i < mAliveDeviceList.size(); i++) {

                dbResultList.add(mDBHandler.getSensorDataListTimeCondition(mAliveDeviceList.get(i), currentTime, pastTime));
            }

            System.out.println("가져온장치갯수 : " + dbResultList.size());
            for (int j = 0; j < dbResultList.size(); j++) {

                System.out.println("가져온장치의 리스트 크기 : " + dbResultList.get(j).size());
                for (int k = 0; k < dbResultList.get(j).size(); k++) {
                    JSONObjectList.add(jsonHandler.createJSONObjectSensorData(dbResultList.get(j).get(k)));
                }
            }
            return jsonHandler.createJSONObjectForServer(mAndroidDeviceMACAddress, JSONObjectList);
        } else {
            return null;
        }

    }

    @Override
    public void run() {

        while (true) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - pastTime > mSendInterval) {
                Calendar calendar = Calendar.getInstance();
                java.util.Date date = calendar.getTime();
                String todayTimeString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(date));


                JSONObject transactionJSONObject = transactionCreator(todayTimeString, pastTimeString);
                if (transactionJSONObject != null) {
                    mHttpURLConnection.sendPost(transactionJSONObject);
                }

                pastTime = currentTime;
                pastTimeString = todayTimeString;
            }

        }
    }
}
