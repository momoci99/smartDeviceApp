package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.transition.Transition;
import android.util.Log;

import com.example.notemel.deviceappalphav02.R;

import java.util.concurrent.CopyOnWriteArrayList;

import format.TransactionForm;

/**
 * Created by Melchior_S on 2016-09-02.
 */
public class DBHandler {

    private static final String TAG = "DBHandler";

    private static SQLiteDatabase mDB = null;
    private static DBOpenHelper mDBHelper;

    //DB 초기화에 필요한 변수들
    private static final String mDBName = "SmartDevice.db";
    private static final int mDBVersion = 1; // 데이터베이스 버전

    private Context mContext;

    private volatile static DBHandler objectInstance;

    public static DBHandler getInstance() {
        if (objectInstance == null) {
            synchronized (DBHandler.class) {
                if (objectInstance == null) {
                    objectInstance = new DBHandler();

                }
            }
        }
        return objectInstance;
    }


    /*
        Table 생성에 필요한 각종 텍스트
    */
    private static String mCreateTableQuery = null;
    private static String mCreateTableDeviceList_Query = null;
    private static String mDeviceListTableName = null;

    public void InitDB(Context context) {
        mContext = context;
        /*
        mDBHelper = new DBOpenHelper(mContext, mDBName, null, mDBVersion);
        try {
            //읽고 쓸수 있는 DB 객체 불러옴
            mDB = mDBHelper.getWritableDatabase();
            mCreateTableDeviceList_Query = mContext.getResources().getString(R.string.query_create_device_list);

            mDeviceListTableName = mContext.getResources().getString(R.string.table_name_device_list);

        } catch (SQLiteException e) {

            e.printStackTrace();
            Log.e(TAG, "데이터베이스를 불러오지 못했습니다");
        }*/
    }

    public void createDeviceListTable() {
        //mDB.execSQL(mCreateTableDeviceList_Query);
    }

    public void insertRow_DeviceList(CopyOnWriteArrayList<String> deviceList) {
        String insertDeviceList_Query = "";
        for (int i = 0; i < deviceList.size(); i++) {
            insertDeviceList_Query = "INSERT OR REPLACE INTO DEVICE_LIST (MACADDRESS) VALUES ";
            insertDeviceList_Query += "(";
            insertDeviceList_Query += deviceList.get(i);
            insertDeviceList_Query += ")";
        }
        //mDB.execSQL(insertDeviceList_Query);
        Log.d(TAG,insertDeviceList_Query);
    }

    public void insertRow_SensorTable(TransactionForm transactionForm) {
        String insertSensorData_Query = "";
        insertSensorData_Query += "INSERT INTO ";
        insertSensorData_Query += transactionForm.getAddress();
        insertSensorData_Query += "(MACADDRESS, BOARD_VER, SNAME_1, DATA_1, SNAME_2,DATA_2,SNAME_3,DATA_3,SNAME_4,DATA_4) VALUES (";
        insertSensorData_Query += "\"" + transactionForm.getAddress() + "\"";

        insertSensorData_Query += ", ";

        insertSensorData_Query += transactionForm.getBoardVer();

        insertSensorData_Query += ", ";


        for (int i = 0; i < transactionForm.getSID().length; i++) {

            insertSensorData_Query += "\"" + transactionForm.getSID()[i] + "\"" + ", ";

            for (int j = 0; j < transactionForm.getSID().length; j++) {
                if (transactionForm.getIntData()[j] == 0) {
                    insertSensorData_Query += transactionForm.getFloatData()[j];
                    if(j!=transactionForm.getSID().length-1)
                    {
                        insertSensorData_Query += ", ";
                    }
                    j++;
                } else if (transactionForm.getFloatData()[j] == 0) {
                    insertSensorData_Query += transactionForm.getIntData()[j];
                    insertSensorData_Query += ", ";
                    if(j!=transactionForm.getSID().length-1)
                    {
                        insertSensorData_Query += ", ";
                    }
                    j++;
                }
            }

        }
        insertSensorData_Query += "); ";
        mDB.execSQL(insertSensorData_Query);
        Log.d(TAG,insertSensorData_Query);

    }
}
