package com.base.weather.contract;

import android.content.Context;

import com.base.weather.model.bean.WeatherBean;

/**
 *
 *
 * 邮箱：568966289@qq.com
 *
 * 创建时间：2017/4/14 18:57
 * 备注：
 */

public class ShowWeatherContract {

    public interface View {
        void updateWeatherUI(WeatherBean weatherBean);

        void refreshComplete();

        void showRefreshError();
    }

    public interface Presenter extends BasePresenter {
        //请求网络更新数据
        void updateWeatherData(Context context, String cityID);

        //使用提供的数据更新数据
        void updateWeatherData(WeatherBean weatherBean);

        void saveWeatherBeanCache(Context context, String cityId, WeatherBean weatherBean);
    }

    public interface Model {
        interface WeatherDataCallBack<T> {
            void onSuccess(T weatherBean);

            void onFailed();
        }

        void requestServerData(String cityID, WeatherDataCallBack callBack);

        void saveWeatherBeanCache(Context context, String cityId, WeatherBean weatherBean);
    }
}