package com.learn.liyv.learnbilibili.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.entity.live.LiveAppIndexInfo;
import com.learn.liyv.learnbilibili.widget.banner.BannerEntity;
import com.learn.liyv.learnbilibili.widget.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by lyh on 2017/12/26 14:40.
 * 1.
 */

public class LiveAppIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //直播分类入口
    private static final int TYPE_ENTRANCE = 0;
    //直播item
    private static final int TYPE_LIVE_ITEM = 1;
    //直播分类Title
    private static final int TYPE_PARTITION = 2;
    //直播页Banner
    private static final int TYPE_BANNER = 3;
    private Context context;
    private LiveAppIndexInfo liveAppIndexInfo;
    private int entranceSize;
    private List<BannerEntity> bannerEntities = new ArrayList<>();
    private List<Integer> liveSizes = new ArrayList<>();
    private int[] entranceIconRes = new int[]{
            R.drawable.live_home_follow_anchor,
            R.drawable.live_home_live_center,
            R.drawable.live_home_search_room,
            R.drawable.live_home_all_category
    };

    private String[] entranceTitles = new String[]{
            "关注直播", "直播中心", "搜索直播", "全部分类"
    };

    public LiveAppIndexAdapter(Context context) {
        this.context = context;
    }

    @SuppressWarnings("InflateParams")
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case TYPE_ENTRANCE:
                view = inflater.inflate(R.layout.item_live_entrance, null);
                return new LiveEntranceViewHolder(view);
            case TYPE_BANNER:
                view = inflater.inflate(R.layout.item_live_banner, null);
                return new LiveBannerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        position -= 1;
        final LiveAppIndexInfo.DataBean.PartitionsBean.LivesBean livesBean;
        if (holder instanceof LiveEntranceViewHolder) {
            LiveEntranceViewHolder liveEntranceViewHolder = (LiveEntranceViewHolder) holder;
            liveEntranceViewHolder.title.setText(entranceTitles[position]);
            Glide.with(context)
                    .load(entranceIconRes[position])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(liveEntranceViewHolder.imageView);
        } else if (holder instanceof LiveBannerViewHolder) {
            LiveBannerViewHolder liveBannerViewHolder = (LiveBannerViewHolder) holder;
            liveBannerViewHolder.bannerView.delayTime(5)
                    .build(bannerEntities);
        }
    }

    @Override
    public int getItemCount() {
        if (liveAppIndexInfo != null) {
//            return 1 + entranceIconRes.length + liveAppIndexInfo.getData().getPartitions().size() * 5;
            return 3;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (0 == position) {
            return TYPE_BANNER;
        }
        position -= 1;
        if (position < entranceSize) {
            return TYPE_ENTRANCE;
        } else if (isPartitionTitle(position)) {
            return TYPE_PARTITION;
        } else {
            return TYPE_LIVE_ITEM;
        }
    }

    public int getSpanSize(int pos) {
        int viewType = getItemViewType(pos);
        switch (viewType) {
            case TYPE_ENTRANCE:
                return 3;
            case TYPE_LIVE_ITEM:
                return 6;
            case TYPE_PARTITION:
                return 12;
            case TYPE_BANNER:
                return 12;
        }
        return 0;
    }

    public void setLiveInfo(LiveAppIndexInfo liveAppIndexInfo) {
        this.liveAppIndexInfo = liveAppIndexInfo;
        entranceSize = 4;
        liveSizes.clear();
        bannerEntities.clear();
        int tempSize = 0;
        int partitionSize = liveAppIndexInfo.getData().getPartitions().size();
        List<LiveAppIndexInfo.DataBean.BannerBean> banner = liveAppIndexInfo.getData().getBanner();
        Observable.fromIterable(banner)
                .forEach(new Consumer<LiveAppIndexInfo.DataBean.BannerBean>() {
                    @Override
                    public void accept(LiveAppIndexInfo.DataBean.BannerBean bannerBean) throws Exception {
                        bannerEntities.add(new BannerEntity(
                                bannerBean.getLink(), bannerBean.getTitle(),
                                bannerBean.getImg()
                        ));
                    }
                });
        for (int i = 0; i < partitionSize; i++) {
            liveSizes.add(tempSize);
            tempSize += liveAppIndexInfo.getData().getPartitions().get(i).getLives().size();
        }
    }

    private boolean isPartitionTitle(int pos) {
        pos -= entranceSize;
        return pos % 5 == 0;
    }

    //banner
    static class LiveBannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_live_banner)
        BannerView bannerView;

        LiveBannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //banner 下的 4个分类
    static class LiveEntranceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_live_entrance)
        TextView title;
        @BindView(R.id.image_live_entrance)
        ImageView imageView;

        LiveEntranceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
