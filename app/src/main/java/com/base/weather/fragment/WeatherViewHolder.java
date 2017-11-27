package com.base.weather.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.weather.R;
import com.base.customview.LineChartView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 *
 * 邮箱：568966289@qq.com
 *
 * 创建时间：2017/3/26
 */

public class WeatherViewHolder {

    public TemplateMsg templateMsg;
    public Recently3Weather recently3Weather;
    public WeatherWeek weatherWeek;
    public IndexOfLiving indexOfLiving;

    public WeatherViewHolder(View view) {
        templateMsg = new TemplateMsg(view);
        recently3Weather = new Recently3Weather(view);
        weatherWeek = new WeatherWeek(view);
        indexOfLiving = new IndexOfLiving(view);
    }

    public static class TemplateMsg {
        public TemplateMsg(View view) {
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.template_weather_update_time)
        TextView templateWeatherUpdateTime;

        @BindView(R.id.template_operator)
        ImageView templateOperator;

        @BindView(R.id.template_shi)
        ImageView templateShi;

        @BindView(R.id.template_ge)
        ImageView templateGe;

        @BindView(R.id.template_weather_now_ic)
        ImageView templateWeatherNowIc;

        @BindView(R.id.template_weather_weather)
        TextView templateWeatherWeather;

        @BindView(R.id.template_weather_temp_scope)
        TextView templateWeatherTempScope;

        @BindView(R.id.template_weather_fl)
        TextView templateWeatherFl;

        @BindView(R.id.template_weather_qlty_ic)
        ImageView templateWeatherQltyIc;

        @BindView(R.id.template_weather_qlty)
        TextView templateWeatherQlty;

        @BindView(R.id.template_weather_hum)
        TextView templateWeatherHum;

        @BindView(R.id.template_weather_sunrise)
        TextView templateWeatherSunrise;

        @BindView(R.id.template_weather_wind)
        TextView templateWeatherWind;

        @BindView(R.id.template_weather_sunset)
        TextView templateWeatherSunset;

        @BindView(R.id.template_area)
        LinearLayout templateArea;
    }

    public static class Recently3Weather {
        public Recently3Weather(View view) {
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.recently3_today_iv)
        ImageView recently3TodayIv;
        @BindView(R.id.recently3_today_max_temp)
        TextView recently3TodayMaxTemp;
        @BindView(R.id.recently3_today_min_temp)
        TextView recently3TodayMinTemp;
        @BindView(R.id.recently3_today_weather)
        TextView recently3TodayWeather;
        @BindView(R.id.recently3_tomorrow_iv)
        ImageView recently3TomorrowIv;
        @BindView(R.id.recently3_tomorrow_max_temp)
        TextView recently3TomorrowMaxTemp;
        @BindView(R.id.recently3_tomorrow_min_temp)
        TextView recently3TomorrowMinTemp;
        @BindView(R.id.recently3_tomorrow_weather)
        TextView recently3TomorrowWeather;
        @BindView(R.id.recently3_atom_iv)
        ImageView recently3AtomIv;
        @BindView(R.id.recently3_atom_max_temp)
        TextView recently3AtomMaxTemp;
        @BindView(R.id.recently3_atom_min_temp)
        TextView recently3AtomMinTemp;
        @BindView(R.id.recently3_atom_weather)
        TextView recently3AtomWeather;
        @BindView(R.id.recently3_area)
        LinearLayout recently3Area;
    }

    public static class WeatherWeek {
        public WeatherWeek(View view) {
            ButterKnife.bind(this, view);
        }

        @BindViews({R.id.weather_week_weather_week1, R.id.weather_week_weather_week2, R.id.weather_week_weather_week3,
                R.id.weather_week_weather_week4, R.id.weather_week_weather_week5, R.id.weather_week_weather_week6,})
        List<TextView> weatherWeekWeatherWeeks;

        @BindViews({R.id.weather_week_weather_date1, R.id.weather_week_weather_date2, R.id.weather_week_weather_date3,
                R.id.weather_week_weather_date4, R.id.weather_week_weather_date5, R.id.weather_week_weather_date6,})
        List<TextView> weatherWeekWeatherDates;

