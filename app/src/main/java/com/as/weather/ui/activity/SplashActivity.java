package com.as.weather.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.as.weather.R;
import com.as.weather.contract.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 56896 on 2016/12/4.
 */

public class SplashActivity extends BaseActivity {

    private static final String[] strings = new String[]{"APP正在启动,请稍后", "APP正在启动,请稍后.", "APP正在启动,请稍后..", "APP正在启动,请稍后..."};

    private static final String APP_START_RECORD_KEY = "appStartRecordKey";
    private static final String TAG = "SplashActivity";

    //记录App的启动次数
    private SharedPreferences appStartRecord;

    //当前是否是首次启动
    private boolean isAppStart = false;

    //记录当前的APP是否正在运行(false 没有运行,true正在运行)
    private static boolean isRunning = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    wait.setText(strings[msg.arg1 % 4]);
                    break;
                case 1:
                    startMainActivity();
                    break;
            }
        }
    };

    @BindView(R.id.wait_tv)
    TextView wait;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_splash_activity_layout);
        ButterKnife.bind(this);

        new Thread() {
            @Override
            public void run() {
                int count = 0;
                while (count <= 16) {
                    Message obtain = Message.obtain();
                    obtain.arg1 = count;
                    obtain.what = 0;
                    handler.sendMessage(obtain);
                    count++;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        //出栈当前这个activity
        finish();
    }
}
