package com.as.weather.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.as.weather.R;
import com.as.weather.contract.BasePresenter;
import com.as.weather.util.DeviceBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by 56896 on 2016/11/28.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.action_back)
    ImageView actionBack;

    @BindView(R.id.about_title)
    LinearLayout settingTitle;

    public static void start(Activity context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_layout);
        ButterKnife.bind(this);
        settingTitle.setPadding(0, DeviceBarUtil.getStatusBarHeight(this), 0, 0);
    }

    @OnClick({R.id.action_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_back:
                onBackPressed();
                break;
        }
    }
}
