package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import format.DBResultForm;
import format.TransactionForm;

/**
 * Created by Melchior_S on 2016-09-02.
 */


public class DBCommander {

    private static String TAG = "DBCommander";

    private static SQLiteDatabase mDB = null;
    private static DBOpenHelper mDBHelper;

    //DB 초기화에 필요한 변수들
    private static final String mDBName = "SmartDevice.db";
    private static final int mDBVersion = 1; // 데이터베이스 버전


    private static final String[] mColumnsSensorData = {"LOGNUMBER",
            "DEVICENAME",
            "BOARD_VER",
            "SNAME_1", "DATA_1",
            "SNAME_2", "DATA_2",
            "SNAME_3", "DATA_3",
            "SNAME_4", "DATA_4",
            "TIME"};

    private static final String[] mColumnsSensorTime = {"TIME"};

    private static final String[] mSensorElementData = {"co","tempt","hum","oxy"};



    public static String[] getSensorElementData()
    {
        return mSensorElementData;
    }

    /*
        Table 생성에 필요한 각종 텍스트
    */
    private static String mCreateDataTableQuery = null;
    private static String mCreateTableDeviceList_Query = null;

    private static final String[] mDEVICE_LISTcolumnList = {"DEVICENAME", "TIME"};

    public static void InitDB(Context context) {


        mDBHelper = new DBOpenHelper(context, mDBName, null, mDBVersion);

        try {
            //읽고 쓸수 있는 DB 객체 불러옴
            mDB = mDBHelper.getWritableDatabase();
            mCreateTableDeviceList_Query = context.getResources().getString(R.string.query_create_device_list);
            mCreateDataTableQuery = context.getResources().getString(R.string.query_create_data_table);

            createDeviceListTable();

        } catch (SQLiteException e) {

            e.printStackTrace();
            Log.e(TAG, "DB장애 발생");
        }
    }

    private static void createDeviceListTable() throws SQLiteException {

        Log.d(TAG, mCreateTableDeviceList_Query);
        mDB.execSQL(mCreateTableDeviceList_Query);
    }

    public static void createDataTable(String deviceName) {
        String createDataTableQuery = "";
        createDataTableQuery += "CREATE TABLE IF NOT EXISTS ";
        createDataTableQuery += "'";
        createDataTableQuery += deviceName + "'" + " ";
        createDataTableQuery += mCreateDataTableQuery;

        Log.d(TAG, createDataTableQuery);
        mDB.execSQL(createDataTableQuery);

    }

