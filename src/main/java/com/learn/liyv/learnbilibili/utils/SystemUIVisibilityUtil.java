package com.learn.liyv.learnbilibili.utils;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by lyh on 2017/12/8.
 */

public class SystemUIVisibilityUtil {

    public static void addFlags(View view, int flags) {
        view.setSystemUiVisibility(view.getSystemUiVisibility() | flags);
    }

    public static void clearFlags(View view, int flags) {
        view.setSystemUiVisibility(view.getSystemUiVisibility() & ~flags);
    }

    public static boolean hasFlags(View view, int flags) {
        return (view.getSystemUiVisibility() & flags) == flags;
    }

    /**
     * 显示或隐藏StatusBar
     */
    public static void hideStatusBar(Window window, boolean visible) {
        WindowManager.LayoutParams lp = window.getAttributes();
        if (visible) {
            // |= 或等于，取其一
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            //&= 与等于 取其二同时满足
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
