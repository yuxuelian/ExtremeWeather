package com.as.weather.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.as.weather.contract.ShowWeatherContract;
import com.as.weather.model.ShowWeatherModelImpl;
import com.as.weather.model.bean.WeatherBean;

/**
 * Created by 56896 on 2017/04/14
 */

public class ShowWeatherPresenterImpl implements ShowWeatherContract.Presenter {
    private ShowWeatherContract.View iShowWeatherView;
    private ShowWeatherContract.Model iShowWeatherModel;

    public ShowWeatherPresenterImpl(ShowWeatherContract.View iShowWeatherView) {
        this.iShowWeatherView = iShowWeatherView;
        iShowWeatherModel = new ShowWeatherModelImpl();
    }

    @Override
    public void UpdateWeatherData(final Context context, final String cityID) {
        if (iShowWeatherView != null) {
            iShowWeatherModel.RequestServerData(cityID, new ShowWeatherContract.Model.WeatherDataCallBack() {
                @Override
                public void onSuccess(final WeatherBean weatherBean) {
                    //由于这个方法异步执行，所以这个地方也需要进行判断
                    if (iShowWeatherView != null) {
                        //刷新完成
                        iShowWeatherView.refreshComplete();
                        //更新UI
                        iShowWeatherView.UpdateWeatherUI(weatherBean);
                    }
                }

                @Override
                public void onFailed() {
                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            //由于这个方法异步执行，所以这个地方也需要进行判断
                            if (iShowWeatherView != null) {
                                //下拉刷新完成
                                iShowWeatherView.refreshComplete();
                                //显示刷新错误提示
                                iShowWeatherView.showRefreshError();
                            }
                        }
                    }.sendEmptyMessageDelayed(0, 1000);
                }
            });
        }
    }

    @Override
    public void UpdateWeatherData(WeatherBean weatherBean) {
        iShowWeatherView.UpdateWeatherUI(weatherBean);
    }

    @Override
    public void saveWeatherBeanCache(Context context, String cityId, WeatherBean weatherBean) {
        iShowWeatherModel.saveWeatherBeanCache(context, cityId, weatherBean);
    }

    @Override
    public void onDestroy() {
        iShowWeatherView = null;
    }
}