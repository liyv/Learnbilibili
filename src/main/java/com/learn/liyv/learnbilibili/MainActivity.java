package com.learn.liyv.learnbilibili;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.learn.liyv.learnbilibili.base.RxBaseActivity;
import com.learn.liyv.learnbilibili.module.home.HomePageFragment;
import com.learn.liyv.learnbilibili.utils.ConstantUtil;
import com.learn.liyv.learnbilibili.utils.PreferenceUtil;
import com.learn.liyv.learnbilibili.utils.ToastUtil;

import butterknife.BindView;

public class MainActivity extends RxBaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    //
    private Fragment[] fragments;
    private int currentTabIndex;
    private int index;
    private long exitTime;
    private HomePageFragment mHomePageFragment;


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    public int getLayoutId() {
        return R.layout.activity_main;
    }


    public void initViews(Bundle savedInstanceState) {
        //初始化Fragment
        initFragments();
        //初始化侧滑菜单
//        initNavigationView();

    }

    private void initFragments() {
        mHomePageFragment = HomePageFragment.newInstance();
//        IFavoritesFragment mFavoritesFragment = IFavoritesFragment.newInstance();
//        HistoryFragment mHistoryFragment = HistoryFragment.newInstance();
//        AttentionPeopleFragment mAttentionPeopleFragment = AttentionPeopleFragment.newInstance();
//        ConsumeHistoryFragment mConsumeHistoryFragment = ConsumeHistoryFragment.newInstance();
//        SettingFragment mSettingFragment = SettingFragment.newInstance();
        fragments = new Fragment[]{
                mHomePageFragment
//                mFavoritesFragment,
//                mHistoryFragment,
//                mAttentionPeopleFragment,
//                mConsumeHistoryFragment,
//                mSettingFragment
        };
        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mHomePageFragment)
                .show(mHomePageFragment).commit();
    }


    public void initToolBar() {

    }

    //监听 back 键操作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(mDrawerLayout.getChildAt(1))) {
                mDrawerLayout.closeDrawers();
            } else {
                exitApp();
            }
        }
        return true;
    }

    /**
     * 双击退出App
     */
    private void exitApp() {

        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.ShortToast("再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            PreferenceUtil.remove(ConstantUtil.SWITCH_MODE_KEY);
            finish();
        }
    }
}
