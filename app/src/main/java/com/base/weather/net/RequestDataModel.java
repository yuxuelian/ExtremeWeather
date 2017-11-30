package com.base.weather.net;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.base.weather.contract.ShowWeatherContract;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
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
    protected T doInBackground(Map<String, String>... inputParameters) {
        Map<String, String> mapParameter = inputParameters[0];

        //取得URL
        String url = mapParameter.get("url");
        //移除
        mapParameter.remove("url");

        //取得请求方式
        String method = mapParameter.get("method");
        //移除请求方式
        mapParameter.remove("method");


        StringBuilder parameterString = new StringBuilder();
        //遍历map拼接参数
        for (HashMap.Entry<String, String> entry : mapParameter.entrySet()) {
            parameterString
                    .append(entry.getKey())
                    .append('=')
                    .append(entry.getValue())
                    .append('&');
        }
        String stringParameter = parameterString.toString();

        String url1;
        if ("get".equals(method.toLowerCase())) {
            url1 = url + "?" + stringParameter;
            method = "GET";
            Logger.d(url1);
        } else if ("post".equals(method.toLowerCase())) {
            url1 = url;
            method = "POST";
        } else {
            //TODO 其他请求方式
            url1 = url;
        }

        HttpURLConnection urlConnection = null;
        try {
            URL u = new URL(url1);
            urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            //post请求方式传输数据
            switch (method) {
                case "POST":
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    OutputStream outputStream = urlConnection.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream);
                    printWriter.write(stringParameter);
                    printWriter.flush();
                    break;
                default:
                    break;
            }

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
