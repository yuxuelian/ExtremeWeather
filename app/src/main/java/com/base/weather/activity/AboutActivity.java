package com.base.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.base.weather.R;
import com.base.weather.contract.BasePresenter;
import com.base.weather.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author 56896
 * @date 2016/11/28
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.action_back)
    ImageView actionBack;

    @BindView(R.id.about_title)
    LinearLayout settingTitle;

    public static void start(Activity context) {
        Intent intent = new Intent(context, AboutActivity.class);
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
        setContentView(R.layout.about_activity_layout);
        ButterKnife.bind(this);
        settingTitle.setPadding(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
    }

    @OnClick({
            R.id.action_back})
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.action_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
