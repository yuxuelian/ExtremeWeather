package com.as.weather.presenter;

import android.content.Context;

import com.as.weather.contract.MainContract;
import com.as.weather.model.MainModelImpl;
import com.as.weather.model.bean.WeatherBean;

import java.util.List;

/**
 * Created by 56896 on 2017/04/12
 */

public class MainPresenterImpl implements MainContract.Presenter {
    private MainContract.View iMainView;
    private MainContract.Model iMainModel;

    public MainPresenterImpl(Context context, MainContract.View iMainView) {
        this.iMainView = iMainView;
        this.iMainModel = new MainModelImpl(context);
    }

    @Override
    public List<String> getShowCityId() {
        List<String> strings = iMainModel.readDiskCacheId();
        return strings;
    }

    @Override
    public void saveShowCityId(List<String> ids) {
        iMainModel.saveShowCityId(ids);
    }

    @Override
    public WeatherBean readWeatherBeanCache(String key) {
        return iMainModel.readWeatherBeanCache(key);
    }

    @Override
    public void deleteWeatherBean(String key) {
        iMainModel.deleteWeatherBean(key);
    }

    //定位
    @Override
    public void location(Context context) {
        iMainModel.startLocation(context, new MainContract.Model.onLocationListener() {
            @Override
            public void onReceiveLocation(String cityMsgBean) {
                if (iMainView!=null){
                    //隐藏对话框
                    iMainView.hiddenDialog();

                    //添加定位结果
                    iMainView.addLocalCity(cityMsgBean);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        iMainView = null;
        iMainModel.stopLocation();
        iMainModel.onDestroy();
    }
}