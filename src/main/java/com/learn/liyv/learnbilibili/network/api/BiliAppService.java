package com.learn.liyv.learnbilibili.network.api;

import com.learn.liyv.learnbilibili.entity.recommend.RecommendBannerInfo;
import com.learn.liyv.learnbilibili.entity.recommend.RecommendInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by lyh on 2017/12/9.
 */

public interface BiliAppService {

    /**
     * 首页推荐数据
     */
    @GET("x/show/old?platform=android&device=&build=412001")
    Observable<RecommendInfo> getRecommendedInfo();

    /**
     * 首页推荐banner
     */
    @GET("x/banner?plat=4&build=411007&channel=bilih5")
    Observable<RecommendBannerInfo> getRecommendedBannerInfo();
}
