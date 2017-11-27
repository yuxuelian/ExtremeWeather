package com.as.weather.util.async;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.as.weather.constant.API;
import com.as.weather.model.bean.WeatherBean;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * 作者：王开波
 * 邮箱：568966289@qq.com
 * 公司：山东宇易信息科技
 * 创建时间：2017/3/26
 */

public class AsyncRequestData extends AsyncTask<String, Void, WeatherBean> {

    private onRequestListener listener;

    public interface onRequestListener {
        void onSucceed(WeatherBean weatherBean);

        void onFailed();
    }

    public AsyncRequestData(onRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected WeatherBean doInBackground(String... params) {
        return getWeatherBean(params[0]);
    }

    @Nullable
    private WeatherBean getWeatherBean(String param) {
        Request<String> stringRequest = NoHttp.createStringRequest(API.WEATHER_URL, RequestMethod.GET);

        stringRequest.add("city", param);

        //这个键不能随便写   必须与get请求中所带参数一致
        stringRequest.add("key", API.KEY);

        stringRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);

        Response<String> stringResponse = NoHttp.startRequestSync(stringRequest);
        if (stringResponse.isSucceed()) {
            String jsonText = stringResponse.get();

            LogUtils.json(jsonText);

            WeatherBean weatherBean = JSONObject.parseObject(jsonText, WeatherBean.class);
            LogUtils.d(weatherBean);
            return weatherBean;
        }
        return null;
    }

    @Override
    protected void onPostExecute(WeatherBean weatherBean) {
        if (listener != null) {
            if (weatherBean == null) {
                listener.onFailed();
            } else {
                listener.onSucceed(weatherBean);
            }
        }
    }
}
