package com.as.weather.model.bean;

import com.as.weather.R;

/**
 * 项目名称：ECanal
 * 作者：王开波
 * 邮箱：568966289@qq.com
 * 公司：山东宇易信息科技
 * 创建时间：2017/4/15 22:05
 * 备注：
 */

public class SlidingWeatherBean {

    public boolean isShowLocationIcon() {
        return showLocationIcon;
    }

    public void setShowLocationIcon(boolean showLocationIcon) {
        this.showLocationIcon = showLocationIcon;
    }

    public String getWeatherTemp() {
        return weatherTemp;
    }

    public void setWeatherTemp(String weatherTemp) {
        this.weatherTemp = weatherTemp;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getCityText() {
        return cityText;
    }

    public void setCityText(String cityText) {
        this.cityText = cityText;
    }

    //是否显示定位图标
    private boolean showLocationIcon = false;

    //显示温度
    private String weatherTemp = "暂无数据";

    //显示天气图标
    private int weatherIcon = R.drawable.ic_weather_kown;

    //显示城市名
    private String cityText = "城市";

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    private String cityId;
}
