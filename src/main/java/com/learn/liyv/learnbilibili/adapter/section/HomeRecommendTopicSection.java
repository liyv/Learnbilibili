package com.learn.liyv.learnbilibili.adapter.section;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.widget.sectioned.StatelessSection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lyh on 2017/12/10.
 */

public class HomeRecommendTopicSection extends StatelessSection {


    private Context mContext;
    private String imgUrl;
    private String title;
    private String link;

    public HomeRecommendTopicSection(Context context, String imgUrl, String title, String link) {
        super(R.layout.layout_home_recommend_topic, R.layout.layout_home_recommend_empty);
        this.mContext = context;
        this.imgUrl = imgUrl;
        this.title = title;
        this.link = link;
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
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        TopicViewHolder topicViewHolder = (TopicViewHolder) holder;
        Glide.with(mContext)
                .load(imgUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bili_default_image_tv)
                .dontAnimate()
                .into(topicViewHolder.mImageView);

        topicViewHolder.mTextView.setText(title);
        if (TextUtils.isEmpty(title)) {
            topicViewHolder.mTextView.setVisibility(View.GONE);
        } else {
            topicViewHolder.mTextView.setVisibility(View.VISIBLE);
        }
        topicViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击事件");
            }
        });
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.topic_image)
        ImageView mImageView;
        @BindView(R.id.topic_title)
        TextView mTextView;
        @BindView(R.id.card_view)
        CardView mCardView;

        TopicViewHolder(View itemView) {
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
