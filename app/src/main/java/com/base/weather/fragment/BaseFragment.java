package com.base.weather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.base.weather.contract.BasePresenter;

/**
 *
 * 邮箱：568966289@qq.com
 *
 * 创建时间：2017/3/26
 * @author Administrator
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    protected T presenter;

    protected abstract T createPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = this.createPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
