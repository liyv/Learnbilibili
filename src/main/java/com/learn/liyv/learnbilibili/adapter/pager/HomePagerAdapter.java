package com.learn.liyv.learnbilibili.adapter.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.module.home.live.HomeLiveFragment;
import com.learn.liyv.learnbilibili.module.home.recommend.HomeRecommendedFragment;

/**
 * Created by lyh on 2017/12/9.
 * 为什么用 FragmentPagerAdapter 而不是 FragmentPagerStateAdapter
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    private final String[] titles;
    private Fragment[] fragments;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles = context.getResources().getStringArray(R.array.sections);
        fragments = new Fragment[titles.length];
    }


    @Override
    public Fragment getItem(int position) {
        if (null == fragments[position]) {
            switch (position) {
                case 0:
                    fragments[position] = HomeLiveFragment.newInstance();
                    break;
                case 1:
                    fragments[position] = HomeRecommendedFragment.newInstance();
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
