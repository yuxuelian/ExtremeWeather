package com.base.weather.model;

import android.content.Context;

import com.base.weather.constant.API;
import com.base.weather.contract.ShowWeatherContract;
import com.base.weather.model.bean.WeatherBean;
import com.base.weather.net.RequestDataModel;
import com.base.weather.util.cache.DiskObjectCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 56896 on 2017/04/14
 *
 * @author Administrator
 */
public class ShowWeatherModelImpl implements ShowWeatherContract.Model {

    @Override
    public void requestServerData(String cityID, WeatherDataCallBack callBack) {
        RequestDataModel<WeatherBean> requestDataModel = new RequestDataModel<>(callBack, WeatherBean.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("url", API.WEATHER_URL);

        //请求方式  GET
        parameters.put("method", "GET");

        parameters.put("key", API.KEY);
        parameters.put("city", cityID);
        requestDataModel.execute(parameters);
    }


    @Override
    public void saveWeatherBeanCache(Context context, String cityId, WeatherBean weatherBean) {
        //缓存城市的数据
        DiskObjectCache<WeatherBean> diskObjectCache = new DiskObjectCache<>(context, MainModelImpl.CACHE_NAME);
        diskObjectCache.saveObjectCache(cityId, weatherBean);
        diskObjectCache.close();
    }
}