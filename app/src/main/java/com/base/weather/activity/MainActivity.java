package com.base.weather.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.base.weather.R;
import com.base.weather.activity.base.SlidingBaseActivity;
import com.base.weather.adapter.FragmentAdapter;
import com.base.weather.contract.MainContract;
import com.base.weather.fragment.ShowWeatherFragment;
import com.base.weather.model.bean.SlidingWeatherBean;
import com.base.weather.model.bean.WeatherBean;
import com.base.weather.presenter.MainPresenterImpl;
import com.base.weather.util.StatusBarUtil;
import com.base.weather.util.UpdateUIUtil;
import com.orhanobut.logger.Logger;
import com.qit.citywheelview.wheelview.util.CityDatabaseUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * @author Administrator
 */
@RuntimePermissions
public class MainActivity extends SlidingBaseActivity<MainContract.Presenter> implements MainContract.View {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_SETTING = 300;

    //双击退出记录时间
    private long lastMillisecond;

    @BindView(R.id.main_action_menu)
    ImageView mainActionMenu;

    @BindView(R.id.main_action_share)
    ImageView mainActionShare;

    @BindView(R.id.home_layout_title_group)
    LinearLayout homeLayoutTitleGroup;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.view_magic_indicator)
    MagicIndicator mMagicIndicator;

    //所有城市的id集合
    private List<String> cityIdList;

    //每个城市对应的fragment
    private List<ShowWeatherFragment.KeyFragment> mFragments;

