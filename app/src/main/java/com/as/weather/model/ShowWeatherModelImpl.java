package com.as.weather.model;

import android.content.Context;

import com.as.weather.contract.ShowWeatherContract;
import com.as.weather.model.bean.WeatherBean;
import com.as.weather.util.async.AsyncRequestData;
import com.as.weather.util.cache.DiskObjectCache;

/**
 * Created by 56896 on 2017/04/14
 */

public class ShowWeatherModelImpl implements ShowWeatherContract.Model {

    @Override
    public void RequestServerData(String cityID, final WeatherDataCallBack callBack) {
        new AsyncRequestData(new AsyncRequestData.onRequestListener() {
            @Override
            public void onSucceed(WeatherBean weatherBean) {
                callBack.onSuccess(weatherBean);
            }

            @Override
            public void onFailed() {
                callBack.onFailed();
            }
        }).execute(cityID);
    }

    @Override
    public void saveWeatherBeanCache(Context context, String cityId, WeatherBean weatherBean) {
        //缓存城市的数据
        DiskObjectCache<WeatherBean> diskObjectCache = new DiskObjectCache<>(context, MainModelImpl.CACHE_NAME);
        diskObjectCache.saveObjectCache(cityId, weatherBean);
        diskObjectCache.close();
    }
}