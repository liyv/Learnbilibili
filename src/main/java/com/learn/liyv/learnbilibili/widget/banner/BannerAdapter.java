package com.learn.liyv.learnbilibili.widget.banner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by lyh on 2017/12/10.
 */

public class BannerAdapter extends PagerAdapter {
    private List<ImageView> mList;
    private int pos;
    private ViewPagerOnItemClickListener mViewPagerOnItemClickListener;


    BannerAdapter(List<ImageView> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对 viewpager 页号求模取出 view列表中显示的项
        position %= mList.size();
        if (position < 0) {
            position = mList.size() + position;
        }
        ImageView v = mList.get(position);
        pos = position;
        v.setScaleType(ImageView.ScaleType.CENTER);
        ViewParent vp = v.getParent();
        if (null != vp) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(v);
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPagerOnItemClickListener != null) {
                    mViewPagerOnItemClickListener.onItemClick();
                }
            }
        });
        container.addView(v);
        return v;
    }

    void setmViewPagerOnItemClickListener(ViewPagerOnItemClickListener viewPagerOnItemClickListener) {
        mViewPagerOnItemClickListener = viewPagerOnItemClickListener;
    }

    interface ViewPagerOnItemClickListener {
        void onItemClick();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
