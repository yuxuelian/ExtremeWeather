package com.as.weather.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.as.pullrefreshlib.PtrClassicFrameLayout;
import com.as.pullrefreshlib.PtrDefaultHandler;
import com.as.pullrefreshlib.PtrFrameLayout;
import com.as.pullrefreshlib.PtrHandler;
import com.as.weather.MyApplication;
import com.as.weather.R;
import com.as.weather.contract.ShowWeatherContract;
import com.as.weather.custom.view.ObservableScrollView;
import com.as.weather.custom.view.ScrollViewListener;
import com.as.weather.model.bean.WeatherBean;
import com.as.weather.presenter.ShowWeatherPresenterImpl;
import com.as.weather.ui.activity.MainActivity;
import com.as.weather.util.DeviceBarUtil;
import com.as.weather.util.bitmap.BitmapBlurUtil;
import com.as.weather.util.bitmap.BitmapFileUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author 56896
 * @date 2016/11/26
 */
public class ShowWeatherFragment extends BaseFragment<ShowWeatherContract.Presenter>
        implements ShowWeatherContract.View, PtrHandler,
        ScrollViewListener {

    private static final String TAG = "ShowWeatherFragment";
    private static final String WEATHER_MSG_FM_BG = "WEATHER_MSG_FM_BG";

    private MainActivity mAttachActivity;

    //根View
    @BindView(R.id.user_city_root_view)
    LinearLayout userCityRootView;

    //背景
    @BindView(R.id.msg_fm_iv_blur)
    ImageView msgFmIvBlur;

    //scrollView
    @BindView(R.id.observable_scroll_view)
    ObservableScrollView observableScrollView;

    //下拉刷新
    @BindView(R.id.rotate_header_web_view_frame)
    PtrClassicFrameLayout rotateHeaderWebViewFrame;

    //当前城市
    @BindView(R.id.weather_address)
    TextView weatherAddress;

    private UpdateWeatherUI updateWeatherUI;

    //背景模糊度(实际上是透明度)
    private float dim = 0;

    //当前Fragment要显示城市的ID
    private String cityId;
    private String cityText;

    /**
     * 当前Fragment要显示天气的数据
     */
    private WeatherBean weatherBean;

    public static class KeyFragment {
        public String Key;
        public ShowWeatherFragment fragment;

        public KeyFragment(String key, ShowWeatherFragment fragment) {
            Key = key;
            this.fragment = fragment;
        }

        public String getKey() {
            return Key;
        }

        public ShowWeatherFragment getFragment() {
            return fragment;
        }
    }

    public static ShowWeatherFragment newInstance(String id, WeatherBean weatherBean) {
        ShowWeatherFragment fragment = new ShowWeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putSerializable("weatherBean", weatherBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public String getCityId() {
        return cityId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAttachActivity = (MainActivity) context;

        Bundle arguments = this.getArguments();
        if (arguments != null) {
            weatherBean = (WeatherBean) arguments.getSerializable("weatherBean");
            String[] split = arguments.getString("id").split("\\|");
            cityId = split[0];
            cityText = split[1];
        }
    }

    @Override
    protected ShowWeatherContract.Presenter createPresenter() {
        return new ShowWeatherPresenterImpl(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //沉浸式
        userCityRootView.setPadding(0, DeviceBarUtil.getStatusBarHeight(mAttachActivity), 0, 0);

        updateWeatherUI = new UpdateWeatherUI(new WeatherViewHolder(view));

        //滑动监听
        observableScrollView.setScrollViewListener(this);

        //初始化背景
        this.initBackGround();

        //初始化下拉刷新
        this.initPullToRefresh();

        //更新多天星期显示
        updateWeatherUI.updateWeek();

        //更新多天日期显示
        updateWeatherUI.updateDate();

        //显示城市
        weatherAddress.setText(cityText);

        if (weatherBean != null) {
            presenter.UpdateWeatherData(weatherBean);
        } else {
            //自动进行下拉刷新
            rotateHeaderWebViewFrame.autoRefresh();
        }
    }

    private void initPullToRefresh() {
        //下拉刷新控件
        rotateHeaderWebViewFrame.setLastUpdateTimeRelateObject(this);

        //刷新保持时间至少500毫秒
        rotateHeaderWebViewFrame.setLoadingMinTime(500);
        rotateHeaderWebViewFrame.setResistance(1.7f);
        rotateHeaderWebViewFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        rotateHeaderWebViewFrame.setDurationToClose(200);

        //刷新关闭的时间
        rotateHeaderWebViewFrame.setDurationToCloseHeader(500);
        rotateHeaderWebViewFrame.setPullToRefresh(false);
        rotateHeaderWebViewFrame.setKeepHeaderWhenRefresh(true);
        rotateHeaderWebViewFrame.setPtrHandler(this);
    }

    private void initBackGround() {
        //背景模糊
        Bitmap slidingBlur = BitmapFileUtil.getBitmap(MyApplication.cache_dir, WEATHER_MSG_FM_BG);
        if (slidingBlur != null) {
            Log.i(TAG, "获取到bitmap");
            msgFmIvBlur.setImageDrawable(new BitmapDrawable(getResources(), slidingBlur));
        } else {
            Log.i(TAG, "未获取到bitmap,须转换");
            msgFmIvBlur.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap blur = BitmapBlurUtil.blur(10, 25, getContext(), BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper_0), msgFmIvBlur);
                    msgFmIvBlur.setImageDrawable(new BitmapDrawable(getResources(), blur));
                    //转换结束保存到本地
                    BitmapFileUtil.saveBitmap(blur, MyApplication.cache_dir, WEATHER_MSG_FM_BG);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //恢复透明度(模糊度)
        msgFmIvBlur.setImageAlpha((int) (this.dim * 0xff));
    }

    //判断当前所包裹的控件是否能够进行下拉刷新操作
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void refreshComplete() {
        rotateHeaderWebViewFrame.refreshComplete();
    }

    @Override
    public void showRefreshError() {
        Toast.makeText(mAttachActivity, "刷新失败,网络异常", Toast.LENGTH_SHORT).show();
    }

    //下拉到一定距离释放后这个方法开始执行进行刷新
    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        //请求数据
        presenter.UpdateWeatherData(mAttachActivity, cityId);
    }

    @Override
    public void UpdateWeatherUI(WeatherBean weatherBean) {

        LogUtils.d(weatherBean);
        if (weatherBean.getHeWeather5().get(0).getStatus().equals("unknown city")) {
            Toast.makeText(mAttachActivity, String.format("服务器没有城市[%s]对应的天气信息", cityText), Toast.LENGTH_SHORT).show();
        } else if (weatherBean.getHeWeather5().get(0).getStatus().equals("invalid key")) {
            Toast.makeText(mAttachActivity, "键不能用", Toast.LENGTH_SHORT).show();
        } else {
            presenter.saveWeatherBeanCache(mAttachActivity, cityId, weatherBean);
            updateWeatherUI.update(weatherBean);
            //TODO 更新侧滑中对应条目的天气信息

            mAttachActivity.updateSlidingMenuList(cityId, weatherBean);
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int left, int top, int oldleft, int oldtop) {
        float half = scrollView.getMeasuredHeight() * 0.5f;
        if (top >= 0) {
            if (top <= half) {
                msgFmIvBlur.setImageAlpha((int) (this.dim * 0xff));
                //保存到全局
                this.dim = (top / half);
            } else {
                msgFmIvBlur.setImageAlpha(0xff);
                //保存到全局
                this.dim = 1;
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAttachActivity = null;
    }
}
