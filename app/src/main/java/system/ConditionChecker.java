package system;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.notemel.deviceappalphav02.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import format.TransactionForm;

/**
 * Created by Melchior_S on 2016-10-21.
 */

public class ConditionChecker {


    private String mSoundAlertEnable;
    private String mNotificationAlertEnable;
    private String mTemperatureEnable;
    private String mHumidityEnable;
    private String mOxygenEnable;
    private String mCOEnable;

    private String mTemperature_Level;
    private String mHumidity_Level;
    private String mOxygen_Level;
    private String mCO_Level;

    private final String TAG = ConditionChecker.this.getClass().getSimpleName();
    private AlertConfigHandler mAlertConfigHandler = AlertConfigHandler.getInstance();
    private Context mContext;

    private HashMap<String, Boolean> mAlertOptionHash;
    private HashMap<String, Float> mAlertDataHash;

    private ArrayList<String> mAlertInfoList = new ArrayList<>();

    public ConditionChecker(Context context) {
        this.mContext = context;

        //mSoundAlertEnable = context.getString(R.string.SoundAlertEnable);
        //mNotificationAlertEnable = context.getString(R.string.NotificationAlertEnable);

        mTemperatureEnable = context.getString(R.string.TemperatureEnable);
        mHumidityEnable = context.getString(R.string.HumidityEnable);
        mOxygenEnable = context.getString(R.string.OxygenEnable);
        mCOEnable = context.getString(R.string.COEnable);


        mTemperature_Level = context.getString(R.string.Temperature_Level);
        mHumidity_Level = context.getString(R.string.Humidity_Level);
        mOxygen_Level = context.getString(R.string.Oxygen_Level);
        mCO_Level = context.getString(R.string.CO_Level);


    }

    public void checkCondition(TransactionForm transactionForm) {
        mAlertOptionHash = (HashMap) mAlertConfigHandler.getAlertOptionHash().clone();
        mAlertDataHash = (HashMap) mAlertConfigHandler.getAlertDataHash().clone();
        for (int i = 0; i < transactionForm.getSID().length; i++) {
            if (transactionForm.getIntData()[i] == -1) {
                if (transactionForm.getSID()[i].equals("tempt") && mAlertOptionHash.get(mTemperatureEnable)) {
                    if (mAlertDataHash.get(mTemperature_Level) <= transactionForm.getFloatData()[i]) {

                        mAlertInfoList.add(transactionForm.getName());
                        mAlertInfoList.add("Temperature");
                        mAlertInfoList.add(String.valueOf(transactionForm.getFloatData()[i]));
                        sendAlertBroadcast();


                    }

                } else if (transactionForm.getSID()[i].equals("co") && mAlertOptionHash.get(mCOEnable)) {
                    if (mAlertDataHash.get(mCO_Level) <= transactionForm.getFloatData()[i]) {

                        mAlertInfoList.add(transactionForm.getName());
                        mAlertInfoList.add("CO");
                        mAlertInfoList.add(String.valueOf(transactionForm.getFloatData()[i]));
                        sendAlertBroadcast();
                    }

                } else if (transactionForm.getSID()[i].equals("hum") && mAlertOptionHash.get(mHumidityEnable)) {
                    if (mAlertDataHash.get(mHumidity_Level) <= transactionForm.getFloatData()[i]) {

                        mAlertInfoList.add(transactionForm.getName());
                        mAlertInfoList.add("Humidity");
                        mAlertInfoList.add(String.valueOf(transactionForm.getFloatData()[i]));
                        sendAlertBroadcast();
                    }

                } else if (transactionForm.getSID()[i].equals("oxy") && mAlertOptionHash.get(mOxygenEnable)) {
                    if (mAlertDataHash.get(mOxygen_Level) > transactionForm.getFloatData()[i]) {

                        mAlertInfoList.add(transactionForm.getName());
                        mAlertInfoList.add("Oxygen");
                        mAlertInfoList.add(String.valueOf(transactionForm.getFloatData()[i]));
                        sendAlertBroadcast();
                    }

                }

                //For Int
            } else if (transactionForm.getFloatData()[i] == -1) {
                if (transactionForm.getSID()[i].equals("tempt") && mAlertOptionHash.get(mTemperatureEnable)) {
                    if (mAlertDataHash.get(mTemperature_Level) <= transactionForm.getIntData()[i]) {

                        mAlertInfoList.add(transactionForm.getName());
                        mAlertInfoList.add("Temperature");
                        mAlertInfoList.add(String.valueOf(transactionForm.getIntData()[i]));
                        sendAlertBroadcast();
                    }

                } else if (transactionForm.getSID()[i].equals("co") && mAlertOptionHash.get(mCOEnable)) {
                    if (mAlertDataHash.get(mCO_Level) <= transactionForm.getIntData()[i]) {
                        mAlertInfoList.add(transactionForm.getName());
                        mAlertInfoList.add("CO");
                        mAlertInfoList.add(String.valueOf(transactionForm.getIntData()[i]));
                        sendAlertBroadcast();
                    }

                } else if (transactionForm.getSID()[i].equals("hum") && mAlertOptionHash.get(mHumidityEnable)) {
                    if (mAlertDataHash.get(mHumidity_Level) <= transactionForm.getIntData()[i]) {
                        mAlertInfoList.add(transactionForm.getName());
                        mAlertInfoList.add("Humidity");
                        mAlertInfoList.add(String.valueOf(transactionForm.getIntData()[i]));
                        sendAlertBroadcast();

                    }

                } else if (transactionForm.getSID()[i].equals("oxy") && mAlertOptionHash.get(mOxygenEnable)) {
                    if (mAlertDataHash.get(mOxygen_Level) > transactionForm.getIntData()[i]) {
                        mAlertInfoList.add(transactionForm.getName());
                        mAlertInfoList.add("Oxygen");
                        mAlertInfoList.add(String.valueOf(transactionForm.getIntData()[i]));
                        sendAlertBroadcast();

                    }

                }
            }
        }
    }

    private void sendAlertBroadcast() {
        Intent sendAlertIntent = new Intent();
        sendAlertIntent.setAction("YOUR_INTENT_FILTER");
        sendAlertIntent.putStringArrayListExtra("alert",mAlertInfoList);
        mContext.sendBroadcast(sendAlertIntent);
        mAlertInfoList.clear();

    }
}
