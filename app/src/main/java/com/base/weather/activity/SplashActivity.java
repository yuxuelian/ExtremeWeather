package com.base.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.base.weather.R;
import com.base.weather.activity.base.BaseActivity;
import com.base.weather.contract.BasePresenter;

import butterknife.ButterKnife;


/**
 *
 * @author Administrator
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_splash_activity_layout);
        ButterKnife.bind(this);

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                startMainActivity();
            }
        }.sendEmptyMessageDelayed(0,2000);
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
