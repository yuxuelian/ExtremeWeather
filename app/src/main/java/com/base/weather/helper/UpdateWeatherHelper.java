package com.base.weather.helper;

import android.view.View;

import com.base.weather.holder.WeatherViewHolder;
import com.base.weather.model.bean.WeatherBean;
import com.base.weather.util.DateUtil;
import com.base.weather.util.UpdateUIUtil;

import java.util.Calendar;
import java.util.List;

/**
 * 邮箱：568966289@qq.com
 * 创建时间：2017/3/27
 * 用于更新UI
 * 更新UI帮助类
 *
 * @author Administrator
 */

public class UpdateWeatherHelper {

    private static final String[] Week = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private WeatherViewHolder viewHolder;

    public UpdateWeatherHelper(WeatherViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public void update(WeatherBean weatherBean) {
        this.updateTemplateMsg(weatherBean);
        this.updateRecently3Weather(weatherBean);
        this.updateWeatherWeek(weatherBean);
        this.updateIndexOfLiving(weatherBean);
    }

    //更新当前天气
    private void updateTemplateMsg(WeatherBean weatherBean) {
        WeatherViewHolder.TemplateMsg templateMsg = viewHolder.templateMsg;
        WeatherBean.HeWeather5Bean.NowBean now = weatherBean.getHeWeather5().get(0).getNow();

        //更新时间
        String loc = weatherBean.getHeWeather5().get(0).getBasic().getUpdate().getLoc();
        templateMsg.templateWeatherUpdateTime.setText(loc.substring(11) + "发布");

        //设置当前温度
        this.setNowTem(now.getTmp());

        //设置体感温度
        templateMsg.templateWeatherFl.setText("体感温度" + now.getFl() + "℃");

        //设置湿度
        templateMsg.templateWeatherHum.setText("湿度" + now.getHum() + "%");

        //设置空气质量
//        String qlty = weatherBean.getHeWeather5().get(0).getAqi().getCity().getQlty();
//        templateMsg.templateWeatherQlty.setText(qlty);
//        templateMsg.templateWeatherQltyIc.setImageResource(UpdateUIUtil.getQltyIcon(qlty));

        //设置风力风向
        String wind = now.getWind().getDir() + " " + now.getWind().getSc() + "级";
        templateMsg.templateWeatherWind.setText(wind);

        //设置日出日落
        WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean = weatherBean.getHeWeather5().get(0).getDaily_forecast().get(0);
        String sr = dailyForecastBean.getAstro().getSr();
        String ss = dailyForecastBean.getAstro().getSs();
        templateMsg.templateWeatherSunrise.setText("日出 " + sr);
        templateMsg.templateWeatherSunset.setText("日落 " + ss);

        //设置当前天气图标
        boolean daytime = UpdateUIUtil.isDaytime(sr, ss);
        int weatherIcon = UpdateUIUtil.getWeatherIcon(now.getCond().getCode(), daytime);
        templateMsg.templateWeatherNowIc.setImageResource(weatherIcon);

        //设置当前天气
        templateMsg.templateWeatherWeather.setText(now.getCond().getTxt());

        //设置最高最低温度
        String maxMin = dailyForecastBean.getTmp().getMin() + "~" + dailyForecastBean.getTmp().getMax();
        templateMsg.templateWeatherTempScope.setText(maxMin);
    }

    private void setNowTem(String temp) {
        WeatherViewHolder.TemplateMsg templateMsg = viewHolder.templateMsg;

        int t = Integer.parseInt(temp);
        if (t >= 0) {
            templateMsg.templateOperator.setVisibility(View.GONE);
        } else {
            t = -t;
            templateMsg.templateOperator.setVisibility(View.VISIBLE);
        }
        int shi = t / 10;
        if (shi == 0) {
            templateMsg.templateShi.setVisibility(View.GONE);
        } else {
            templateMsg.templateShi.setVisibility(View.VISIBLE);
            templateMsg.templateShi.setImageResource(UpdateUIUtil.TEMP_NUM[shi]);
        }
        templateMsg.templateGe.setImageResource(UpdateUIUtil.TEMP_NUM[t % 10]);
    }

    //更新近三天天气
    private void updateRecently3Weather(WeatherBean weatherBean) {
        List<WeatherBean.HeWeather5Bean.DailyForecastBean> daily_forecast = weatherBean.getHeWeather5().get(0).getDaily_forecast();
        WeatherViewHolder.Recently3Weather recently3Weather = viewHolder.recently3Weather;

        WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean0 = daily_forecast.get(0);
        WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean1 = daily_forecast.get(1);
        WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean2 = daily_forecast.get(2);

        //今天
        int weatherIcon0 = UpdateUIUtil.getWeatherIcon(dailyForecastBean0.getCond().getCode_d(), true);
        recently3Weather.recently3TodayIv.setImageResource(weatherIcon0);
        recently3Weather.recently3TodayMaxTemp.setText(dailyForecastBean0.getTmp().getMax() + "℃");
        recently3Weather.recently3TodayMinTemp.setText(dailyForecastBean0.getTmp().getMin() + "℃");
        recently3Weather.recently3TodayWeather.setText(dailyForecastBean0.getCond().getTxt_d());

        //明天
        int weatherIcon1 = UpdateUIUtil.getWeatherIcon(dailyForecastBean1.getCond().getCode_d(), true);
        recently3Weather.recently3TomorrowIv.setImageResource(weatherIcon1);
        recently3Weather.recently3TomorrowMaxTemp.setText(dailyForecastBean1.getTmp().getMax() + "℃");
        recently3Weather.recently3TomorrowMinTemp.setText(dailyForecastBean1.getTmp().getMin() + "℃");
        recently3Weather.recently3TomorrowWeather.setText(dailyForecastBean1.getCond().getTxt_d());

        //后天
        int weatherIcon2 = UpdateUIUtil.getWeatherIcon(dailyForecastBean2.getCond().getCode_d(), true);
        recently3Weather.recently3AtomIv.setImageResource(weatherIcon2);
        recently3Weather.recently3AtomMaxTemp.setText(dailyForecastBean2.getTmp().getMax() + "℃");
        recently3Weather.recently3AtomMinTemp.setText(dailyForecastBean2.getTmp().getMin() + "℃");
        recently3Weather.recently3AtomWeather.setText(dailyForecastBean2.getCond().getTxt_d());
    }

    //更新近一周天气
    private void updateWeatherWeek(WeatherBean weatherBean) {
        WeatherViewHolder.WeatherWeek weatherWeek = viewHolder.weatherWeek;

        List<WeatherBean.HeWeather5Bean.DailyForecastBean> daily_forecast = weatherBean.getHeWeather5().get(0).getDaily_forecast();
        int[] maxTemps = new int[6];
        int[] minTemps = new int[6];
        for (int i = 1; i < 7; i++) {
            WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean = daily_forecast.get(i);
            String max = dailyForecastBean.getTmp().getMax();
            maxTemps[i - 1] = Integer.parseInt(max);

            String min = dailyForecastBean.getTmp().getMin();
            minTemps[i - 1] = Integer.parseInt(min);
        }

        //更新天气走势图
        weatherWeek.weatherWeekLineChartView.setMaxTemps(maxTemps);
        weatherWeek.weatherWeekLineChartView.setMinTemps(minTemps);

        //白天
        for (int i = 0; i < 6; i++) {
            WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean = daily_forecast.get(i + 1);
            String code_d = dailyForecastBean.getCond().getCode_d();
            int weatherIconD = UpdateUIUtil.getWeatherIcon(code_d, true);

            //白天
            weatherWeek.weatherWeekDaysIcons.get(i).setImageResource(weatherIconD);
            weatherWeek.weatherWeekDaysTexts.get(i).setText(dailyForecastBean.getCond().getTxt_d());

            String code_n = dailyForecastBean.getCond().getCode_n();
            int weatherIconN = UpdateUIUtil.getWeatherIcon(code_n, false);
            //晚上
            weatherWeek.weatherWeekNightsIcons.get(i).setImageResource(weatherIconN);
            weatherWeek.weatherWeekNightsTexts.get(i).setText(dailyForecastBean.getCond().getTxt_n());

            //风向
            weatherWeek.weatherWeekWindTexts.get(i).setText(dailyForecastBean.getWind().getDir());
            //风力
            weatherWeek.weatherWeekWindSizeTexts.get(i).setText(dailyForecastBean.getWind().getSc() + "级");
        }
    }

    //更新生活指数
    private void updateIndexOfLiving(WeatherBean weatherBean) {
        WeatherBean.HeWeather5Bean.SuggestionBean suggestion = weatherBean.getHeWeather5().get(0).getSuggestion();
//        WeatherBean.HeWeather5Bean.SuggestionBean.AirBean air = suggestion.getAir();
        WeatherBean.HeWeather5Bean.SuggestionBean.ComfBean comf = suggestion.getComf();
        WeatherBean.HeWeather5Bean.SuggestionBean.CwBean cw = suggestion.getCw();
        WeatherBean.HeWeather5Bean.SuggestionBean.DrsgBean drsg = suggestion.getDrsg();
        WeatherBean.HeWeather5Bean.SuggestionBean.FluBean flu = suggestion.getFlu();
        WeatherBean.HeWeather5Bean.SuggestionBean.SportBean sport = suggestion.getSport();
        WeatherBean.HeWeather5Bean.SuggestionBean.TravBean trav = suggestion.getTrav();
        WeatherBean.HeWeather5Bean.SuggestionBean.UvBean uv = suggestion.getUv();

        //空气指数
//        viewHolder.indexOfLiving.indexOfLivingLifeIndexText1.setText(air.getBrf());

        //舒适度指数
        viewHolder.indexOfLiving.indexOfLivingLifeIndexText3.setText(comf.getBrf());

        //洗车指数
        viewHolder.indexOfLiving.indexOfLivingLifeIndexText5.setText(cw.getBrf());

        //穿衣指数
        viewHolder.indexOfLiving.indexOfLivingLifeIndexText7.setText(drsg.getBrf());

        //感冒指数
        viewHolder.indexOfLiving.indexOfLivingLifeIndexText9.setText(flu.getBrf());

        //运动指数
        viewHolder.indexOfLiving.indexOfLivingLifeIndexText11.setText(sport.getBrf());

        //旅游指数
        viewHolder.indexOfLiving.indexOfLivingLifeIndexText13.setText(trav.getBrf());

        //紫外线
        viewHolder.indexOfLiving.indexOfLivingLifeIndexText15.setText(uv.getBrf());
    }

    //更新多天显示的星期
    public void updateWeek() {
        int i = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        viewHolder.weatherWeek.weatherWeekWeatherWeeks.get(2).setText(Week[(i + 5) % 7]);
        viewHolder.weatherWeek.weatherWeekWeatherWeeks.get(3).setText(Week[(i + 6) % 7]);
        viewHolder.weatherWeek.weatherWeekWeatherWeeks.get(4).setText(Week[(i + 7) % 7]);
        viewHolder.weatherWeek.weatherWeekWeatherWeeks.get(5).setText(Week[(i + 8) % 7]);
    }

    //更新多天显示的日期
    public void updateDate() {
        viewHolder.weatherWeek.weatherWeekWeatherDates.get(0).setText(DateUtil.getFutureDate(1));
        viewHolder.weatherWeek.weatherWeekWeatherDates.get(1).setText(DateUtil.getFutureDate(2));
        viewHolder.weatherWeek.weatherWeekWeatherDates.get(2).setText(DateUtil.getFutureDate(3));
        viewHolder.weatherWeek.weatherWeekWeatherDates.get(3).setText(DateUtil.getFutureDate(4));
        viewHolder.weatherWeek.weatherWeekWeatherDates.get(4).setText(DateUtil.getFutureDate(5));
        viewHolder.weatherWeek.weatherWeekWeatherDates.get(5).setText(DateUtil.getFutureDate(6));
    }
}
