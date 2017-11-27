package com.base.weather.holder;

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
 * 邮箱：568966289@qq.com
 * <p>
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
        public TextView templateWeatherUpdateTime;

        @BindView(R.id.template_operator)
        public ImageView templateOperator;

        @BindView(R.id.template_shi)
        public ImageView templateShi;

        @BindView(R.id.template_ge)
        public ImageView templateGe;

        @BindView(R.id.template_weather_now_ic)
        public ImageView templateWeatherNowIc;

        @BindView(R.id.template_weather_weather)
        public TextView templateWeatherWeather;

        @BindView(R.id.template_weather_temp_scope)
        public TextView templateWeatherTempScope;

        @BindView(R.id.template_weather_fl)
        public TextView templateWeatherFl;

        @BindView(R.id.template_weather_qlty_ic)
        public ImageView templateWeatherQltyIc;

        @BindView(R.id.template_weather_qlty)
        public TextView templateWeatherQlty;

        @BindView(R.id.template_weather_hum)
        public TextView templateWeatherHum;

        @BindView(R.id.template_weather_sunrise)
        public TextView templateWeatherSunrise;

        @BindView(R.id.template_weather_wind)
        public TextView templateWeatherWind;

        @BindView(R.id.template_weather_sunset)
        public TextView templateWeatherSunset;

        @BindView(R.id.template_area)
        public LinearLayout templateArea;
    }

    public static class Recently3Weather {
        public Recently3Weather(View view) {
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.recently3_today_iv)
        public ImageView recently3TodayIv;
        @BindView(R.id.recently3_today_max_temp)
        public TextView recently3TodayMaxTemp;
        @BindView(R.id.recently3_today_min_temp)
        public TextView recently3TodayMinTemp;
        @BindView(R.id.recently3_today_weather)
        public TextView recently3TodayWeather;
        @BindView(R.id.recently3_tomorrow_iv)
        public ImageView recently3TomorrowIv;
        @BindView(R.id.recently3_tomorrow_max_temp)
        public TextView recently3TomorrowMaxTemp;
        @BindView(R.id.recently3_tomorrow_min_temp)
        public TextView recently3TomorrowMinTemp;
        @BindView(R.id.recently3_tomorrow_weather)
        public TextView recently3TomorrowWeather;
        @BindView(R.id.recently3_atom_iv)
        public ImageView recently3AtomIv;
        @BindView(R.id.recently3_atom_max_temp)
        public TextView recently3AtomMaxTemp;
        @BindView(R.id.recently3_atom_min_temp)
        public TextView recently3AtomMinTemp;
        @BindView(R.id.recently3_atom_weather)
        public TextView recently3AtomWeather;
        @BindView(R.id.recently3_area)
        public LinearLayout recently3Area;
    }

    public static class WeatherWeek {
        public WeatherWeek(View view) {
            ButterKnife.bind(this, view);
        }

        @BindViews({R.id.weather_week_weather_week1, R.id.weather_week_weather_week2, R.id.weather_week_weather_week3,
                R.id.weather_week_weather_week4, R.id.weather_week_weather_week5, R.id.weather_week_weather_week6,})
        public List<TextView> weatherWeekWeatherWeeks;

        @BindViews({R.id.weather_week_weather_date1, R.id.weather_week_weather_date2, R.id.weather_week_weather_date3,
                R.id.weather_week_weather_date4, R.id.weather_week_weather_date5, R.id.weather_week_weather_date6,})
        public List<TextView> weatherWeekWeatherDates;

        @BindViews({R.id.weather_week_daysIcon1, R.id.weather_week_daysIcon2, R.id.weather_week_daysIcon3,
                R.id.weather_week_daysIcon4, R.id.weather_week_daysIcon5, R.id.weather_week_daysIcon6,})
        public List<ImageView> weatherWeekDaysIcons;

        @BindViews({R.id.weather_week_daysText1, R.id.weather_week_daysText2, R.id.weather_week_daysText3,
                R.id.weather_week_daysText4, R.id.weather_week_daysText5, R.id.weather_week_daysText6,})
        public List<TextView> weatherWeekDaysTexts;

        @BindView(R.id.weather_week_weather_line_chart)
        public LineChartView weatherWeekLineChartView;

        @BindViews({R.id.weather_week_nightsText1, R.id.weather_week_nightsText2, R.id.weather_week_nightsText3,
                R.id.weather_week_nightsText4, R.id.weather_week_nightsText5, R.id.weather_week_nightsText6,})
        public List<TextView> weatherWeekNightsTexts;

        @BindViews({R.id.weather_week_nightsIcon1, R.id.weather_week_nightsIcon2, R.id.weather_week_nightsIcon3,
                R.id.weather_week_nightsIcon4, R.id.weather_week_nightsIcon5, R.id.weather_week_nightsIcon6,})
        public List<ImageView> weatherWeekNightsIcons;

        @BindViews({R.id.weather_week_windText1, R.id.weather_week_windText2, R.id.weather_week_windText3,
                R.id.weather_week_windText4, R.id.weather_week_windText5, R.id.weather_week_windText6,})
        public List<TextView> weatherWeekWindTexts;

        @BindViews({R.id.weather_week_windSizeText1, R.id.weather_week_windSizeText2, R.id.weather_week_windSizeText3,
                R.id.weather_week_windSizeText4, R.id.weather_week_windSizeText5, R.id.weather_week_windSizeText6,})
        public List<TextView> weatherWeekWindSizeTexts;

        @BindView(R.id.weather_week_area)
        public LinearLayout weatherWeekArea;
    }

    public static class IndexOfLiving {
        public IndexOfLiving(View view) {
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.index_of_living_life_index_text1)
        public TextView indexOfLivingLifeIndexText1;
        @BindView(R.id.index_of_living_life_index_text2)
        public TextView indexOfLivingLifeIndexText2;
        @BindView(R.id.index_of_living_life_index_text3)
        public TextView indexOfLivingLifeIndexText3;
        @BindView(R.id.index_of_living_life_index_text4)
        public TextView indexOfLivingLifeIndexText4;
        @BindView(R.id.index_of_living_life_index_text5)
        public TextView indexOfLivingLifeIndexText5;
        @BindView(R.id.index_of_living_life_index_text6)
        public TextView indexOfLivingLifeIndexText6;
        @BindView(R.id.index_of_living_life_index_text7)
        public TextView indexOfLivingLifeIndexText7;
        @BindView(R.id.index_of_living_life_index_text8)
        public TextView indexOfLivingLifeIndexText8;
        @BindView(R.id.index_of_living_life_index_text9)
        public TextView indexOfLivingLifeIndexText9;
        @BindView(R.id.index_of_living_life_index_text10)
        public TextView indexOfLivingLifeIndexText10;
        @BindView(R.id.index_of_living_life_index_text11)
        public TextView indexOfLivingLifeIndexText11;
        @BindView(R.id.index_of_living_life_index_text12)
        public TextView indexOfLivingLifeIndexText12;
        @BindView(R.id.index_of_living_life_index_text13)
        public TextView indexOfLivingLifeIndexText13;
        @BindView(R.id.index_of_living_life_index_text14)
        public TextView indexOfLivingLifeIndexText14;
        @BindView(R.id.index_of_living_life_index_text15)
        public TextView indexOfLivingLifeIndexText15;
        @BindView(R.id.index_of_living_life_index_text16)
        public TextView indexOfLivingLifeIndexText16;
    }
}
