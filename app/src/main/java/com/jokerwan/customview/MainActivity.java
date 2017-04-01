package com.jokerwan.customview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.jokerwan.customview.widget.WaveProgressView;

public class MainActivity extends AppCompatActivity {

    /************************ PercentCircleView ***************************/
   /* private int progress;

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

//        circleView = (PercentCircleView) findViewById(R.id.circle);
////        circleView.setmProgress(80);
//        handler.sendEmptyMessageDelayed(0,200);
    }*/

    /************************ WaveProgressView ***************************/
    private WaveProgressView wpv;
    private static final int FLAG_ONE = 0X0001;
    private int max_progress = 100;
    private int progress;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress++;
            switch (msg.what) {
                case FLAG_ONE:
                    if (progress <= max_progress){
                        wpv.setCurrentProgress(progress, progress + "%");
                        sendEmptyMessageDelayed(FLAG_ONE, 100);
                    }else {
                        return;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wave_progress);
        initView();
    }
    private void initView() {
        wpv = (WaveProgressView) findViewById(R.id.wpv);
        wpv.setWaveColor("#ff0000");
        handler.sendEmptyMessageDelayed(FLAG_ONE, 1000);
    }
}