//    private Map<String, ShowWeatherFragment> mFragments;

    //侧滑菜单中对应的List数据
    private List<SlidingWeatherBean> slidingWeatherBeen;

    private FragmentManager fm;
    private FragmentAdapter mFragmentAdapter;

    //定位对话框
    private ProgressDialog dialog;

    @Override
    protected MainContract.Presenter createPresenter() {
        return new MainPresenterImpl(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //查找view
        ButterKnife.bind(this);

        //设置沉浸式
        homeLayoutTitleGroup.setPadding(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);

        //初始化Viewpager
        this.initViewPager();

        //viewPager添加事件监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mMagicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mMagicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mMagicIndicator.onPageScrollStateChanged(state);
            }
        });

        //请求定位权限
        MainActivityPermissionsDispatcher.needsLocationWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION})
    void needsLocation() {
        showLocalDialog();
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION})
    void rationaleLocation(final PermissionRequest request) {
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION})
    void deniedLocation() {
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION})
    void askLocation() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 定位城市
     */
    public void showLocalDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setTitle("定位提示");
            dialog.setMessage("正在定位，请稍后");
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
        //开始定位
        presenter.location(getApplication());
    }

    @Override
    public void hiddenDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
            dialog.dismiss();
        }
    }

    @Override
    public void addLocalCity(String cityMsgBean) {
        if (cityMsgBean != null) {
            //添加城市
            CityDatabaseUtil cityDatabaseUtil = new CityDatabaseUtil(this);
            String cityId = cityDatabaseUtil.getCityId(cityMsgBean.substring(0, cityMsgBean.length() - 1));
            if (cityId != null) {
                //添加城市
                addCity(cityId);
            } else {
                Toast.makeText(this, "在城市库中没有查询到当前城市的ID信息", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "定位失败 请重新定位", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViewPager() {
        fm = getSupportFragmentManager();
        cityIdList = presenter.getShowCityId();
        mFragments = new ArrayList<>();
        slidingWeatherBeen = new ArrayList<>();

        for (String city : cityIdList) {
            ShowWeatherFragment showWeatherFragment = getShowWeatherFragment(city);
            ShowWeatherFragment.KeyFragment keyFragment = new ShowWeatherFragment.KeyFragment(city, showWeatherFragment);
            mFragments.add(keyFragment);
        }
        mFragmentAdapter = new FragmentAdapter(fm, mFragments);
        mViewPager.setAdapter(mFragmentAdapter);
        mSlidingMenuFragment.setSlidingList(slidingWeatherBeen);
        this.initIndicator();
    }

    private void initIndicator() {
        CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setCircleColor(Color.WHITE);
        circleNavigator.setCircleCount(cityIdList.size());
        mMagicIndicator.setNavigator(circleNavigator);
    }

    @NonNull
    private ShowWeatherFragment getShowWeatherFragment(String city) {
        String[] split = city.split("\\|");
        WeatherBean weatherBean = presenter.readWeatherBeanCache(split[0]);
        SlidingWeatherBean slidingWeatherBean = getSlidingWeatherBean(split, weatherBean);
        slidingWeatherBeen.add(slidingWeatherBean);
        ShowWeatherFragment showWeatherFragment = ShowWeatherFragment.newInstance(city, weatherBean);
        return showWeatherFragment;
    }

    @NonNull
    private SlidingWeatherBean getSlidingWeatherBean(String[] split, WeatherBean weatherBean) {
        SlidingWeatherBean slidingWeatherBean = new SlidingWeatherBean();
        slidingWeatherBean.setCityId(split[0]);
        slidingWeatherBean.setCityText(split[1]);
        if (weatherBean != null) {
            BeanToBean(weatherBean, slidingWeatherBean);
        }
        return slidingWeatherBean;
    }

    private void BeanToBean(WeatherBean weatherBean, SlidingWeatherBean slidingWeatherBean) {
        slidingWeatherBean.setWeatherTemp(weatherBean.getHeWeather5().get(0).getNow().getTmp());
        WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean = weatherBean.getHeWeather5().get(0).getDaily_forecast().get(0);
        String sr = dailyForecastBean.getAstro().getSr();
        String ss = dailyForecastBean.getAstro().getSs();
        //设置当前天气图标
        boolean daytime = UpdateUIUtil.isDaytime(sr, ss);
        int weatherIcon = UpdateUIUtil.getWeatherIcon(weatherBean.getHeWeather5().get(0).getNow().getCond().getCode(), daytime);
        slidingWeatherBean.setWeatherIcon(weatherIcon);
        slidingWeatherBean.setShowLocationIcon(false);
        slidingWeatherBean.setCityId(weatherBean.getHeWeather5().get(0).getBasic().getId());
    }

    /**
     * 增加某个城市的操作
     *
     * @param cityMsg
     */
    public void addCity(String cityMsg) {
        //首先判断当前城市是否已经添加
        if (!cityIdList.contains(cityMsg)) {
            //添加到城市
            cityIdList.add(cityMsg);
            //保存到本地缓存
            presenter.saveShowCityId(cityIdList);
            ShowWeatherFragment showWeatherFragment = getShowWeatherFragment(cityMsg);
            ShowWeatherFragment.KeyFragment keyFragment = new ShowWeatherFragment.KeyFragment(cityMsg, showWeatherFragment);
            mFragments.add(keyFragment);
            mFragmentAdapter.notifyDataSetChanged();
            //更新Indicator
            this.initIndicator();
            //更新侧滑
            mSlidingMenuFragment.setSlidingList(slidingWeatherBeen);
            //更新顺序
//            swapFragment(cityIdList.size() - 1, 0);
        } else {
            Toast.makeText(this, "当前城市已经添加,无需重复添加", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fragment排序操作
     *
     * @param fromPosition
     * @param toPosition
     */
    public void swapFragment(int fromPosition, int toPosition) {
        //排序城市列表
        Collections.swap(cityIdList, fromPosition, toPosition);
        //重新保存到缓存
        presenter.saveShowCityId(cityIdList);
        //更新Fragment的顺序
        Collections.swap(mFragments, fromPosition, toPosition);
        //刷新界面
        mFragmentAdapter.notifyDataSetChanged();
    }

    /**
     * 向侧滑中的列表添加城市天气信息
     *
     * @param cityId
     * @param weatherBean
     */
    public void updateSlidingMenuList(String cityId, WeatherBean weatherBean) {
        for (SlidingWeatherBean bean : slidingWeatherBeen) {
            if (bean.getCityId().equals(cityId)) {
                BeanToBean(weatherBean, bean);
                //更新侧滑
                mSlidingMenuFragment.setSlidingList(slidingWeatherBeen);
            }
        }
    }

    /**
     * 删除某个城市的操作
     *
     * @param cityId
     */
    public void deleteCity(String cityId) {
        //移除所有城市列表
        Iterator<String> iterator = cityIdList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(cityId)) {
                iterator.remove();
                break;
            }
        }
        //重新保存到缓存
        presenter.saveShowCityId(cityIdList);
        //删除缓存中的天气数据
        presenter.deleteWeatherBean(cityId);
        //删除Fragment
        Iterator<ShowWeatherFragment.KeyFragment> iterator1 = mFragments.iterator();
        while (iterator1.hasNext()) {
            ShowWeatherFragment.KeyFragment next = iterator1.next();
            String getCityId = next.getKey();
            if (getCityId.equals(cityId)) {
                iterator1.remove();
                Logger.d("移除Fragment");
                break;
            }
        }
        //删除侧滑中的条目
        Iterator<SlidingWeatherBean> weatherBeanIterator = slidingWeatherBeen.iterator();
        while (weatherBeanIterator.hasNext()) {
            SlidingWeatherBean next = weatherBeanIterator.next();
            if (next.getCityId().equals(cityId)) {
                weatherBeanIterator.remove();
                break;
            }
        }
        Logger.d(slidingWeatherBeen);
        //更新Indicator
        this.initIndicator();
        //刷新Fragment
        mFragmentAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.main_action_menu, R.id.main_action_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_action_menu:
                if (!mSlidingMenu.isMenuShowing()) {
                    mSlidingMenu.showMenu();
                }
                break;
            case R.id.main_action_share:

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING:
                Toast.makeText(this, "用户从设置界面返回了", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    //返回键
    @Override
    public void onBackPressed() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastMillisecond > 2000) {
            Toast.makeText(this, "再按一次返回到桌面", Toast.LENGTH_SHORT).show();
            lastMillisecond = currentTimeMillis;
        } else {
            //程序不退出,返回到系统桌面
//            Intent home = new Intent();
//            home.setAction(Intent.ACTION_MAIN);
//            home.addCategory(Intent.CATEGORY_HOME);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(home);

            //程序退出
            super.onBackPressed();
        }
    }

    //设置选中的ViewPager页面
    public void selectViewpager(int position) {
        mViewPager.setCurrentItem(position);
    }
}
