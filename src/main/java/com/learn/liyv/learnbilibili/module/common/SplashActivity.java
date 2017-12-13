package com.learn.liyv.learnbilibili.module.common;

import android.content.Intent;
import android.os.Bundle;

import com.learn.liyv.learnbilibili.MainActivity;
import com.learn.liyv.learnbilibili.R;
import com.learn.liyv.learnbilibili.utils.ConstantUtil;
import com.learn.liyv.learnbilibili.utils.PreferenceUtil;
import com.learn.liyv.learnbilibili.utils.SystemUIVisibilityUtil;
import com.trello.rxlifecycle2.components.RxActivity;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class SplashActivity extends RxActivity {

    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        SystemUIVisibilityUtil.hideStatusBar(getWindow(), true);
        setUpSplash();
    }

    private void setUpSplash() {
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .compose(this.<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        finishTask();
                    }
                });
    }

    private void finishTask() {
        boolean isLogin = PreferenceUtil.getBoolean(ConstantUtil.KEY, false);
        if (isLogin) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        SplashActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
