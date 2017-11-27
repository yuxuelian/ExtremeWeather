package com.as.weather.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.apkfuns.logutils.LogUtils;
import com.as.weather.contract.MainContract;
import com.as.weather.model.bean.WeatherBean;
import com.as.weather.util.cache.DiskObjectCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 56896 on 2017/04/12
 */

public class MainModelImpl implements MainContract.Model {

    public static final String CACHE_NAME = "WeatherBeanCache";

    public static final String CACHE_CITIES_ID_KEY = "citiesId";

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //缓存城市的ID集合
    private DiskObjectCache<List<String>> diskObjectCache;

    //缓存城市的数据
    private DiskObjectCache<WeatherBean> weatherBeanDiskObjectCache;

    public MainModelImpl(Context context) {
        //读取城市ID的DiskObjectCache
        diskObjectCache = new DiskObjectCache<>(context, CACHE_NAME);
        //读取城市缓存的天气数据
        weatherBeanDiskObjectCache = new DiskObjectCache<>(context, CACHE_NAME);
    }

    @Override
    public void startLocation(final Context context, final onLocationListener listener) {
        //进行定位
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation == null) {
                    Log.e("TAG", "未能定位成功");
                    listener.onReceiveLocation(null);
                    return;
                }

                LogUtils.d(amapLocation);

//                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                amapLocation.getLatitude();//获取纬度
//                amapLocation.getLongitude();//获取经度
//                amapLocation.getAccuracy();//获取精度信息
//                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                amapLocation.getCountry();//国家信息
//                amapLocation.getProvince();//省信息
//                amapLocation.getCity();//城市信息
//                amapLocation.getDistrict();//城区信息
//                amapLocation.getStreet();//街道信息
//                amapLocation.getStreetNum();//街道门牌号信息
//                amapLocation.getCityCode();//城市编码
//                amapLocation.getAdCode();//地区编码
//                amapLocation.getAoiName();//获取当前定位点的AOI信息
//                amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
//                amapLocation.getFloor();//获取当前室内定位的楼层
//
////                amapLocation.getGpsStatus();//获取GPS的当前状态
//
//                //获取定位时间
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                String dateString = df.format(date);

                Toast.makeText(context, "定位到的地点是:"+amapLocation.getCity()+amapLocation.getDistrict(), Toast.LENGTH_SHORT).show();

                //构建当前定位城市的信息
                listener.onReceiveLocation(amapLocation.getCity());
            }
        });

        this.initLocationOptions();

        //启动定位
        mLocationClient.startLocation();
    }

    private void initLocationOptions() {
        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

//        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
//        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);

//        //获取一次定位结果：
//        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2 * 60 * 1000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否强制刷新WIFI，默认为true，强制刷新。
//        mLocationOption.setWifiActiveScan(false);

        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);

        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public List<String> readDiskCacheId() {
        List<String> strings = diskObjectCache.readObjectCache(CACHE_CITIES_ID_KEY);
        if (strings == null) {
            strings = new ArrayList<>();
        }
        return strings;
    }

    @Override
    public void saveShowCityId(List<String> ids) {
        //保存到本地的缓存中
        diskObjectCache.saveObjectCache(CACHE_CITIES_ID_KEY, ids);
    }

    @Override
    public WeatherBean readWeatherBeanCache(String key) {
        WeatherBean weatherBean = weatherBeanDiskObjectCache.readObjectCache(key);
        return weatherBean;
    }

    @Override
    public void deleteWeatherBean(String key) {
        weatherBeanDiskObjectCache.deleteObject(key);
    }

    @Override
    public void onDestroy() {
        if (diskObjectCache != null) {
            diskObjectCache.close();
        }

        if (weatherBeanDiskObjectCache != null) {
            weatherBeanDiskObjectCache.close();
        }

        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }


    @Override
    public void stopLocation() {
        //退出时销毁定位
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }
}