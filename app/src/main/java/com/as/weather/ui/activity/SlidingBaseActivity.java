package com.as.weather.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.as.weather.R;
import com.as.weather.contract.BasePresenter;
import com.as.weather.ui.fragment.SlidingMenuFragment;
import com.as.weather.util.DeviceBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Sliding
 *
 * @param <T>
 */
public abstract class SlidingBaseActivity<T extends BasePresenter> extends SlidingFragmentActivity {

    protected T presenter;

    protected abstract T createPresenter();

    protected SlidingMenu mSlidingMenu;
    protected SlidingMenuFragment mSlidingMenuFragment;

//    public int getNavigationBarHeight() {
//        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
//        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
//        if (!hasMenuKey && !hasBackKey) {
//            Resources resources = getResources();
//            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//            //获取NavigationBar的高度
//            int height = resources.getDimensionPixelSize(resourceId);
//            return height;
//        } else {
//            return 0;
//        }
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceBarUtil.setStatusBarAlpha(getWindow(), 0);

        presenter = this.createPresenter();

        // 先给侧滑菜单设置一个布局,此布局中只有一个FrameLayout,
        // 然后使用FragmentManager将Fragment替换掉此FrameLayout,从而降低耦合度
        setBehindContentView(R.layout.menu_frame);

        mSlidingMenuFragment = new SlidingMenuFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, mSlidingMenuFragment).commit(); // 左菜单

        // 获取SlidingMenu
        mSlidingMenu = getSlidingMenu();

        // 设置滑动菜单视图的宽度
        mSlidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);

        //默认设置为边界
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        // 设置阴影图片的宽度
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);

        // 设置阴影图片
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);

        //设置侧滑菜单的宽度
        mSlidingMenu.setBehindWidthRes(R.dimen.sliding_menu_width);

//        int widthPixels = getWidthPixels();
//        mSlidingMenu.setBehindWidth((int) (widthPixels * 0.618));

        mSlidingMenu.setFadeEnabled(true);

        // 设置渐入渐出效果的值
        mSlidingMenu.setFadeDegree(0.8f);

        // 为侧滑菜单设置布局,为了降低耦合度,一般不用这个
        //mSlidingMenu.setMenu(R.layout.leftmenu);
        // 设置actionBar能否跟随侧滑栏移动，如果没有，则可以去掉
//        setSlidingActionBarEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
