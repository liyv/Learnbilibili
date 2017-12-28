package com.learn.liyv.learnbilibili.module.home.bangumi;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.adapter.section.HomeBangumiBannerSection;
import com.learn.liyv.learnbilibili.adapter.section.HomeBangumiBobySection;
import com.learn.liyv.learnbilibili.adapter.section.HomeBangumiItemSection;
import com.learn.liyv.learnbilibili.adapter.section.HomeBangumiNewSerialSection;
import com.learn.liyv.learnbilibili.adapter.section.HomeBangumiSeasonNewSection;
import com.learn.liyv.learnbilibili.base.RxLazyFragment;
import com.learn.liyv.learnbilibili.entity.bangumi.BangumiAppIndexInfo;
import com.learn.liyv.learnbilibili.entity.bangumi.BangumiRecommendInfo;
import com.learn.liyv.learnbilibili.network.RetrofitHelper;
import com.learn.liyv.learnbilibili.widget.banner.BannerEntity;
import com.learn.liyv.learnbilibili.widget.sectioned.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lyh on 2017/12/28 9:02.
 * 1.
 */

public class HomeBangumiFragment extends RxLazyFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_home_bangumi)
    RecyclerView recyclerView;

    private List<BannerEntity> bannerList = new ArrayList<>();
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    private List<BangumiRecommendInfo.ResultBean> bangumiRecommends = new ArrayList<>();
    private List<BangumiAppIndexInfo.ResultBean.AdBean.HeadBean> banners = new ArrayList<>();
    private List<BangumiAppIndexInfo.ResultBean.AdBean.BodyBean> bangumibobys = new ArrayList<>();
    private List<BangumiAppIndexInfo.ResultBean.PreviousBean.ListBean> seasonNewBangumis = new ArrayList<>();
    private List<BangumiAppIndexInfo.ResultBean.SerializingBean> newBangumiSerials = new ArrayList<>();
    private boolean mIsRefreshing = false;
    private int season;

    public static HomeBangumiFragment newInstance() {
        return new HomeBangumiFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_bangumi;
    }

    @Override
    public void finishCreateView(Bundle state) {
        isPrepared = true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initRefreshLayout();
        initRecyclerView();
        isPrepared = false;
    }

    @Override
    protected void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                mIsRefreshing = true;
                loadData();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearData();
                loadData();
            }
        });
    }

    private void clearData() {
        mIsRefreshing = true;
        banners.clear();
        bannerList.clear();
        bangumibobys.clear();
        bangumiRecommends.clear();
        newBangumiSerials.clear();
        seasonNewBangumis.clear();
        mSectionedRecyclerViewAdapter.removeAllSections();
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getBangumiAPI()
                .getBangumiAppIndex()
                .compose(bindToLifecycle())
                .flatMap(new Function<BangumiAppIndexInfo, ObservableSource<BangumiRecommendInfo>>() {
                    @Override
                    public ObservableSource<BangumiRecommendInfo> apply(BangumiAppIndexInfo bangumiAppIndexInfo) throws Exception {
                        banners.addAll(bangumiAppIndexInfo.getResult().getAd().getHead());
                        bangumibobys.addAll(bangumiAppIndexInfo.getResult().getAd().getBody());
                        seasonNewBangumis.addAll(bangumiAppIndexInfo.getResult().getPrevious().getList());
                        season = bangumiAppIndexInfo.getResult().getPrevious().getSeason();
                        newBangumiSerials.addAll(bangumiAppIndexInfo.getResult().getSerializing());
                        return RetrofitHelper.getBangumiAPI().getBangumiRecommended();

                    }
                })
                .compose(bindToLifecycle())
                .map(BangumiRecommendInfo::getResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BangumiRecommendInfo.ResultBean>>() {
                    @Override
                    public void accept(List<BangumiRecommendInfo.ResultBean> resultBeans) throws Exception {
                        bangumiRecommends.addAll(resultBeans);
                        finishTask();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


    }

    @Override
    protected void finishTask() {
        swipeRefreshLayout.setRefreshing(false);
        mIsRefreshing = false;
        Observable.fromIterable(banners)
                .compose(bindToLifecycle())
                .forEach(new Consumer<BangumiAppIndexInfo.ResultBean.AdBean.HeadBean>() {
                    @Override
                    public void accept(BangumiAppIndexInfo.ResultBean.AdBean.HeadBean headBean) throws Exception {
                        bannerList.add(new BannerEntity(
                                headBean.getLink(), headBean.getTitle(), headBean.getImg()));

                    }
                });
        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiBannerSection(bannerList));
        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiItemSection(getActivity()));
        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiNewSerialSection(getActivity(), newBangumiSerials));
        if (!bangumibobys.isEmpty()) {
            mSectionedRecyclerViewAdapter.addSection(new HomeBangumiBobySection(getActivity(), bangumibobys));
        }
        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiSeasonNewSection(getActivity(), season, seasonNewBangumis));
//        mSectionedRecyclerViewAdapter.addSection(new HomeBangumiRecommendSection(getActivity(), bangumiRecommends));
        mSectionedRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initRecyclerView() {
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 3;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(mSectionedRecyclerViewAdapter);
        setRecycleNoScroll();
    }

    private void setRecycleNoScroll() {
        recyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }
}
