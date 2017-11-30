package com.base.weather.activity.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.base.weather.contract.BasePresenter;
import com.base.weather.util.StatusBarUtil;

/**
 *
 * @author Administrator
 * @date 2017/3/10 0010
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T presenter;

    /**
     * 创建presenter
     * @return
     */
    protected abstract T createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //固定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarAlpha(getWindow(), 0);
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
