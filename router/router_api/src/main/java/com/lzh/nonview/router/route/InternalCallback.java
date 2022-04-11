package com.lzh.nonview.router.route;


import com.lzh.nonview.router.RouterConfiguration;
import com.lzh.nonview.router.exception.NotFoundException;
import com.lzh.nonview.router.extras.RouteBundleExtras;
import com.lzh.nonview.router.module.RouteRule;
import com.lzh.nonview.router.tools.RouterLog;
import ohos.app.Context;
import ohos.utils.Pair;
import ohos.utils.net.Uri;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     内部路由回调.
 * </p>
 */
public final class InternalCallback {

    // 存储map以提供uri的额外信息.
    private static Map<Uri, Pair<Context, RouteBundleExtras>> cache = new HashMap<>();

    private Uri uri;
    private RouteBundleExtras extras = new RouteBundleExtras();
    private RouteRule rule;
    private Throwable error;

    public InternalCallback(Uri uri) {
        this.uri = uri;
    }

    public void setCallback(RouteCallback callback) {
        extras.setCallback(callback);
    }

    public RouteBundleExtras getExtras() {
        return extras;
    }

    public void setExtras(RouteBundleExtras extras) {
        if (extras != null) {
            this.extras = extras;
        }
    }

    public void onOpenSuccess(RouteRule rule) {
        this.rule = rule;
    }

    public void onOpenFailed(Throwable e) {
        if (error == null) {
            // 只接受第一次配置的异常
            this.error = e;
        }
    }

    void invoke(Context context) {
        cache.put(uri, new Pair<>(context, extras));
        invokeWithCallback(RouterConfiguration.get().getCallback(),
                extras.getCallback());
        cache.remove(uri);
    }

    private void invokeWithCallback(RouteCallback global, RouteCallback callback) {
        if (error != null && error instanceof NotFoundException) {
            RouterLog.d("[RouterLog] Could not found matched route for " + uri);
            if (global != null) {
                global.notFound(uri, (NotFoundException) error);
            }
            if (callback != null) {
                callback.notFound(uri, (NotFoundException) error);
            }
        } else if (error != null) {
            RouterLog.e("[RouterLog] Launch route with " + uri + " failed.", error);
            if (global != null) {
                global.onOpenFailed(uri, error);
            }
            if (callback != null) {
                callback.onOpenFailed(uri, error);
            }
        } else if (rule != null) {
            RouterLog.d("[RouterLog] Launch route with " + uri + " successful!, target class name is " + rule.getRuleClz());
            if (global != null) {
                global.onOpenSuccess(uri, rule);
            }
            if (callback != null) {
                callback.onOpenSuccess(uri, rule);
            }
        } else {
            RouterLog.e("[RouterLog] Launch route with " + uri + " failed.", null);
            if (global != null) {
                global.onOpenFailed(uri, new RuntimeException("Unknown error"));
            }
            if (callback != null) {
                callback.onOpenFailed(uri, new RuntimeException("Unknown error"));
            }
        }
    }

    public static RouteBundleExtras findExtrasByUri(Uri uri) {
        Pair<Context, RouteBundleExtras> pair = cache.get(uri);
        return pair == null? null : pair.s;
    }

    public static Context findContextByUri(Uri uri) {
        Pair<Context, RouteBundleExtras> pair = cache.get(uri);
        return pair == null? null : pair.f;
    }
}
