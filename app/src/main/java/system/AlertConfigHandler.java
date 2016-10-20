package system;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Melchior_S on 2016-10-20.
 */

public class AlertConfigHandler {

    private Context mContext = null;
    private HashMap<String,Boolean> mAlertOptionaHash = new HashMap<>();
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

    public AlertConfigHandler()
    {
        //데이터베이스에 미리 설정된 초기값이 없으면
        //초기값 생성.

        //초기값이 있으면
        //초기값을 읽어오고
        //읽은 초기값을 AlertConfigActivity에 출력

        //AlertConfigActivity의 데이터를 데이터 베이스에 저장
        //커넥션에서 데이터 검사하여 서비스에 브로드캐스트 전달.
    }

}
