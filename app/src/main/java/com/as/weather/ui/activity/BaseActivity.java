package com.as.weather.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.as.weather.contract.BasePresenter;
import com.as.weather.util.DeviceBarUtil;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T presenter;

    protected abstract T createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceBarUtil.setStatusBarAlpha(getWindow(), 0);
        presenter = this.createPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
