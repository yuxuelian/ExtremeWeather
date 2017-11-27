package com.base.weather.presenter;

import android.content.Context;

import com.base.weather.contract.ShowWeatherContract;
import com.base.weather.model.ShowWeatherModelImpl;
import com.base.weather.model.bean.WeatherBean;

/**
 * Created by 56896 on 2017/04/14
 *
 * @author Administrator
 */

public class ShowWeatherPresenterImpl implements ShowWeatherContract.Presenter {
    private ShowWeatherContract.View iShowWeatherView;
    private ShowWeatherContract.Model iShowWeatherModel;

    public ShowWeatherPresenterImpl(ShowWeatherContract.View iShowWeatherView) {
        this.iShowWeatherView = iShowWeatherView;
        iShowWeatherModel = new ShowWeatherModelImpl();
    }

    @Override
    public void updateWeatherData(final Context context, final String cityID) {
        if (iShowWeatherView != null) {
            iShowWeatherModel.requestServerData(cityID, new ShowWeatherContract.Model.WeatherDataCallBack<WeatherBean>() {
                @Override
                public void onSuccess(WeatherBean weatherBean) {
                    //由于这个方法异步执行，所以这个地方也需要进行判断
                    if (iShowWeatherView != null) {
                        //刷新完成
                        iShowWeatherView.refreshComplete();
                        //更新UI
                        iShowWeatherView.updateWeatherUI(weatherBean);
                    }
                }

                @Override
                public void onFailed() {
                    //由于这个方法异步执行，所以这个地方也需要进行判断
                    if (iShowWeatherView != null) {
                        //下拉刷新完成
                        iShowWeatherView.refreshComplete();
                        //显示刷新错误提示
                        iShowWeatherView.showRefreshError();
                    }
                }
            });
        }
    }

    @Override
    public void updateWeatherData(WeatherBean weatherBean) {
        iShowWeatherView.updateWeatherUI(weatherBean);
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