        @BindViews({R.id.weather_week_daysIcon1, R.id.weather_week_daysIcon2, R.id.weather_week_daysIcon3,
                R.id.weather_week_daysIcon4, R.id.weather_week_daysIcon5, R.id.weather_week_daysIcon6,})
        List<ImageView> weatherWeekDaysIcons;

        @BindViews({R.id.weather_week_daysText1, R.id.weather_week_daysText2, R.id.weather_week_daysText3,
                R.id.weather_week_daysText4, R.id.weather_week_daysText5, R.id.weather_week_daysText6,})
        List<TextView> weatherWeekDaysTexts;

        @BindView(R.id.weather_week_weather_line_chart)
        LineChartView weatherWeekLineChartView;

        @BindViews({R.id.weather_week_nightsText1, R.id.weather_week_nightsText2, R.id.weather_week_nightsText3,
                R.id.weather_week_nightsText4, R.id.weather_week_nightsText5, R.id.weather_week_nightsText6,})
        List<TextView> weatherWeekNightsTexts;

        @BindViews({R.id.weather_week_nightsIcon1, R.id.weather_week_nightsIcon2, R.id.weather_week_nightsIcon3,
                R.id.weather_week_nightsIcon4, R.id.weather_week_nightsIcon5, R.id.weather_week_nightsIcon6,})
        List<ImageView> weatherWeekNightsIcons;

        @BindViews({R.id.weather_week_windText1, R.id.weather_week_windText2, R.id.weather_week_windText3,
                R.id.weather_week_windText4, R.id.weather_week_windText5, R.id.weather_week_windText6,})
        List<TextView> weatherWeekWindTexts;

        @BindViews({R.id.weather_week_windSizeText1, R.id.weather_week_windSizeText2, R.id.weather_week_windSizeText3,
                R.id.weather_week_windSizeText4, R.id.weather_week_windSizeText5, R.id.weather_week_windSizeText6,})
        List<TextView> weatherWeekWindSizeTexts;

        @BindView(R.id.weather_week_area)
        LinearLayout weatherWeekArea;
    }

    public static class IndexOfLiving {
        public IndexOfLiving(View view) {
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.index_of_living_life_index_text1)
        TextView indexOfLivingLifeIndexText1;
        @BindView(R.id.index_of_living_life_index_text2)
        TextView indexOfLivingLifeIndexText2;
        @BindView(R.id.index_of_living_life_index_text3)
        TextView indexOfLivingLifeIndexText3;
        @BindView(R.id.index_of_living_life_index_text4)
        TextView indexOfLivingLifeIndexText4;
        @BindView(R.id.index_of_living_life_index_text5)
        TextView indexOfLivingLifeIndexText5;
        @BindView(R.id.index_of_living_life_index_text6)
        TextView indexOfLivingLifeIndexText6;
        @BindView(R.id.index_of_living_life_index_text7)
        TextView indexOfLivingLifeIndexText7;
        @BindView(R.id.index_of_living_life_index_text8)
        TextView indexOfLivingLifeIndexText8;
        @BindView(R.id.index_of_living_life_index_text9)
        TextView indexOfLivingLifeIndexText9;
        @BindView(R.id.index_of_living_life_index_text10)
        TextView indexOfLivingLifeIndexText10;
        @BindView(R.id.index_of_living_life_index_text11)
        TextView indexOfLivingLifeIndexText11;
        @BindView(R.id.index_of_living_life_index_text12)
        TextView indexOfLivingLifeIndexText12;
        @BindView(R.id.index_of_living_life_index_text13)
        TextView indexOfLivingLifeIndexText13;
        @BindView(R.id.index_of_living_life_index_text14)
        TextView indexOfLivingLifeIndexText14;
        @BindView(R.id.index_of_living_life_index_text15)
        TextView indexOfLivingLifeIndexText15;
        @BindView(R.id.index_of_living_life_index_text16)
        TextView indexOfLivingLifeIndexText16;
    }
}
