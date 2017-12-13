package com.learn.liyv.learnbilibili.module.home;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.flyco.tablayout.SlidingTabLayout;
import com.learn.liyv.learnbilibili.MainActivity;
import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.adapter.pager.HomePagerAdapter;
import com.learn.liyv.learnbilibili.base.RxLazyFragment;

import butterknife.BindView;

/**
 * Created by lyh on 2017/12/9.
 */

public class HomePageFragment extends RxLazyFragment {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTab;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setHasOptionsMenu(true);
        initToolBar();
        initViewPager();
    }

    private void initToolBar() {
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);

    }

    private void initViewPager() {
        HomePagerAdapter mHomeAdapter = new HomePagerAdapter(getChildFragmentManager(), getApplicationContext());
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mHomeAdapter);
        mSlidingTab.setViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
    }
}
