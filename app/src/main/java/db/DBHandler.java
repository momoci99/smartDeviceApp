package db;

import android.content.ContentValues;
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

//ContentValues 찾아보기
public class DBHandler {

    private static final String TAG = "DBHandler";

    private SQLiteDatabase mDB = null;
    private DBOpenHelper mDBHelper;

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
    private static String mCreateDataTableQuery = null;
    private static String mCreateTableDeviceList_Query = null;
    private static String mDeviceListTableName = null;

    public void InitDB(Context context) {
        mContext = context;

        mDBHelper = new DBOpenHelper(mContext, mDBName, null, mDBVersion);
        try {
            //읽고 쓸수 있는 DB 객체 불러옴
            mDB = mDBHelper.getWritableDatabase();
            mCreateTableDeviceList_Query = mContext.getResources().getString(R.string.query_create_device_list);
            mCreateDataTableQuery = mContext.getResources().getString(R.string.query_create_data_table);

            createDeviceListTable();

        } catch (SQLiteException e) {

            e.printStackTrace();
            Log.e(TAG, "DB장애 발생");
        }
    }

    public void createDeviceListTable() throws SQLiteException {

        Log.d(TAG, mCreateTableDeviceList_Query);
        mDB.execSQL(mCreateTableDeviceList_Query);
    }
    public void createDataTable(String deviceName)
    {
        String createDataTableQuery = "";
        createDataTableQuery += "CREATE TABLE IF NOT EXISTS ";
        createDataTableQuery +=   deviceName + " ";
        createDataTableQuery += mCreateDataTableQuery;

        Log.d(TAG, createDataTableQuery);
        mDB.execSQL(createDataTableQuery);

    }

    public void insertRow_DeviceList(CopyOnWriteArrayList<String> deviceList) {
        String insertDeviceList_Query = "";
        for (int i = 0; i < deviceList.size(); i++) {
            insertDeviceList_Query = "INSERT OR REPLACE INTO DEVICE_LIST (DEVICENAME) VALUES ";
            insertDeviceList_Query += "(";
            insertDeviceList_Query += "\""+ deviceList.get(i) + "\"";
            insertDeviceList_Query += ")";
        }
        Log.d(TAG, insertDeviceList_Query);
        mDB.execSQL(insertDeviceList_Query);

    }

    public void insertRow_SensorTable(TransactionForm transactionForm) {
        String insertSensorData_Query = "";
        insertSensorData_Query += "INSERT INTO ";
        insertSensorData_Query += transactionForm.getName();
        insertSensorData_Query += " (DEVICENAME, BOARD_VER, SNAME_1, DATA_1, SNAME_2,DATA_2,SNAME_3,DATA_3,SNAME_4,DATA_4) VALUES (";
        insertSensorData_Query += "\"" + transactionForm.getAddress() + "\"";

        insertSensorData_Query += ", ";

        insertSensorData_Query += transactionForm.getBoardVer();

        insertSensorData_Query += ", ";


        for (int i = 0; i < transactionForm.getSID().length; i++) {

            insertSensorData_Query += "\"" + transactionForm.getSID()[i] + "\"" + ", ";

            //for (int j = 0; j < transactionForm.getSID().length; j++) {
            if (transactionForm.getIntData()[i] == -1) {
                insertSensorData_Query += Double.toString(transactionForm.getFloatData()[i]);
                if (i != transactionForm.getSID().length - 1) {
                    insertSensorData_Query += ", ";
                }
            } else if (transactionForm.getFloatData()[i] == -1) {
                insertSensorData_Query += Integer.toString(transactionForm.getIntData()[i]);
                if (i != transactionForm.getSID().length - 1) {
                    insertSensorData_Query += ", ";
                }

            }
        }
        if(transactionForm.getSID().length<4)
        {
            insertSensorData_Query += ", ";
            for(int i=0; i<4-transactionForm.getSID().length; i++)
            {
                insertSensorData_Query += "null";
                insertSensorData_Query += ", ";
                insertSensorData_Query += Integer.toString(-1);
                if (i != (4-transactionForm.getSID().length)-1) {
                    insertSensorData_Query += ", ";
                }
            }
        }
        insertSensorData_Query += "); ";

        Log.d(TAG, insertSensorData_Query);
        mDB.execSQL(insertSensorData_Query);


    }
}
