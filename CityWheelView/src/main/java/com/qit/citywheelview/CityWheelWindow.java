package com.qit.citywheelview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qit.citywheelview.wheelview.OnWheelChangedListener;
import com.qit.citywheelview.wheelview.OnWheelScrollListener;
import com.qit.citywheelview.wheelview.WheelView;
import com.qit.citywheelview.wheelview.adapter.AbstractWheelTextAdapter1;
import com.qit.citywheelview.wheelview.util.CityDatabaseUtil;

import java.util.ArrayList;
import java.util.List;

public class CityWheelWindow extends PopupWindow implements View.OnClickListener {

    private WheelView wvProvince;
    private WheelView wvLeader;
    private WheelView wvCity;

    private Button btnSure;
    private Button btnCancel;

    private AddressTextAdapter provinceAdapter;
    private AddressTextAdapter cityAdapter;
    private AddressTextAdapter areaAdapter;

    private OnCityIdListener onCityIdListener;

    private int maxSize = 16;
    private int minSize = 14;

    private CityDatabaseUtil cityDatabaseUtil;

    private String strProvince = "北京";
    private String strLeader = "北京";
    private String strCity = "北京";
    private String cityId = "CN101010100|北京";

    //所有的省
    private List<String> provinces;
    //某个省的所有的市
    private List<String> leaders;
    //某个市所有的区
    private List<String> cities;

    public CityWheelWindow(final Context context) {
        super(context);

        this.initData(context.getApplicationContext());

        View view = View.inflate(context, R.layout.edit_changeaddress_pop_layout, null);

        wvProvince = (WheelView) view.findViewById(R.id.wv_address_province);
        wvLeader = (WheelView) view.findViewById(R.id.wv_address_city);
        wvCity = (WheelView) view.findViewById(R.id.wv_address_area);
        btnSure = (Button) view.findViewById(R.id.btn_myinfo_sure);
        btnCancel = (Button) view.findViewById(R.id.btn_myinfo_cancel);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //触摸外部消失
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);

        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.mypopwindow_anim_style);

        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        provinceAdapter = new AddressTextAdapter(context, provinces, 0, maxSize, minSize);
        wvProvince.setVisibleItems(5);
        wvProvince.setViewAdapter(provinceAdapter);

        cityAdapter = new AddressTextAdapter(context, leaders, 0, maxSize, minSize);
        wvLeader.setVisibleItems(5);
        wvLeader.setViewAdapter(cityAdapter);

        areaAdapter = new AddressTextAdapter(context, cities, 0, maxSize, minSize);
        wvCity.setVisibleItems(5);
        wvCity.setViewAdapter(areaAdapter);

        wvProvince.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                strProvince = currentText;
                setTextviewSize(currentText, provinceAdapter);

                //更新市
                leaders = cityDatabaseUtil.getLeaders(strProvince);
                strLeader = leaders.get(0);

                cityAdapter = new AddressTextAdapter(context, leaders, 0, maxSize, minSize);
                wvLeader.setVisibleItems(5);
                wvLeader.setViewAdapter(cityAdapter);
                wvLeader.setCurrentItem(0);
                setTextviewSize("0", cityAdapter);

                //根据市，地区联动
                cities = cityDatabaseUtil.getCities(strLeader);
                strCity = cities.get(0);

                //根据城市strCity 查询对应的ID
                String id = cityDatabaseUtil.getCityId(strCity);
                if (id != null) {
                    cityId = id;
                }

                areaAdapter = new AddressTextAdapter(context, cities, 0, maxSize, minSize);
                wvCity.setVisibleItems(5);
                wvCity.setViewAdapter(areaAdapter);
                wvCity.setCurrentItem(0);
                setTextviewSize("0", areaAdapter);
            }
        });

        wvProvince.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
            }
        });

        wvLeader.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                strLeader = currentText;
                setTextviewSize(currentText, cityAdapter);

                //根据市，地区联动
                cities = cityDatabaseUtil.getCities(strLeader);
                strCity = cities.get(0);

                //根据城市strCity 查询对应的ID
                String id = cityDatabaseUtil.getCityId(strCity);
                if (id != null) {
                    cityId = id;
                }

                areaAdapter = new AddressTextAdapter(context, cities, 0, maxSize, minSize);
                wvCity.setVisibleItems(5);
                wvCity.setViewAdapter(areaAdapter);
                wvCity.setCurrentItem(0);
                setTextviewSize("0", areaAdapter);
            }
        });

        wvLeader.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
            }
        });

        wvCity.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
                strCity = currentText;
                setTextviewSize(currentText, cityAdapter);

                //根据城市strCity 查询对应的ID
                String id = cityDatabaseUtil.getCityId(strCity);
                if (id != null) {
                    cityId = id;
                }
            }
        });

        wvCity.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, areaAdapter);
            }
        });
    }

    private void initData(Context context) {
        cityDatabaseUtil = new CityDatabaseUtil(context);
        provinces = cityDatabaseUtil.getProvince();
        strProvince = provinces.get(0);
        leaders = cityDatabaseUtil.getLeaders(strProvince);
        strLeader = leaders.get(0);
        cities = cityDatabaseUtil.getCities(strLeader);
        strCity = cities.get(0);
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter1 {

        List<String> list;

        protected AddressTextAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    private void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(14);
            } else {
                textvew.setTextSize(12);
            }
        }
    }

    public void setAddressListener(OnCityIdListener OnCityIdListener) {
        this.onCityIdListener = OnCityIdListener;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onCityIdListener != null) {
                onCityIdListener.onComplete(cityId);
            }
        } else if (v == btnCancel) {

        } else {
//			dismiss();
        }
        dismiss();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        cityDatabaseUtil.closeDb();
    }


    public interface OnCityIdListener {
        void onComplete(String cityId);
    }

}