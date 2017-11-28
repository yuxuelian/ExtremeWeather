package com.base.weather.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.base.weather.MyApplication;
import com.base.weather.R;
import com.base.weather.activity.AboutActivity;
import com.base.weather.activity.MainActivity;
import com.base.weather.activity.SettingActivity;
import com.base.weather.adapter.OnItemClickListener;
import com.base.weather.adapter.RecyclerViewDivider;
import com.base.weather.adapter.SlidingListAdapter;
import com.base.weather.adapter.draghelper.OnStartDragListener;
import com.base.weather.adapter.draghelper.SimpleItemTouchHelperCallback;
import com.base.weather.model.bean.SlidingWeatherBean;
import com.base.weather.util.StatusBarUtil;
import com.base.weather.util.bitmap.BitmapBlurUtil;
import com.base.weather.util.bitmap.BitmapFileUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orhanobut.logger.Logger;
import com.qit.citywheelview.CityWheelWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gc
 * @date 16/10/26
 */
public class SlidingMenuFragment extends Fragment
        implements OnStartDragListener, OnItemClickListener, SlidingMenu.OnClosedListener {

    private static final String TAG = "SlidingMenuFragment";

    private static final String SLIDING_BG_NAME = "slidingBlur";

    private int closeFlag = 0;

    private MainActivity mAttachActivity;

    private SlidingMenu slidingMenu;

    private ViewHolder viewHolder;

    /**
     * 侧滑中List适配器
     */
    private SlidingListAdapter slidingListAdapter;

    /**
     * 侧滑中的数据
     */
    private List<SlidingWeatherBean> weatherBeanList;

    public SlidingMenuFragment() {
        weatherBeanList = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAttachActivity = (MainActivity) context;
        slidingMenu = mAttachActivity.getSlidingMenu();
        slidingMenu.setOnClosedListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.sliding_menu_layout, container, false);
        viewHolder = new ViewHolder(inflate);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置状态栏的高度
        viewHolder.slidingMainView.setPadding(0, StatusBarUtil.getStatusBarHeight(mAttachActivity), 0, 0);

        //背景模糊
        Bitmap slidingBlur = BitmapFileUtil.getBitmap(MyApplication.CACHE_DIR, SLIDING_BG_NAME);
        if (slidingBlur != null) {
            viewHolder.slidingMainView.setBackground(new BitmapDrawable(getResources(), slidingBlur));
        } else {
            viewHolder.slidingMainView.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap blur = BitmapBlurUtil.blur(10, 25, getContext(), BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper_0), viewHolder.slidingMainView);
                    viewHolder.slidingMainView.setBackground(new BitmapDrawable(getResources(), blur));
                    BitmapFileUtil.saveBitmap(blur, MyApplication.CACHE_DIR, SLIDING_BG_NAME);
                }
            });
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewHolder.listCity.setLayoutManager(linearLayoutManager);

        viewHolder.listCity.setHasFixedSize(true);
        //设置适配器并添加监听
        slidingListAdapter = new SlidingListAdapter(weatherBeanList, this);
        viewHolder.listCity.setAdapter(slidingListAdapter);
        slidingListAdapter.setOnItemClickListener(this);

        //设置分割线
        RecyclerViewDivider recyclerViewDivider = new RecyclerViewDivider(getContext());
        viewHolder.listCity.addItemDecoration(recyclerViewDivider);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(slidingListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(viewHolder.listCity);
    }

    public void setSlidingList(List<SlidingWeatherBean> list) {
        weatherBeanList = list;
        if (slidingListAdapter != null) {
            slidingListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        //侧滑菜单中的项目被点击的时候回调
//        Toast.makeText(mAttachActivity, "当前点击position=" + position, Toast.LENGTH_SHORT).show();

        slidingMenu.showContent();
        mAttachActivity.selectViewpager(position);
    }

    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
//        Toast.makeText(mAttachActivity, String.format("位置从%d移动到%d", fromPosition, toPosition), Toast.LENGTH_SHORT).show();
        //TODO Fragment移动操作
        mAttachActivity.swapFragment(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        //当侧滑删除时 这个方法会被触发
//        Toast.makeText(mAttachActivity, "dismiss" + position, Toast.LENGTH_SHORT).show();
        SlidingWeatherBean slidingWeatherBean = weatherBeanList.get(position);

        Logger.d(slidingWeatherBean);

        String cityId = slidingWeatherBean.getCityId();
        mAttachActivity.deleteCity(cityId);
    }

    @Override
    public void onClosed() {
        switch (closeFlag) {
            case 1:
                SettingActivity.start(mAttachActivity);
                break;
            case 2:
                AboutActivity.start(mAttachActivity);
                break;
            default:
                break;
        }
        closeFlag = 0;
    }

    private void showPupWindow() {
        CityWheelWindow mCityWheelWindow = new CityWheelWindow(mAttachActivity);
        mCityWheelWindow.showAtLocation(this.getView(), Gravity.BOTTOM, 0, 0);
        mCityWheelWindow.setAddressListener(new CityWheelWindow.OnCityIdListener() {
            @Override
            public void onComplete(String cityMsg) {
                //TODO 添加城市
                mAttachActivity.addCity(cityMsg);
            }
        });
    }

    @OnClick({
            R.id.sliding_add_button,
            R.id.sliding_setting_button,
            R.id.sliding_about_button})
    public void onSlidingViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sliding_add_button:
                this.showPupWindow();
                break;
            case R.id.sliding_setting_button:
                slidingMenu.showContent();
                closeFlag = 1;
                break;
            case R.id.sliding_about_button:
                slidingMenu.showContent();
                closeFlag = 2;
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAttachActivity = null;
    }

    public static class ViewHolder {

        @BindView(R.id.list_city)
        RecyclerView listCity;

        @BindView(R.id.city_manager)
        ImageView cityManager;

        @BindView(R.id.sliding_setting_button)
        LinearLayout slidingEditCityItem;

        @BindView(R.id.app_setting)
        ImageView appSetting;

        @BindView(R.id.sliding_about_button)
        LinearLayout slidingAppSettingItem;

        @BindView(R.id.sliding_main_view)
        LinearLayout slidingMainView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
