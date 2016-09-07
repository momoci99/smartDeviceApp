package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.transition.Transition;
import android.util.Log;

import com.example.notemel.deviceappalphav02.R;

import java.util.concurrent.CopyOnWriteArrayList;

import format.DBResultForm;
import format.TransactionForm;

/**
 * Created by Melchior_S on 2016-09-02.
 */


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


    private String[] mDEVICE_LISTcolumnsList = {"DEVICENAME", "TIME"};

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

    public void createDataTable(String deviceName) {
        String createDataTableQuery = "";
        createDataTableQuery += "CREATE TABLE IF NOT EXISTS ";
        createDataTableQuery += deviceName + " ";
        createDataTableQuery += mCreateDataTableQuery;

        Log.d(TAG, createDataTableQuery);
        mDB.execSQL(createDataTableQuery);

    }

    public void insertRow_DeviceList(CopyOnWriteArrayList<String> deviceList) {
        String insertDeviceList_Query = "";
        for (int i = 0; i < deviceList.size(); i++) {
            insertDeviceList_Query = "INSERT OR REPLACE INTO DEVICE_LIST (DEVICENAME) VALUES ";
            insertDeviceList_Query += "(";
            insertDeviceList_Query += "\"" + deviceList.get(i) + "\"";
            insertDeviceList_Query += ")";
        }
        Log.d(TAG, insertDeviceList_Query);
        mDB.execSQL(insertDeviceList_Query);

    }

    public void insertRow_SensorTable(TransactionForm transactionForm) {
        ContentValues newValues = new ContentValues();
        newValues.put("DEVICENAME", transactionForm.getName());
        newValues.put("BOARD_VER", transactionForm.getBoardVer());

        String stringSNAME = "SNAME_";
        String stringDATA = "DATA_";

        for (int i = 0; i < transactionForm.getSID().length; i++) {
            newValues.put(stringSNAME + (String.valueOf(i + 1)), transactionForm.getSID()[i]);

            if (transactionForm.getIntData()[i] == -1) {
                newValues.put(stringDATA + (String.valueOf(i + 1)), transactionForm.getFloatData()[i]);
            } else if (transactionForm.getFloatData()[i] == -1) {
                newValues.put(stringDATA + (String.valueOf(i + 1)), transactionForm.getIntData()[i]);
            }
        }
        //Log.d(TAG, newValues.toString());
        mDB.insert(transactionForm.getName(), null, newValues);


    }

    public CopyOnWriteArrayList<DBResultForm> getSensorDataList(String deviceName)
    {
        CopyOnWriteArrayList<DBResultForm> DBResultFormList = new CopyOnWriteArrayList<>();
        String[] columns = {"LOGNUMBER",
                "DEVICENAME",
                "BOARD_VER",
                "SNAME_1", "DATA_1",
                "SNAME_2", "DATA_2",
                "SNAME_3", "DATA_3",
                "SNAME_4", "DATA_4",
                "TIME"};

        Cursor dataQueryResult = mDB.query(deviceName, columns, null, null, null, null, null);



        while (dataQueryResult.moveToNext()) {
            int logNumber = dataQueryResult.getInt(0);

            String devicename = dataQueryResult.getString(1);
            int BoardVer = dataQueryResult.getInt(2);
            String SNAME_1 = dataQueryResult.getString(3);
            double DATA_1 = dataQueryResult.getDouble(4);

            String SNAME_2 = dataQueryResult.getString(5);
            double DATA_2 = dataQueryResult.getDouble(6);

            String SNAME_3 = dataQueryResult.getString(7);
            double DATA_3 = dataQueryResult.getDouble(8);

            String SNAME_4 = dataQueryResult.getString(9);
            double DATA_4 = dataQueryResult.getDouble(10);

            String time = dataQueryResult.getString(11);


            DBResultFormList.add(new DBResultForm(logNumber,devicename,BoardVer,
                    SNAME_1,DATA_1,
                    SNAME_2,DATA_2,
                    SNAME_3,DATA_3,
                    SNAME_4,DATA_4,
                    time));
            /*
            String stringResult = String.valueOf(logNumber)
                    + devicename
                    + String.valueOf(BoardVer)
                    + SNAME_1
                    + String.valueOf(DATA_1)
                    + SNAME_2
                    + String.valueOf(DATA_2)
                    + SNAME_3
                    + String.valueOf(DATA_3)
                    + SNAME_4
                    + String.valueOf(DATA_4)
                    + time;
                    Log.e(TAG,stringResult);
            */

        }
        dataQueryResult.close();
        return DBResultFormList;


    }

    public CopyOnWriteArrayList<String> getFullDeviceList()
    {
        CopyOnWriteArrayList<String> fullDeviceList = new CopyOnWriteArrayList<>();
        Cursor result = mDB.query("DEVICE_LIST", mDEVICE_LISTcolumnsList, null, null, null, null, null);
        while (result.moveToNext()) {
            String name = result.getString(0);
            fullDeviceList.add(name);

        }
        result.close();
        return fullDeviceList;

    }
    public void showFullDeviceList() {




        String[] columns = {"DEVICENAME", "TIME"};
        Cursor result = mDB.query("DEVICE_LIST", columns, null, null, null, null, null);
        while (result.moveToNext()) {
            String name = result.getString(0);
            String time = result.getString(1);
            Log.e(TAG, "Device Name : " + name + "  time : " + time);
        }
        //result.close();
    }

    public void showAllDeviceData() {
        String[] deviceColumns = {"DEVICENAME", "TIME"};
        String[] columns = {"LOGNUMBER",
                "DEVICENAME",
                "BOARD_VER",
                "SNAME_1", "DATA_1",
                "SNAME_2", "DATA_2",
                "SNAME_3", "DATA_3",
                "SNAME_4", "DATA_4",
                "TIME"};

        Cursor result = mDB.query("DEVICE_LIST", deviceColumns, null, null, null, null, null);


        while (result.moveToNext()) {
            String name = result.getString(0);
            Cursor dataQueryResult = mDB.query(name, columns, null, null, null, null, null);

            while (dataQueryResult.moveToNext()) {
                int logNumber = dataQueryResult.getInt(0);

                String devicename = dataQueryResult.getString(1);
                int BoardVer = dataQueryResult.getInt(2);
                String SNAME_1 = dataQueryResult.getString(3);
                double DATA_1 = dataQueryResult.getDouble(4);

                String SNAME_2 = dataQueryResult.getString(5);
                double DATA_2 = dataQueryResult.getDouble(6);

                String SNAME_3 = dataQueryResult.getString(7);
                double DATA_3 = dataQueryResult.getDouble(8);

                String SNAME_4 = dataQueryResult.getString(9);
                double DATA_4 = dataQueryResult.getDouble(10);

                String time = dataQueryResult.getString(11);

                String stringResult = String.valueOf(logNumber)
                        + devicename
                        + String.valueOf(BoardVer)
                        + SNAME_1
                        + String.valueOf(DATA_1)
                        + SNAME_2
                        + String.valueOf(DATA_2)
                        + SNAME_3
                        + String.valueOf(DATA_3)
                        + SNAME_4
                        + String.valueOf(DATA_4)
                        + time;

                Log.e(TAG,stringResult);
            }
        }

    }


}
