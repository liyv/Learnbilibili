package com.learn.liyv.learnbilibili.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.learn.liyv.learnbilibili.LearnBilibiliApp;
import com.learn.liyv.learnbilibili.network.api.BiliAppService;
import com.learn.liyv.learnbilibili.network.api.LiveService;
import com.learn.liyv.learnbilibili.network.axiliary.ApiConstants;
import com.learn.liyv.learnbilibili.utils.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lyh on 2017/12/9.
 * Retrofit 辅助类
 */

public class RetrofitHelper {
    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }

    public static BiliAppService getBiliAppAPI(){
        return createApi(BiliAppService.class,ApiConstants.APP_BASE_URL);
    }

    public static LiveService getLiveAPI(){
        return createApi(LiveService.class, ApiConstants.LIVE_BASE_URL);
    }

    /**
     * 根据传入的baseUrl，和 api 创建 retrofit
     */
    private static <T> T createApi(Class<T> clazz, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }


    /**
     * 初始化 OkHttpClient ，设置缓存，设置超时时间，
     * 设置打印日志，UA拦截器
     */
    private static void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (null == mOkHttpClient) {
            synchronized (RetrofitHelper.class) {
                if (null == mOkHttpClient) {
                    //设置 Http 缓存
                    Cache cache = new Cache(new File(LearnBilibiliApp.getmInstance().getCacheDir(),
                            "HttpCache"), 1024 * 1024 * 10);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(new CustomCacheInterceptor())
                            .addNetworkInterceptor(new StethoInterceptor())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .addInterceptor(new UserAgentInterceptor())
                            .build();
                }
            }
        }
    }

    /**
     * 添加 UA 拦截器，B站请求 API 需要加上UA才能正常使用
     */
    private static class UserAgentInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", ApiConstants.COMMON_UA_STR)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }

    /**
     * 为 okhttp 添加缓存，这里是考虑到服务器不支持缓存时，从而让 okhttp 支持缓存
     */
    private static class CustomCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //有网络，设置缓存超时时间1个小时
            int maxAge = 60 * 60;
            //无网络，设置超时为1天
            int maxStale = 60 * 60 * 24;
            Request request = chain.request();
            if (CommonUtil.isNettworkAvailable(LearnBilibiliApp.getmInstance())) {
                //有网络
                request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();

            } else {
                //无网络
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response = chain.proceed(request);
            if (CommonUtil.isNettworkAvailable(LearnBilibiliApp.getmInstance())) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public,max-age=" + maxAge)
                        .build();

            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }
}
