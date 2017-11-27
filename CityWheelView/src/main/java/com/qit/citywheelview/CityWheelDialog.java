package com.qit.citywheelview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.qit.citywheelview.wheelview.OnWheelChangedListener;
import com.qit.citywheelview.wheelview.OnWheelScrollListener;
import com.qit.citywheelview.wheelview.WheelView;
import com.qit.citywheelview.wheelview.adapter.AbstractWheelTextAdapter1;
import com.qit.citywheelview.wheelview.util.CityDatabaseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * 邮箱：568966289@qq.com
 *
 * 创建时间：2017/4/19 14:44
 * 备注：
 */

public class CityWheelDialog extends Dialog {

    private Button mButtonGetComplete;
    private Button mButtonGetCancel;

    private WheelView wvProvince;
    private WheelView wvLeader;
    private WheelView wvCity;

    private String strProvince = "北京";
    private String strLeader = "北京";
    private String strCity = "北京";

    private AddressTextAdapter provinceAdapter;
    private AddressTextAdapter cityAdapter;
    private AddressTextAdapter areaAdapter;

    private CityDatabaseUtil cityDatabaseUtil;

    private OnCitySelectedListener listener;

    public void setSelectedListener(OnCitySelectedListener listener) {
        this.listener = listener;
    }

    private int maxSize = 16;
    private int minSize = 14;

    //所有的省
    private List<String> provinces;
    //某个省的所有的市
    private List<String> leaders;
    //某个市所有的区
    private List<String> cities;

    public CityWheelDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CityWheelDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected CityWheelDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(final Context context) {
        initData(context);

        View view = View.inflate(context, R.layout.edit_changeaddress_dialog_layout, null);

        wvProvince = (WheelView) view.findViewById(R.id.wv_address_province);
        wvLeader = (WheelView) view.findViewById(R.id.wv_address_city);
        wvCity = (WheelView) view.findViewById(R.id.wv_address_area);

        mButtonGetComplete = (Button) view.findViewById(R.id.button_get_complete);
        mButtonGetCancel = (Button) view.findViewById(R.id.button_get_cancel);

        mButtonGetComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onComplete(strProvince, strLeader, strCity);
                }
                dismiss();
            }
        });

        mButtonGetCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.setContentView(view);
        this.initWindow();

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

    private void initWindow() {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (0.90 * getScreenWidth(getContext()));
        if (lp.width > dp2px(getContext(), 480)) {
            lp.width = dp2px(getContext(), 480);
        }
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        win.setAttributes(lp);
    }


    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
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

    @Override
    public void dismiss() {
        super.dismiss();
        cityDatabaseUtil.closeDb();
    }


    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnCitySelectedListener {
        void onComplete(String strProvince, String strLeader, String strCity);
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

}
