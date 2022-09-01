package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.ChaptersFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.DetailFragment;

/**
 * Created by AmeerHamza on 4/23/18.
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
                ChaptersFragment playerFragment = new ChaptersFragment();
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