package system;

import android.content.Context;
import android.util.Log;

import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;
import java.util.HashMap;

import db.DBCommander;
import json.JsonHandler;
import serverconnection.ServerConnectionHandler;

import static db.DBCommander.updateAlertConfigTable;

/**
 * Created by Melchior_S on 2016-10-20.
 */

public class AlertConfigHandler {

    private final String TAG = "AlertConfigHandler";

    private volatile static AlertConfigHandler objectInstance;
    public static AlertConfigHandler getInstance() {
        if (objectInstance == null) {
            synchronized (AlertConfigHandler.class) {
                if (objectInstance == null) {
                    objectInstance = new AlertConfigHandler();

                }
            }
        }
        return objectInstance;
    }

    private HashMap<String,Boolean> mAlertOptionHash = new HashMap<>();
    private HashMap<String,Float> mAlertDataHash = new HashMap<>();

    private String mSoundAlertEnable;
    private String mNotificationAlertEnable;
    private String mTemperatureEnable;
    private String mHumidityEnable;
    private String mOxygenEnable;
    private String mCOEnable;

    private String mCO_Level;
    private String mTemperature_Level;
    private String mOxygen_Level;
    private String mHumidity_Level;

    private Float mCO_LevelData;
    private Float mTemperature_LevelData;
    private Float mOxygen_LevelData;
    private Float mHumidity_LevelData;

    private ArrayList<String> mAlertOptionIndexList = new ArrayList<>();
    private ArrayList<String> mAlertConfigDataIndexList = new ArrayList<>();
    public void initConfig(Context context)
    {


        mSoundAlertEnable           = context.getString(R.string.SoundAlertEnable);
        mNotificationAlertEnable    = context.getString(R.string.NotificationAlertEnable);
        mTemperatureEnable          = context.getString(R.string.TemperatureEnable);
        mHumidityEnable             = context.getString(R.string.HumidityEnable);
        mOxygenEnable               = context.getString(R.string.OxygenEnable);
        mCOEnable                   = context.getString(R.string.COEnable);

        mCO_Level           = context.getString(R.string.CO_Level);
        mTemperature_Level  = context.getString(R.string.Temperature_Level);
        mOxygen_Level       = context.getString(R.string.Oxygen_Level);
        mHumidity_Level     = context.getString(R.string.Humidity_Level);


        //getDefault
        if(0==DBCommander.checkAlertConfigTableEmpty())
        {
            initDefaultData(context);
            Log.e(TAG,"result"+JsonHandler.getConfigJSONtoString(mAlertOptionHash,mAlertDataHash,mAlertOptionIndexList,mAlertConfigDataIndexList));
            //DBCommander.updateAlertConfigTable(JsonHandler.getConfigJSONtoString(mAlertOptionHash,mAlertDataHash,mAlertOptionIndexList,mAlertConfigDataIndexList));
        }
        else if(DBCommander.checkAlertConfigTableEmpty()>0)
        {
            Log.e(TAG,"From Data Base " + DBCommander.getAlertConfigData());
        }


        //데이터베이스에 미리 설정된 초기값이 없으면
        //초기값 생성.

        //초기값이 있으면
        //초기값을 읽어오고
        //읽은 초기값을 AlertConfigActivity에 출력

        //AlertConfigActivity의 데이터를 데이터 베이스에 저장
        //커넥션에서 데이터 검사하여 서비스에 브로드캐스트 전달.
    }
    private void initDefaultData(Context context)
    {
        mAlertOptionHash.put(mSoundAlertEnable,true);
        mAlertOptionHash.put(mNotificationAlertEnable,true);
        mAlertOptionHash.put(mTemperatureEnable,true);
        mAlertOptionHash.put(mHumidityEnable,true);
        mAlertOptionHash.put(mOxygenEnable,true);
        mAlertOptionHash.put(mCOEnable,true);

        //get Default Data
        mCO_LevelData           = Float.parseFloat(context.getString(R.string.CO_Level_Default));
        mTemperature_LevelData  = Float.parseFloat(context.getString(R.string.Temperature_Level_Default));
        mOxygen_LevelData       = Float.parseFloat(context.getString(R.string.Oxygen_Level_Default));
        mHumidity_LevelData     = Float.parseFloat(context.getString(R.string.Humidity_Level_Default));

        mAlertDataHash.put(mCO_Level,mCO_LevelData);
        mAlertDataHash.put(mTemperature_Level,mTemperature_LevelData);
        mAlertDataHash.put(mOxygen_Level,mOxygen_LevelData);
        mAlertDataHash.put(mHumidity_Level,mHumidity_LevelData);

        mAlertOptionIndexList.add(mSoundAlertEnable);
        mAlertOptionIndexList.add(mNotificationAlertEnable);
        mAlertOptionIndexList.add(mTemperatureEnable);
        mAlertOptionIndexList.add(mHumidityEnable);
        mAlertOptionIndexList.add(mOxygenEnable);
        mAlertOptionIndexList.add(mCOEnable);

        mAlertConfigDataIndexList.add(mTemperature_Level);
        mAlertConfigDataIndexList.add(mHumidity_Level);
        mAlertConfigDataIndexList.add(mOxygen_Level);
        mAlertConfigDataIndexList.add(mCO_Level);

    }
}
