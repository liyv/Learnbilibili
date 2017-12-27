package com.learn.liyv.learnbilibili.module.home.live;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.adapter.LiveAppIndexAdapter;
import com.learn.liyv.learnbilibili.base.RxLazyFragment;
import com.learn.liyv.learnbilibili.entity.live.LiveAppIndexInfo;
import com.learn.liyv.learnbilibili.network.RetrofitHelper;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lyh on 2017/12/26 14:32.
 * 1.
 */

public class HomeLiveFragment extends RxLazyFragment {

    @BindView(R.id.rv_home_live)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private LiveAppIndexAdapter liveAppIndexAdapter;

    public static HomeLiveFragment newInstance() {
        return new HomeLiveFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_live;
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        });
    }

    @Override
    protected void initRecyclerView() {
        liveAppIndexAdapter = new LiveAppIndexAdapter(getActivity());
        recyclerView.setAdapter(liveAppIndexAdapter);
        GridLayoutManager lm = new GridLayoutManager(getActivity(), 12);
//        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int span = liveAppIndexAdapter.getSpanSize(position);
                return span;
            }
        });
        recyclerView.setLayoutManager(lm);
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getLiveAPI()
                .getLiveAppIndex()
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LiveAppIndexInfo>() {
                    @Override
                    public void accept(LiveAppIndexInfo liveAppIndexInfo) throws Exception {
                        liveAppIndexAdapter.setLiveInfo(liveAppIndexInfo);
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
        liveAppIndexAdapter.notifyDataSetChanged();
//        recyclerView.scrollToPosition(0);
    }
}
