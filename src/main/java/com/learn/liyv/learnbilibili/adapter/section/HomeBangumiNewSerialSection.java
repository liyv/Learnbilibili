package com.learn.liyv.learnbilibili.adapter.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.entity.bangumi.BangumiAppIndexInfo;
import com.learn.liyv.learnbilibili.utils.NumberUtil;
import com.learn.liyv.learnbilibili.utils.ToastUtil;
import com.learn.liyv.learnbilibili.widget.sectioned.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lyh on 2017/12/28 10:24.
 * 1.
 */

public class HomeBangumiNewSerialSection extends StatelessSection {

    private Context context;
    private List<BangumiAppIndexInfo.ResultBean.SerializingBean> newBangumiSerials;

    public HomeBangumiNewSerialSection(Context context, List<BangumiAppIndexInfo.ResultBean.SerializingBean> list) {
        super(R.layout.layout_home_bangumi_new_serial_head, R.layout.layout_home_bangumi_new_serial_body);
        this.context = context;
        this.newBangumiSerials = list;
    }


    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        BangumiAppIndexInfo.ResultBean.SerializingBean serializingBean = newBangumiSerials.get(position);
        Glide.with(context)
                .load(serializingBean.getCover())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bili_default_image_tv)
                .dontAnimate()
                .into(itemViewHolder.mImage);
        itemViewHolder.mTitle.setText(serializingBean.getTitle());
        itemViewHolder.mPlay.setText(
                NumberUtil.convertString(serializingBean.getWatching_count()) + "人在看"
        );
        itemViewHolder.mUpdate.setText("更新至第" + serializingBean.getNewest_ep_index() + "话");
        itemViewHolder.mCardView.setOnClickListener(v -> {
            ToastUtil.ShortToast("点击cardview");
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.mAllSerial.setOnClickListener(v -> {
            ToastUtil.ShortToast("更多...");
        });
    }

    @Override
    public int getContentItemsTotal() {
        return newBangumiSerials.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_all_serial)
        TextView mAllSerial;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        LinearLayout mCardView;

        @BindView(R.id.item_img)
        ImageView mImage;

        @BindView(R.id.item_title)
        TextView mTitle;

        @BindView(R.id.item_play)
        TextView mPlay;

        @BindView(R.id.item_update)
        TextView mUpdate;


        public ItemViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
