package com.learn.liyv.learnbilibili.module.home.region;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.adapter.HomeRegionItemAdapter;
import com.learn.liyv.learnbilibili.adapter.helper.AbsRecyclerViewAdapter;
import com.learn.liyv.learnbilibili.base.RxLazyFragment;
import com.learn.liyv.learnbilibili.entity.region.RegionTypesInfo;
import com.learn.liyv.learnbilibili.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lyh on 2017/12/30 10:16.
 * 1.
 */

public class HomeRegionFragment extends RxLazyFragment {
    @BindView(R.id.rv_home_region)
    RecyclerView recyclerView;

    private List<RegionTypesInfo.DataBean> regionTypes = new ArrayList<>();

    public static HomeRegionFragment newInstance() {
        return new HomeRegionFragment();
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_region;
    }

    @Override
    public void finishCreateView(Bundle state) {
        loadData();
        initRecyclerView();
    }

    @Override
    protected void loadData() {
        Observable.just(readAssetsJson())
                .compose(bindToLifecycle())
                .map(new Function<String, RegionTypesInfo>() {
                    @Override
                    public RegionTypesInfo apply(String s) throws Exception {
                        return new Gson().fromJson(s, RegionTypesInfo.class);
                    }
                })
                .map(new Function<RegionTypesInfo, List<RegionTypesInfo.DataBean>>() {
                    @Override
                    public List<RegionTypesInfo.DataBean> apply(RegionTypesInfo regionTypesInfo) throws Exception {
                        return regionTypesInfo.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RegionTypesInfo.DataBean>>() {
                    @Override
                    public void accept(List<RegionTypesInfo.DataBean> dataBeans) throws Exception {
                        regionTypes.addAll(dataBeans);
                        finishTask();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


    private String readAssetsJson() {
        AssetManager assetManager = getActivity().getAssets();
        try {
            InputStream is = assetManager.open("region.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        HomeRegionItemAdapter adapter = new HomeRegionItemAdapter(recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AbsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder) {
                ToastUtil.ShortToast(position + "");
//                System.out.println("点击" + position);
            }
        });
    }
}
