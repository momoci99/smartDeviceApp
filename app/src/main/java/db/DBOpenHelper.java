package db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Melchior_S on 2016-05-13.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public static String LOGTABLE = "LOGTABLE";



    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    //http://stackoverflow.com/questions/14814433/how-to-change-timestamp-of-sqlite-db-to-local-timestamp 참조

    /**
     * 최초 테이블생성
     * 총 4개의 테이블생성한다.
     * 각 테이블은 4개의 속성을 가짐
     *
     * ID - int, Autoincrement 주키
     * Time - Datetime, Default Current_timestamp
     * STYPE - Text 센서의 이름
     * Data - Real 센서의 측정값
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            StringBuilder query = new StringBuilder();
            query.append("create table ");
            query.append(LOGTABLE);
            query.append("(LOGNUMBER INTEGER primary key autoincrement, ");
            query.append("TIME DATETIME DEFAULT CURRENT_TIMESTAMP, ");
            query.append("BOARDVER INTEGER, ");
            query.append("STYPE TEXT,");
            query.append("DATA REAL);");
            Log.d("CreateDB", query.toString());
            db.execSQL(query.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*
        db.execSQL("DROP TABLE IF EXISTS " + TableA);
        db.execSQL("DROP TABLE IF EXISTS " + TableB);
        db.execSQL("DROP TABLE IF EXISTS " + TableC);
        db.execSQL("DROP TABLE IF EXISTS " + TableD);

        // create new tables
        onCreate(db);*/
    }
}
