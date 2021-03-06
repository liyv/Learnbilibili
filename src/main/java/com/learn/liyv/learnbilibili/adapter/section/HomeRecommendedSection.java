package com.learn.liyv.learnbilibili.adapter.section;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.entity.recommend.RecommendInfo;
import com.learn.liyv.learnbilibili.utils.DisplayUtil;
import com.learn.liyv.learnbilibili.widget.sectioned.StatelessSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lyh on 2017/12/10.
 */

public class HomeRecommendedSection extends StatelessSection {
    public static final String TAG = "推荐";
    private static final String TYPE_RECOMMENDED = "recommend";
    private static final String TYPE_LIVE = "live";
    private static final String TYPE_BANGUMI = "bangumi_2";
    private static final String GOTO_BANGUMI = "bangumi_list";
    private static final String TYPE_ACTIVITY = "activity";
    private final Random mRandom;
    private Context mContext;
    private String title;
    private String type;
    private int liveCount;
    private List<RecommendInfo.ResultBean.BodyBean> datas = new ArrayList<>();
    private int[] icons = new int[]{
            R.drawable.ic_header_hot, R.drawable.ic_head_live,
            R.drawable.ic_category_t13, R.drawable.ic_category_t1,
            R.drawable.ic_category_t3, R.drawable.ic_category_t129,
            R.drawable.ic_category_t4, R.drawable.ic_category_t119,
            R.drawable.ic_category_t36, R.drawable.ic_category_t160,
            R.drawable.ic_category_t155, R.drawable.ic_category_t5,
            R.drawable.ic_category_t11, R.drawable.ic_category_t23
    };

    public HomeRecommendedSection(Context context, String title, String type, int liveCount, List<RecommendInfo.ResultBean.BodyBean> datas) {
        super(R.layout.layout_home_recommend_head, R.layout.layout_home_recommend_foot, R.layout.layout_home_recommend_boby);
        this.mContext = context;
        this.title = title;
        this.type = type;
        this.liveCount = liveCount;
        this.datas = datas;
        mRandom = new Random();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        Log.e(TAG, "getItemViewHolder: 内容");
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "onBindItemViewHolder: 绑定内容");
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final RecommendInfo.ResultBean.BodyBean bodyBean = datas.get(position);

