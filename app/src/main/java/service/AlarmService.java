package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notemel.deviceappalphav02.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import system.AlertConfigHandler;

/**
 * Created by Melchior_S on 2016-10-19.
 */

public class AlarmService extends Service implements TextToSpeech.OnInitListener {

    private final String TAG = AlarmService.this.getClass().getSimpleName();
    private MediaPlayer mPlayer = null;

    private String mSoundAlertEnable;
    private String mNotificationAlertEnable;
    private HashMap<String, Boolean> mAlertOptionHash;

    private String mTemperatureString;
    private String mHumidityString;
    private String mOxygenString;
    private String mCOString;

    private String mTemperatureKOR;
    private String mHumidityKOR;
    private String mOxygenKOR;
    private String mCO_KOR;

    private String mCelsius_KOR;
    private String mPPM_KOR;
    private String mPercent_KOR;

    private String mConcentration_KOR;

    AlertConfigHandler mAlertConfigHandler = AlertConfigHandler.getInstance();

    private TextToSpeech myTTS;

    private long mPastTime = 0;
    private final long mInterVal = 7000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");
        return START_NOT_STICKY;
    }

    //서비스 최초 생성시 실행. 단 일회만
    @Override
    public void onCreate() {

        setIntentFilter();


        mTemperatureString  = this.getString(R.string.Temperature);
        mHumidityString     = this.getString(R.string.Humidity);
        mOxygenString       = this.getString(R.string.Oxygen);
        mCOString           = this.getString(R.string.CO);
        mTemperatureKOR     = this.getString(R.string.Temperature_KOR);
        mHumidityKOR        = this.getString(R.string.Humidity_KOR);
        mOxygenKOR          = this.getString(R.string.Oxygen_KOR);
        mCO_KOR             = this.getString(R.string.CO_KOR);
        mCelsius_KOR        = this.getString(R.string.Celsius_KOR);
        mPPM_KOR            = this.getString(R.string.PPM_KOR);
        mPercent_KOR        = this.getString(R.string.Percent_KOR);
        mConcentration_KOR  = this.getString(R.string.Concentration_KOR);


        mSoundAlertEnable = this.getString(R.string.SoundAlertEnable);
        mNotificationAlertEnable = this.getString(R.string.NotificationAlertEnable);

        mPlayer = MediaPlayer.create(this, R.raw.alarm_test_oxy);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayer.stop();
            }
        });

        mPastTime = System.currentTimeMillis();
        myTTS = new TextToSpeech(this, this);
        Log.e(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        mPlayer.stop();
        myTTS.shutdown();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void startAlarm() {
        if (mPlayer != null) {
            mPlayer.start();
        }

    }

    private void setIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("YOUR_INTENT_FILTER");
        registerReceiver(broadcastReceiver, filter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("YOUR_INTENT_FILTER")) {
                //HERE YOU WILL GET VALUES FROM BROADCAST THROUGH INTENT EDIT YOUR TEXTVIEW///////////
                mAlertOptionHash = (HashMap) mAlertConfigHandler.getAlertOptionHash().clone();
                ArrayList<String> mAlertInfoList = intent.getStringArrayListExtra("alert");
                String deviceName = mAlertInfoList.get(0);
                String type = mAlertInfoList.get(1);
                String value = mAlertInfoList.get(2);
                Log.e(TAG, "name: " + mAlertInfoList.get(0));
                Log.e(TAG, "Type: " + mAlertInfoList.get(1));
                Log.e(TAG, "Value: " + mAlertInfoList.get(2));

                long currentTime = System.currentTimeMillis();
                if (currentTime - mPastTime > mInterVal) {
                    boolean notificationOptionEnable = mAlertOptionHash.get(mNotificationAlertEnable);
                    boolean soundOptionEnable = mAlertOptionHash.get(mSoundAlertEnable);

                    if (notificationOptionEnable && (!soundOptionEnable)) {
                        activateNotification(type, value);
                        mPastTime = currentTime;
                    }

                    if ((!notificationOptionEnable) && soundOptionEnable) {
                        soundAlert(type, value);
                        mPastTime = currentTime;
                    }

                    if (notificationOptionEnable && soundOptionEnable) {
                        activateNotification(type, value);
                        soundAlert(type, value);
                        Log.e(TAG, "notificationOptionEnable + soundOptionEnable");
                        mPastTime = currentTime;
                    }

                    //옵션에 따라 노티피케이션, 경보음 알람 실시
                    //반복시간설정.
                    //가령 노티나 경보음 알람 1회실행후 5초간 무시한다던지..ㅇㅇ
                }
            }
        }

    };

    private void activateNotification(String type, String value) {
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        // 작은 아이콘 이미지.
        builder.setSmallIcon(R.drawable.alert);

        // 알림이 출력될 때 상단에 나오는 문구.
        builder.setTicker("Warning");

        // 알림 출력 시간.
        builder.setWhen(System.currentTimeMillis());

        // 알림 제목.
        builder.setContentTitle(type + " is now " + value);

        // 알림 내용.
        builder.setContentText("Please Evacuation now");

        // 알림시 사운드, 진동, 불빛을 설정 가능.
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        // 알림 터치시 반응.
        //builder.setContentIntent(pendingIntent);

        // 알림 터치시 반응 후 알림 삭제 여부.
        builder.setAutoCancel(true);


        // 고유ID로 알림을 생성.
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(123456, builder.build());
    }

    private void soundAlert(String type, String value) {

        String warningText = "경고 ";
        String dialogue = createDialogue(type,value);
        String evacuationText = "주의하시기 바랍니다.";

        myTTS.speak(warningText, TextToSpeech.QUEUE_FLUSH, null, "asdf");
        myTTS.speak(dialogue, TextToSpeech.QUEUE_ADD, null, "asdf2");
        myTTS.speak(evacuationText, TextToSpeech.QUEUE_ADD, null, "asdf3");
    }

    private String createDialogue(String type,String value) {
        String dialogue = "현재 ";
        if (type.equals(mTemperatureString))
        {
            dialogue+=mTemperatureKOR;
            dialogue+="가 ";
            dialogue+=value;
            dialogue+=mCelsius_KOR;
            dialogue+=" 입니다. ";
        }
        if(type.equals(mHumidityString))
        {
            dialogue+=mHumidityKOR;
            dialogue+="가 ";
            dialogue+=value;
            dialogue+=mPercent_KOR;
            dialogue+=" 입니다. ";
        }
        if(type.equals(mOxygenString))
        {
            dialogue+=mOxygenKOR;
            dialogue+=mConcentration_KOR;
            dialogue+="가 ";
            dialogue+=value;
            dialogue+=mPercent_KOR;
            dialogue+=" 입니다. ";
        }
        if(type.equals(mCOString))
        {
            dialogue+=mCO_KOR;
            dialogue+="가 ";
            dialogue+=value;
            dialogue+=mPPM_KOR;
            dialogue+=" 입니다. ";
        }
        return dialogue;
    }

    @Override
    public void onInit(int status) {


        if (status == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.KOREA) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.KOREA);
            Log.e(TAG, "tts init success");
        } else if (status == TextToSpeech.ERROR) {
            Log.e(TAG, "tts Failed");
        }
    }
}