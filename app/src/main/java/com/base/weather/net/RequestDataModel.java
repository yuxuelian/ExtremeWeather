package com.base.weather.net;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.base.weather.contract.ShowWeatherContract;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2017/11/27 0027 12:29
 * @description 请求天气数据  传递map参数   且map必须有URL
 */
public class RequestDataModel<T> extends AsyncTask<Map<String, String>, Integer, T> {

    private Class<T> weatherBeanClass;
    private ShowWeatherContract.Model.WeatherDataCallBack callBack;

    public RequestDataModel(ShowWeatherContract.Model.WeatherDataCallBack callBack, Class<T> weatherBeanClass) {
        this.weatherBeanClass = weatherBeanClass;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        //预处理做一些事

    }

    @Override
    protected T doInBackground(Map<String, String>... parameters) {
        Map<String, String> parameter = parameters[0];

        //取得URL
        String url = parameter.get("url");

        //移除
        parameter.remove("url");

        StringBuilder stringBuilder = new StringBuilder(url);

        stringBuilder.append('?');

        //拼接参数
        for (HashMap.Entry<String, String> entry : parameter.entrySet()) {
            stringBuilder
                    .append(entry.getKey())
                    .append('=')
                    .append(entry.getValue())
                    .append('&');
        }

        String url1 = stringBuilder.toString();

        Logger.e(url1);

        HttpURLConnection urlConnection = null;
        try {
            URL u = new URL(url1);
            urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                StringBuilder stringBuilder1 = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder1.append(line);
                }

                String res = stringBuilder1.toString();
                return JSON.parseObject(res, weatherBeanClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    @Override
    protected void onPostExecute(T weatherBean) {
        //数据获取完成
        if (callBack != null) {
            if (weatherBean != null) {
                callBack.onSuccess(weatherBean);
            } else {
                callBack.onFailed();
            }
        }
    }
}
