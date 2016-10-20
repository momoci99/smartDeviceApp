package service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notemel.deviceappalphav02.R;

/**
 * Created by Melchior_S on 2016-10-19.
 */

public class AlarmService extends Service {

    private final String TAG = AlarmService.this.getClass().getSimpleName();
    private MediaPlayer mPlayer = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("slog", "onStartCommand()");
        return START_NOT_STICKY;
    }

    //서비스 최초 생성시 실행. 단 일회만
    @Override
    public void onCreate() {

        setIntentFilter();
        mPlayer = MediaPlayer.create(this, R.raw.alarm_test_oxy);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayer.stop();
            }
        });
        Log.e("slog", "onStart()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("slog", "onDestroy()");
        mPlayer.stop();
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
        /** Receives the broadcast that has been fired */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("YOUR_INTENT_FILTER")) {
                //HERE YOU WILL GET VALUES FROM BROADCAST THROUGH INTENT EDIT YOUR TEXTVIEW///////////
                String receivedValue = intent.getStringExtra("alert");
                Log.e(TAG, receivedValue + " burn!!!!!!");
            }
        }
    };
}
