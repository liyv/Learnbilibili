package com.learn.liyv.learnbilibili.module.home.recommend;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.adapter.section.HomeRecommendBannerSection;
import com.learn.liyv.learnbilibili.adapter.section.HomeRecommendTopicSection;
import com.learn.liyv.learnbilibili.adapter.section.HomeRecommendedSection;
import com.learn.liyv.learnbilibili.base.RxLazyFragment;
import com.learn.liyv.learnbilibili.entity.recommend.RecommendBannerInfo;
import com.learn.liyv.learnbilibili.entity.recommend.RecommendInfo;
import com.learn.liyv.learnbilibili.network.RetrofitHelper;
import com.learn.liyv.learnbilibili.utils.ConstantUtil;
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
 * Created by lyh on 2017/12/9.
 */

public class HomeRecommendedFragment extends RxLazyFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    private List<RecommendBannerInfo.DataBean> recommendBanners = new ArrayList<>();
    private List<RecommendInfo.ResultBean> results = new ArrayList<>();
    private boolean mIsRefreshing = false;
    private List<BannerEntity> banners = new ArrayList<>();
    private SectionedRecyclerViewAdapter mSectionedAdapter;

    public static HomeRecommendedFragment newInstance() {
        return new HomeRecommendedFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_recommend;
    }

    @Override
    public void finishCreateView(Bundle state) {
        isPrepared = true;
        initRecyclerView();
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        System.out.println("懒加载 HomeRecommendedFragment");

        initRefreshLayout();
        isPrepared = false;
    }

    @Override
    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearData();
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getBiliAppAPI()
                .getRecommendedBannerInfo()
                .compose(bindToLifecycle())
                .map(new Function<RecommendBannerInfo, List<RecommendBannerInfo.DataBean>>() {
                    @Override
                    public List<RecommendBannerInfo.DataBean> apply(RecommendBannerInfo recommendBannerInfo) throws Exception {
                        System.out.println(recommendBannerInfo.getData());
                        return recommendBannerInfo.getData();
                    }
                })
                .flatMap(new Function<List<RecommendBannerInfo.DataBean>, ObservableSource<RecommendInfo>>() {
                    @Override
                    public ObservableSource<RecommendInfo> apply(List<RecommendBannerInfo.DataBean> dataBeans) throws Exception {
                        recommendBanners.addAll(dataBeans);
                        return RetrofitHelper.getBiliAppAPI().getRecommendedInfo();
                    }
                }).compose(bindToLifecycle())
                .map(new Function<RecommendInfo, List<RecommendInfo.ResultBean>>() {
                    @Override
                    public List<RecommendInfo.ResultBean> apply(RecommendInfo recommendInfo) throws Exception {
                        return recommendInfo.getResult();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RecommendInfo.ResultBean>>() {
                    @Override
                    public void accept(List<RecommendInfo.ResultBean> resultBeans) throws Exception {
                        results.addAll(resultBeans);
                        finishTask();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //显示缺省页
//                        initEmptyView();
                    }
                });
    }

    @Override
    protected void finishTask() {
        System.out.println("取数据完成");
        mSwipeRefreshLayout.setRefreshing(false);
        mIsRefreshing = false;
        //隐藏缺省页
//        hideEmptyView();
        convertBanner();
        mSectionedAdapter.addSection(new HomeRecommendBannerSection(banners));
        int size = results.size();
        /*for (int i = 0; i < size; i++) {
            String type = results.get(i).getType();
            if (!TextUtils.isEmpty(type)) {
                switch (type) {
                    //话题
                    case ConstantUtil.TYPE_TOPIC:
                        mSectionedAdapter.addSection(new HomeRecommendTopicSection(
                                getActivity(), results.get(i).getBody().get(0).getCover(),
                                results.get(i).getBody().get(0).getTitle(),
                                results.get(i).getBody().get(0).getParam()
                        ));
                        break;

                    default:
                        mSectionedAdapter.addSection(new HomeRecommendedSection(
                                getActivity(),
                                results.get(i).getHead().getTitle(),
                                results.get(i).getType(),
                                results.get(1).getHead().getCount(),
                                results.get(i).getBody()));
                        break;
                }
            }

        }*/
        System.out.println(results.size());
        mSectionedAdapter.notifyDataSetChanged();
    }

    private void clearData() {
        banners.clear();
        recommendBanners.clear();
        results.clear();
        mIsRefreshing = true;
        mSectionedAdapter.removeAllSections();
    }

    //设置 轮播 banner
    private void convertBanner() {
        Observable.fromIterable(recommendBanners)
                .compose(bindToLifecycle())
                .forEach(new Consumer<RecommendBannerInfo.DataBean>() {
                    @Override
                    public void accept(RecommendBannerInfo.DataBean dataBean) throws Exception {
                        banners.add(new BannerEntity(dataBean.getValue(), dataBean.getTitle(), dataBean.getImage()));
                    }
                });
    }

    @Override
    protected void initRecyclerView() {
        mSectionedAdapter = new SectionedRecyclerViewAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mSectionedAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_FOOTER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setAdapter(mSectionedAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        setRecycleNoScroll();
    }

    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }
}
