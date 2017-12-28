package com.learn.liyv.learnbilibili.adapter.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.utils.ToastUtil;
import com.learn.liyv.learnbilibili.widget.sectioned.StatelessSection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lyh on 2017/12/28 10:23.
 * 1.追番 放送表、索引
 */

public class HomeBangumiItemSection extends StatelessSection {

    private Context context;

    public HomeBangumiItemSection(Context context) {
        super(R.layout.layout_home_bangumi_top_item, R.layout.layout_home_recommend_empty);
        this.context = context;
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
        return new TopItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        TopItemViewHolder topItemViewHolder = (TopItemViewHolder) holder;
        //牵网追番
        topItemViewHolder.mChaseBangumi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.ShortToast("追番");
            }
        });
        topItemViewHolder.mBangumiSchedule.setOnClickListener(v -> {
            ToastUtil.ShortToast("放送表");
        });
        topItemViewHolder.mBangumiIndex.setOnClickListener(v -> {
            ToastUtil.ShortToast("索引");
        });
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class TopItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_chase_bangumi)
        TextView mChaseBangumi;
        @BindView(R.id.layout_bangumi_schedule)
        TextView mBangumiSchedule;
        @BindView(R.id.layout_bangumi_index)
        TextView mBangumiIndex;

        TopItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
