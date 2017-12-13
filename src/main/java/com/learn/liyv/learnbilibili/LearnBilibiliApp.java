package com.learn.liyv.learnbilibili;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by lyh on 2017/12/8.
 */

public class LearnBilibiliApp extends Application {
    public static LearnBilibiliApp mInstance;

    public static LearnBilibiliApp getmInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
        //初始化 Leak 内存泄漏检测工具
        LeakCanary.install(this);
        //初始化 Stetho 调试工具
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }
}