    public static void insertRow_DeviceList(CopyOnWriteArrayList<String> deviceList) {
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

    public static void insertRow_SensorTable(TransactionForm transactionForm) {
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
        //newValues.put("TIME","datetime('now','localtime'");
        mDB.insert("'" + transactionForm.getName() + "'", null, newValues);


    }

    public static ArrayList<DBResultForm> getSensorDataListTimeCondition(String deviceName, String currentTime, String pastTime) {
        ArrayList<DBResultForm> DBResultFormList = new ArrayList<>();
        Cursor dataQueryResult = mDB.query("'" + deviceName + "'", mColumnsSensorData, "TIME < " + "'" + currentTime + "'" + "AND " + "TIME > " + "'" + pastTime + "'", null, null, null, null);

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


            DBResultFormList.add(new DBResultForm(logNumber, devicename, BoardVer,
                    SNAME_1, DATA_1,
                    SNAME_2, DATA_2,
                    SNAME_3, DATA_3,
                    SNAME_4, DATA_4,
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

    public static long getSensorDataRowCount(String tableName)
    {
        return DatabaseUtils.queryNumEntries(mDB,"'" + tableName + "'");

    }


    public static String getFirstSensorRecordTime(String deviceName)
    {

        Cursor dataQueryResult =
                mDB.query("'" + deviceName + "'", mColumnsSensorTime, null, null, null, null, null,"0 , 1");

        dataQueryResult.moveToNext();
        String time = dataQueryResult.getString(0);
        dataQueryResult.close();
        return time;
    }
    public static String getLastSensorRecordTime(String deviceName)
    {
        Cursor dataQueryResult =
                mDB.query("'" + deviceName + "'", mColumnsSensorTime, null, null, null, null, mColumnsSensorTime[0] + " desc","0 , 1");

        dataQueryResult.moveToNext();
        String time = dataQueryResult.getString(0);
        dataQueryResult.close();
        return time;
    }


    /**
     *
     * @param deviceName - table name
     * @param count - number of row
     * @param offsetNumber - offset
     * @return offset을 기준으로 count만큼의 row를 반환한다.
     */
    public static ArrayList<DBResultForm> getSensorDataList(String deviceName, String count, String offsetNumber)
    {
        ArrayList<DBResultForm> DBResultFormList = new ArrayList<>();
        Cursor dataQueryResult =
                mDB.query("'" + deviceName + "'", mColumnsSensorData, null, null, null, null, null,offsetNumber+","+count);


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


            DBResultFormList.add(new DBResultForm(logNumber, devicename, BoardVer,
                    SNAME_1, DATA_1,
                    SNAME_2, DATA_2,
                    SNAME_3, DATA_3,
                    SNAME_4, DATA_4,
                    time));
        }
        dataQueryResult.close();
        return DBResultFormList;

    }

    /**
     *
     * @param deviceName
     * @return SensorDataList
     */
    public static ArrayList<DBResultForm> getSensorDataList(String deviceName) {
        ArrayList<DBResultForm> DBResultFormList = new ArrayList<>();
        Cursor dataQueryResult = mDB.query("'" + deviceName + "'", mColumnsSensorData, null, null, null, null, null);


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


            DBResultFormList.add(new DBResultForm(logNumber, devicename, BoardVer,
                    SNAME_1, DATA_1,
                    SNAME_2, DATA_2,
                    SNAME_3, DATA_3,
                    SNAME_4, DATA_4,
                    time));
        }
        dataQueryResult.close();
        return DBResultFormList;


    }

    public static CopyOnWriteArrayList<String> getFullDeviceList() {
        CopyOnWriteArrayList<String> fullDeviceList = new CopyOnWriteArrayList<>();
        Cursor result = mDB.query("DEVICE_LIST", mDEVICE_LISTcolumnList, null, null, null, null, null);
        while (result.moveToNext()) {
            String name = result.getString(0);
            fullDeviceList.add(name);

        }
        result.close();
        return fullDeviceList;

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
            Cursor dataQueryResult = mDB.query("'" + name + "'", columns, null, null, null, null, null);

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

                Log.e(TAG, stringResult);
                dataQueryResult.close();
            }
        }
        result.close();

    }
    public static String getMaxData(String deviceName, String columnName)
    {
        double maxData = 0;
        Cursor dataQueryResult = mDB.query("'" + deviceName + "'",new String[]{"MAX("+columnName+")"} , null, null, null, null, null);
        while (dataQueryResult.moveToNext()) {
            maxData = dataQueryResult.getDouble(0);
        }
        dataQueryResult.close();
        return String.format(Locale.US,"%.2f",maxData);
    }
    public static String getMinData(String deviceName, String columnName)
    {
        double minData = 0;
        Cursor dataQueryResult = mDB.query("'" + deviceName + "'",new String[]{"MIN("+columnName+")"} , null, null, null, null, null);
        while (dataQueryResult.moveToNext()) {
            minData = dataQueryResult.getDouble(0);
        }
        dataQueryResult.close();
        return String.format(Locale.US,"%.2f",minData);
    }
    public static String getAvgData(String deviceName, String columnName)
    {
        double minData = 0;
        Cursor dataQueryResult = mDB.query("'" + deviceName + "'",new String[]{"AVG("+columnName+")"} , null, null, null, null, null);
        while (dataQueryResult.moveToNext()) {
            minData = dataQueryResult.getDouble(0);
        }
        dataQueryResult.close();
        return String.format(Locale.US,"%.2f",minData);
    }


}
