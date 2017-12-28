package com.learn.liyv.learnbilibili.adapter.section;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.entity.bangumi.BangumiRecommendInfo;
import com.learn.liyv.learnbilibili.widget.sectioned.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lyh on 2017/12/28 10:25.
 * 1. 推荐番剧
 */

public class HomeBangumiRecommendSection extends StatelessSection {

    private Context mContext;
    private List<BangumiRecommendInfo.ResultBean> bangumiRecommends;

    public HomeBangumiRecommendSection(Context context, List<BangumiRecommendInfo.ResultBean> bangumiRecommends) {
        super(R.layout.layout_home_bangumi_recommend_head, R.layout.layout_home_recommend_empty);
        this.mContext = context;
        this.bangumiRecommends = bangumiRecommends;
    }


    @Override
    public int getContentItemsTotal() {
        return 1;
    }


    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new HomeBangumiRecommendSection.EmptyViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HomeBangumiRecommendSection.RecyclerViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HomeBangumiRecommendSection.RecyclerViewHolder recyclerViewHolder = (HomeBangumiRecommendSection.RecyclerViewHolder) holder;
        recyclerViewHolder.mRecyclerView.setHasFixedSize(false);
        recyclerViewHolder.mRecyclerView.setNestedScrollingEnabled(false);
    }


    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_bangumi_recommend_recycler)
        RecyclerView mRecyclerView;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
