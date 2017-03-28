package com.jokerwan.customview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import widget.PercentCircleView;

public class MainActivity extends AppCompatActivity {

    private int progress;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(progress < 100) {
                progress ++;
                circleView.setmProgress(progress);
                sendEmptyMessageDelayed(0,200);
            } else {
                removeCallbacksAndMessages(null);
            }
        }
    };
    private PercentCircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleView = (PercentCircleView) findViewById(R.id.circle);
        handler.sendEmptyMessageDelayed(0,200);
    }
}
