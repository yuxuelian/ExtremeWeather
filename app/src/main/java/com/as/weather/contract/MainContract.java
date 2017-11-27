package com.as.weather.contract;

import android.content.Context;

import com.as.weather.model.bean.WeatherBean;

import java.util.List;

/**
 * 项目名称：ECanal
 * 作者：王开波
 * 邮箱：568966289@qq.com
 * 公司：山东宇易信息科技
 * 创建时间：2017/4/12 10:32
 * 备注：
 */

public class MainContract {

    public interface View {
        //隐藏对话框
        void hiddenDialog();

        //添加定位城市
        void addLocalCity(String cityMsgBean);
    }

    public interface Presenter extends BasePresenter {
        void location(Context context);

        List<String> getShowCityId();

        void saveShowCityId(List<String> ids);

        WeatherBean readWeatherBeanCache(String city);

        void deleteWeatherBean(String key);
    }

    public interface Model {
        interface onLocationListener {

            void onReceiveLocation(String cityMsgBean);
        }

        //实现定位
        void startLocation(Context context, onLocationListener listener);

        //停止定位
        void stopLocation();

        //读取城市列表信息
        List<String> readDiskCacheId();

        //保存城市列表信息
        void saveShowCityId(List<String> ids);

        //读取指定ID的天气信息
        WeatherBean readWeatherBeanCache(String key);

        //删除指定城市ID的天气信息
        void deleteWeatherBean(String key);

        //保存指定城市的ID信息
//        void saveWeatherBeanCache(String key, WeatherBean weatherBean);

        void onDestroy();
    }
}