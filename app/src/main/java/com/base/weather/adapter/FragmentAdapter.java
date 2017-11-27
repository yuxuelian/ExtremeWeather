package com.base.weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.base.weather.fragment.ShowWeatherFragment;

import java.util.List;

/**
 * Created by 56896 on 2016/11/26.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<ShowWeatherFragment.KeyFragment> fragments;

    public FragmentAdapter(FragmentManager fm, List<ShowWeatherFragment.KeyFragment> fragmentLists) {
        super(fm);
        this.fragments = fragmentLists;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }
}
