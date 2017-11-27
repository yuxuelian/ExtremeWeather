package com.as.weather.contract;

import android.content.Context;

import com.as.weather.model.bean.WeatherBean;

/**
 * 项目名称：ECanal
 * 作者：王开波
 * 邮箱：568966289@qq.com
 * 公司：山东宇易信息科技
 * 创建时间：2017/4/14 18:57
 * 备注：
 */

public class ShowWeatherContract {

    public interface View {
        void UpdateWeatherUI(WeatherBean weatherBean);

        void refreshComplete();

        void showRefreshError();
    }

    public interface Presenter extends BasePresenter {
        //请求网络更新数据
        void UpdateWeatherData(Context context, String cityID);

        //使用提供的数据更新数据
        void UpdateWeatherData(WeatherBean weatherBean);

        void saveWeatherBeanCache(Context context, String cityId, WeatherBean weatherBean);
    }

    public interface Model {
        interface WeatherDataCallBack {
            void onSuccess(WeatherBean weatherBean);

            void onFailed();
        }

        void RequestServerData(String cityID, WeatherDataCallBack callBack);

        void saveWeatherBeanCache(Context context, String cityId, WeatherBean weatherBean);
    }
}