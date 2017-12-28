package com.learn.liyv.learnbilibili.adapter.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.widget.banner.BannerEntity;
import com.learn.liyv.learnbilibili.widget.banner.BannerView;
import com.learn.liyv.learnbilibili.widget.sectioned.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lyh on 2017/12/28 10:23.
 * 1. 首页番剧轮播图 Section
 */

public class HomeBangumiBannerSection extends StatelessSection {

    private List<BannerEntity> banners;

    public HomeBangumiBannerSection(List<BannerEntity> bannerEntities) {
        super(R.layout.layout_banner, R.layout.layout_home_recommend_empty);
        banners = bannerEntities;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
        bannerViewHolder.bannerView.delayTime(5).build(banners);

    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View view) {
            super(view);
        }
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_recommend_banner)
        BannerView bannerView;

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
