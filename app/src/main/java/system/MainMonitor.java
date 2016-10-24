package system;

import android.content.Context;
import android.content.Intent;

import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;

import format.TransactionForm;

/**
 * Created by Melchior_S on 2016-10-24.
 */

public class MainMonitor {

    private String mTemperatureString;
    private String mHumidityString;
    private String mOxygenString;
    private String mCOString;

    private Context mContext;

    private ArrayList<String> mDataInfoList = new ArrayList<>();

    public MainMonitor(Context context) {
        this.mContext = context;
        mTemperatureString = context.getString(R.string.Temperature);
        mHumidityString = context.getString(R.string.Humidity);
        mOxygenString = context.getString(R.string.Oxygen);
        mCOString = context.getString(R.string.CO);
    }

    public void sendDataToMainActivate(TransactionForm transactionForm) {
        for (int i = 0; i < transactionForm.getSID().length; i++) {
            if (transactionForm.getIntData()[i] == -1) {
                if (transactionForm.getSID()[i].equals("tempt")) {
                    mDataInfoList.add(mTemperatureString + " : " + String.valueOf(transactionForm.getFloatData()[i]));
                }
                if (transactionForm.getSID()[i].equals("co")) {
                    mDataInfoList.add(mCOString + " : " + String.valueOf(transactionForm.getFloatData()[i]));
                }
                if (transactionForm.getSID()[i].equals("hum")) {
                    mDataInfoList.add(mHumidityString + " : " + String.valueOf(transactionForm.getFloatData()[i]));
                }
                if (transactionForm.getSID()[i].equals("oxy")) {
                    mDataInfoList.add(mOxygenString + " : " + String.valueOf(transactionForm.getFloatData()[i]));
                }
            } else if (transactionForm.getFloatData()[i] == -1) {
                if (transactionForm.getSID()[i].equals("tempt")) {
                    mDataInfoList.add(mTemperatureString + " : " + String.valueOf(transactionForm.getIntData()[i]));
                }
                if (transactionForm.getSID()[i].equals("co")) {
                    mDataInfoList.add(mCOString + " : " + String.valueOf(transactionForm.getIntData()[i]));
                }
                if (transactionForm.getSID()[i].equals("hum")) {
                    mDataInfoList.add(mHumidityString + " : " + String.valueOf(transactionForm.getIntData()[i]));
                }
                if (transactionForm.getSID()[i].equals("oxy")) {
                    mDataInfoList.add(mOxygenString + " : " + String.valueOf(transactionForm.getIntData()[i]));
                }
            }
        }
        if (mDataInfoList.size() > 0) {
            sendAlertBroadcast();
        }

    }

    private void sendAlertBroadcast() {
        Intent sendAlertIntent = new Intent();
        sendAlertIntent.setAction("MAIN_MONITOR");
        sendAlertIntent.putStringArrayListExtra("DATA_Info_List", mDataInfoList);
        mContext.sendBroadcast(sendAlertIntent);
        mDataInfoList.clear();

    }
}
