package serverconnection;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class ServerConnectionHandler implements Runnable {

    private final long mSendInterval = 5000;
    private long pastTime = 0;
    private String pastTimeString;

    private HttpHandler mHttpURLConnection = new HttpHandler();



    private String mAndroidDeviceMACAddress;
    //현재 살아있는 장치 목록
    private static CopyOnWriteArrayList<String> mAliveDeviceList = new CopyOnWriteArrayList<>(); //현재 살아있는 장치 목록

    private volatile static ServerConnectionHandler objectInstance;

    private boolean sendFlag = false;

    public void setSendFlag(boolean flag)
    {
        this.sendFlag = flag;
    }

    public boolean getSendingStatus()
    {
        return this.sendFlag;
    }

    public static ServerConnectionHandler getInstance() {
        if (objectInstance == null) {
            synchronized (ServerConnectionHandler.class) {
                if (objectInstance == null) {
                    objectInstance = new ServerConnectionHandler();

                }
            }
        }
        return objectInstance;
    }

    public void setAndroidDeviceMACAddress(String mac) {
        mAndroidDeviceMACAddress = mac;
    }


    private JSONObject transactionCreator(String currentTime, String pastTime) {
        ArrayList<JSONObject> JSONObjectList = new ArrayList<>();
        ArrayList<ArrayList<DBResultForm>> dbResultList = new ArrayList<>();

        mAliveDeviceList = BluetoothConnectionMonitor.getAliveDeviceNameList();

        if (mAliveDeviceList.size() > 0) {

            for (int i = 0; i < mAliveDeviceList.size(); i++) {

                dbResultList.add(DBCommander.getSensorDataListTimeCondition(mAliveDeviceList.get(i), currentTime, pastTime));
            }

            System.out.println("가져온장치갯수 : " + dbResultList.size());
            for (int j = 0; j < dbResultList.size(); j++) {

                System.out.println("가져온장치의 리스트 크기 : " + dbResultList.get(j).size());
                for (int k = 0; k < dbResultList.get(j).size(); k++) {
                    JSONObjectList.add(JsonHandler.createJSONObjectSensorData(dbResultList.get(j).get(k)));
                }
            }
            return JsonHandler.createJSONObjectForServer(mAndroidDeviceMACAddress, JSONObjectList);
        } else {
            return null;
        }

    }

    @Override
    public void run() {

        while (true) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - pastTime > mSendInterval && sendFlag) {
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

    private class HttpHandler implements Runnable
    {
        private final String Token = "c94da62a7d8c4b4b0148b9738ede292b";
        private final String USER_AGENT = "Mozilla/5.0";

        private final String url = "http://hive.codefict.com/hive/data";

        private void sendPost(JSONObject parameters)  {

            URL obj = null;
            try {
                obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection ) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Token",Token);
                String urlParameters = parameters.toString();
                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\n con :"+con);
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {

        }
    }


}