        Glide.with(mContext)
                .load(Uri.parse(bodyBean.getCover()))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bili_default_image_tv)
                .dontAnimate()
                .into(itemViewHolder.mVideoImg);

        itemViewHolder.mVideoTitle.setText(bodyBean.getTitle());
        itemViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gotoX=bodyBean.getGotoX();
                switch (gotoX)
                {
                    case TYPE_LIVE:
                    case GOTO_BANGUMI:
                        break;
                    default:

                }
                System.out.println("点击卡片");
            }
        });

        switch (type) {
            case TYPE_LIVE:
                //直播item
                itemViewHolder.mLiveLayout.setVisibility(View.VISIBLE);
                itemViewHolder.mVideoLayout.setVisibility(View.GONE);
                itemViewHolder.mBangumiLayout.setVisibility(View.GONE);
                itemViewHolder.mLiveUp.setText(bodyBean.getUp());
                itemViewHolder.mLiveOnline.setText(String.valueOf(bodyBean.getOnline()));
                break;
            case TYPE_BANGUMI:
                // 番剧item
                itemViewHolder.mLiveLayout.setVisibility(View.GONE);
                itemViewHolder.mVideoLayout.setVisibility(View.GONE);
                itemViewHolder.mBangumiLayout.setVisibility(View.VISIBLE);
                itemViewHolder.mBangumiUpdate.setText(bodyBean.getDesc1());
                break;
            case TYPE_ACTIVITY:
                ViewGroup.LayoutParams layoutParams = itemViewHolder.mCardView.getLayoutParams();
                layoutParams.height = DisplayUtil.dp2px(mContext, 200f);
                itemViewHolder.mCardView.setLayoutParams(layoutParams);
                itemViewHolder.mLiveLayout.setVisibility(View.GONE);
                itemViewHolder.mVideoLayout.setVisibility(View.GONE);
                itemViewHolder.mBangumiLayout.setVisibility(View.GONE);
                break;
            default:
                itemViewHolder.mLiveLayout.setVisibility(View.GONE);
                itemViewHolder.mBangumiLayout.setVisibility(View.GONE);
                itemViewHolder.mVideoLayout.setVisibility(View.VISIBLE);
                itemViewHolder.mVideoPlayNum.setText(bodyBean.getPlay());
                itemViewHolder.mVideoReviewCount.setText(bodyBean.getDanmaku());
                break;
        }
    }

    @Override
    public int getContentItemsTotal() {
        return datas.size();
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        Log.e(TAG, "getHeaderViewHolder: 头部");
        return new HeaderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        Log.e(TAG, "onBindHeaderViewHolder: 绑定头部");
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        setTypeIcon(headerViewHolder);
        headerViewHolder.mTypeTv.setText(title);
        headerViewHolder.mTypeRankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击排行榜");
            }
        });
        switch (type) {
            case TYPE_RECOMMENDED:
                headerViewHolder.mTypeMore.setVisibility(View.GONE);
                headerViewHolder.mTypeRankBtn.setVisibility(View.VISIBLE);
                headerViewHolder.mAllLiveNum.setVisibility(View.GONE);
                break;
            case TYPE_LIVE:
                headerViewHolder.mTypeRankBtn.setVisibility(View.GONE);
                headerViewHolder.mTypeMore.setVisibility(View.VISIBLE);
                headerViewHolder.mAllLiveNum.setVisibility(View.VISIBLE);
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder("当前" + liveCount + "个直播");
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.pink_text_color));
                stringBuilder.setSpan(foregroundColorSpan, 2,
                        stringBuilder.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                headerViewHolder.mAllLiveNum.setText(stringBuilder);
                break;
            default:
                headerViewHolder.mTypeRankBtn.setVisibility(View.GONE);
                headerViewHolder.mTypeMore.setVisibility(View.VISIBLE);
                headerViewHolder.mAllLiveNum.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 根据title设置typeIcon
     */
    private void setTypeIcon(HeaderViewHolder headerViewHolder) {
        switch (title) {
            case "热门焦点":
                headerViewHolder.mTypeImg.setImageResource(icons[0]);
                break;
            case "正在直播":
                headerViewHolder.mTypeImg.setImageResource(icons[1]);
                break;
            case "番剧推荐":
                headerViewHolder.mTypeImg.setImageResource(icons[2]);
                break;
            case "动画区":
                headerViewHolder.mTypeImg.setImageResource(icons[3]);
                break;
            case "音乐区":
                headerViewHolder.mTypeImg.setImageResource(icons[4]);
                break;
            case "舞蹈区":
                headerViewHolder.mTypeImg.setImageResource(icons[5]);
                break;
            case "游戏区":
                headerViewHolder.mTypeImg.setImageResource(icons[6]);
                break;
            case "鬼畜区":
                headerViewHolder.mTypeImg.setImageResource(icons[7]);
                break;
            case "科技区":
                headerViewHolder.mTypeImg.setImageResource(icons[8]);
                break;
            case "生活区":
                headerViewHolder.mTypeImg.setImageResource(icons[9]);
                break;
            case "时尚区":
                headerViewHolder.mTypeImg.setImageResource(icons[10]);
                break;
            case "娱乐区":
                headerViewHolder.mTypeImg.setImageResource(icons[11]);
                break;
            case "电视剧区":
                headerViewHolder.mTypeImg.setImageResource(icons[12]);
                break;
            case "电影区":
                headerViewHolder.mTypeImg.setImageResource(icons[13]);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder getFooterViewHolder(View view) {
        Log.e(TAG, "getFooterViewHolder: 底部");
        return new FootViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        Log.e(TAG, "onBindFooterViewHolder: 绑定底部");
        final FootViewHolder footViewHolder = (FootViewHolder) holder;
        footViewHolder.mDynamic.setText(String.valueOf(mRandom.nextInt(200)) + "条新动态,点这里刷新");
        footViewHolder.mRefreshBtn.setOnClickListener(v -> footViewHolder.mRefreshBtn
                .animate()
                .rotation(360)
                .setInterpolator(new LinearInterpolator())
                .setDuration(1000).start());
        footViewHolder.mRecommendRefresh.setOnClickListener(v -> footViewHolder.mRecommendRefresh
                .animate()
                .rotation(360)
                .setInterpolator(new LinearInterpolator())
                .setDuration(1000).start());
        footViewHolder.mBangumiIndexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("这是点击那个 bangumi");
            }
        });
        footViewHolder.mBangumiTimelineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击BangumiTimeline");
            }
        });
        switch (type) {
            case TYPE_RECOMMENDED:
                footViewHolder.mMoreBtn.setVisibility(View.GONE);
                footViewHolder.mRefreshLayout.setVisibility(View.GONE);
                footViewHolder.mBangumiLayout.setVisibility(View.GONE);
                footViewHolder.mRecommendRefreshLayout.setVisibility(View.VISIBLE);
                break;
            case TYPE_BANGUMI:
                footViewHolder.mMoreBtn.setVisibility(View.GONE);
                footViewHolder.mRefreshLayout.setVisibility(View.GONE);
                footViewHolder.mRecommendRefreshLayout.setVisibility(View.GONE);
                footViewHolder.mBangumiLayout.setVisibility(View.VISIBLE);
                break;
            case TYPE_ACTIVITY:
                footViewHolder.mRecommendRefreshLayout.setVisibility(View.GONE);
                footViewHolder.mBangumiLayout.setVisibility(View.GONE);
                footViewHolder.mMoreBtn.setVisibility(View.GONE);
                footViewHolder.mRefreshLayout.setVisibility(View.GONE);
                break;
            default:
                footViewHolder.mRecommendRefreshLayout.setVisibility(View.GONE);
                footViewHolder.mBangumiLayout.setVisibility(View.GONE);
                footViewHolder.mMoreBtn.setVisibility(View.VISIBLE);
                footViewHolder.mRefreshLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_type_img)
        ImageView mTypeImg;
        @BindView(R.id.item_type_tv)
        TextView mTypeTv;
        @BindView(R.id.item_type_more)
        TextView mTypeMore;
        @BindView(R.id.item_type_rank_btn)
        TextView mTypeRankBtn;
        @BindView(R.id.item_live_all_num)
        TextView mAllLiveNum;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view)
        CardView mCardView;
        @BindView(R.id.video_preview)
        ImageView mVideoImg;
        @BindView(R.id.video_title)
        TextView mVideoTitle;
        @BindView(R.id.video_play_num)
        TextView mVideoPlayNum;
        @BindView(R.id.video_review_count)
        TextView mVideoReviewCount;
        @BindView(R.id.layout_live)
        RelativeLayout mLiveLayout;
        @BindView(R.id.layout_video)
        LinearLayout mVideoLayout;
        @BindView(R.id.item_live_up)
        TextView mLiveUp;
        @BindView(R.id.item_live_online)
        TextView mLiveOnline;
        @BindView(R.id.layout_bangumi)
        RelativeLayout mBangumiLayout;
        @BindView(R.id.item_bangumi_update)
        TextView mBangumiUpdate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_btn_more)
        Button mMoreBtn;
        @BindView(R.id.item_dynamic)
        TextView mDynamic;
        @BindView(R.id.item_btn_refresh)
        ImageView mRefreshBtn;
        @BindView(R.id.item_refresh_layout)
        LinearLayout mRefreshLayout;
        @BindView(R.id.item_recommend_refresh_layout)
        LinearLayout mRecommendRefreshLayout;
        @BindView(R.id.item_recommend_refresh)
        ImageView mRecommendRefresh;
        @BindView(R.id.item_bangumi_layout)
        LinearLayout mBangumiLayout;
        @BindView(R.id.item_btn_bangumi_index)
        ImageView mBangumiIndexBtn;
        @BindView(R.id.item_btn_bangumi_timeline)
        ImageView mBangumiTimelineBtn;

        FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
