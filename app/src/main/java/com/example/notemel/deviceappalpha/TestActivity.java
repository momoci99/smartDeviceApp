package com.example.notemel.deviceappalpha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import java.util.concurrent.BlockingQueue;


import system.ThreadManager;

/**
 * Created by noteMel on 2016-07-05.
 */
public class TestActivity extends AppCompatActivity{
    private Button testBtn;

    private ThreadManager mThreadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testBtn = (Button) findViewById(R.id.btn_test);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mThreadManager = mThreadManager.getInstance();
                BlockingQueue<Runnable> threadQueue =  mThreadManager.GetThreadQueue();

            }
        });
    }
}
