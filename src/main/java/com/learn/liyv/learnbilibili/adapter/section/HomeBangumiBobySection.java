package com.learn.liyv.learnbilibili.adapter.section;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.entity.bangumi.BangumiAppIndexInfo;
import com.learn.liyv.learnbilibili.utils.ToastUtil;
import com.learn.liyv.learnbilibili.widget.sectioned.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lyh on 2017/12/28 10:24.
 * 1.
 */

public class HomeBangumiBobySection extends StatelessSection {


    private Context context;
    private List<BangumiAppIndexInfo.ResultBean.AdBean.BodyBean> bangumibobys;

    public HomeBangumiBobySection(Context context, List<BangumiAppIndexInfo.ResultBean.AdBean.BodyBean> bangumibobys) {
        super(R.layout.layout_home_bangumi_boby, R.layout.layout_home_recommend_empty);
        this.context = context;
        this.bangumibobys = bangumibobys;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new BangumiBobyViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        BangumiBobyViewHolder bangumiBobyViewHolder = (BangumiBobyViewHolder) holder;

        Glide.with(context)
                .load(bangumibobys.get(0).getImg())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bili_default_image_tv)
                .dontAnimate()
                .into(bangumiBobyViewHolder.mBobyImage);

        bangumiBobyViewHolder.mCardView.setOnClickListener(v -> {
            ToastUtil.ShortToast("点击Card");
        });
    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class BangumiBobyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_bangumi_boby_image)
        ImageView mBobyImage;
        @BindView(R.id.card_view)
        CardView mCardView;

        BangumiBobyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
