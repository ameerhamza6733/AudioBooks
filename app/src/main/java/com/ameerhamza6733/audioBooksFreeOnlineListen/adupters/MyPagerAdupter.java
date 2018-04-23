package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.DetailFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.PlayerFragment;

/**
 * Created by apple on 4/23/18.
 */

public class MyPagerAdupter extends FragmentStatePagerAdapter {
    public static final String KEY_POSITION = "KEY_POSITION";
    private static final String TABS[] = {"Player", "Detail"};

    public MyPagerAdupter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 1:
                DetailFragment detailFragment = new DetailFragment();
                return detailFragment;
            case 0:
                PlayerFragment playerFragment = new PlayerFragment();
                return playerFragment;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }
}