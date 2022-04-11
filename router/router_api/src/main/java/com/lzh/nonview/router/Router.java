/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lzh.nonview.router;


import com.lzh.nonview.router.exception.NotFoundException;
import com.lzh.nonview.router.extras.RouteBundleExtras;
import com.lzh.nonview.router.interceptors.RouteInterceptor;
import com.lzh.nonview.router.module.RouteRule;
import com.lzh.nonview.router.protocol.HostServiceWrapper;
import com.lzh.nonview.router.route.ActionRoute;
import com.lzh.nonview.router.activityresult.ActivityResultCallback;
import com.lzh.nonview.router.route.ActivityRoute;
import com.lzh.nonview.router.route.BaseRoute;
import com.lzh.nonview.router.route.BrowserRoute;
import com.lzh.nonview.router.route.IActionRoute;
import com.lzh.nonview.router.route.IActivityRoute;
import com.lzh.nonview.router.route.IBaseRoute;
import com.lzh.nonview.router.route.IRoute;
import com.lzh.nonview.router.route.InternalCallback;
import com.lzh.nonview.router.route.RouteCallback;
import com.lzh.nonview.router.tools.Cache;
import com.lzh.nonview.router.tools.Constants;
import com.lzh.nonview.router.tools.Utils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.IntentParams;
import ohos.utils.net.Uri;

import java.util.concurrent.Executor;


/**
 * 路由对象。
 *
 * @author haoge
 */
public final class Router {

    /**
     * 原始uri的关键。 您可以通过此键获取uri，例如:
     * <pre>
     *      <i><b>bundle.getParcelable(Router.RAW_URI)</b></i>
     * </pre> haoge://page/intent/printer?title=动态登录检查&requestLogin=1
     */
    public static final String RAW_URI = "_ROUTER_RAW_URI_KEY_";
    /**
     * DEBUG
     */
    private static boolean debug = false;
    private Uri uri;
    private String url;
    private InternalCallback internalCallback;

    private Router(String url) {
        this.url = url;
        this.uri = Uri.parse(url == null ? "" : url);
        internalCallback = new InternalCallback(uri);
    }

    /**
     * 通过URL字符串创建路由器
     *
     * @param url 创建路由器的URL
     * @return 返回新路由器
     */
    public static Router create(String url) {

        return new Router(url);
    }

    public static boolean isDebug() {
        return debug;
    }
    public static void setDebug(boolean debug) {
        Router.debug = debug;
    }

    /**
     * createInstanceRouter
     *
     * @param url
     * @return InstanceRouter
     */
    public static InstanceRouter createInstanceRouter(String url) {
        return InstanceRouter.build(url);
    }

    /**
     * Set a callback to notify the user when the routing were success or failure.
     *
     * @param callback The callback you set.
     * @return Router itself
     */
    public Router setCallback(RouteCallback callback) {
        this.internalCallback.setCallback(callback);
        return this;
    }

    public Router addInterceptor(RouteInterceptor interceptor) {
        this.internalCallback.getExtras().addInterceptor(interceptor);
        return this;
    }

    public Router requestCode(int requestCode) {
        this.internalCallback.getExtras().setRequestCode(requestCode);
        return this;
    }

    public Router resultCallback(ActivityResultCallback callback) {
        this.internalCallback.getExtras().putValue(Constants.KEY_RESULT_CALLBACK, callback);
        return this;
    }

    public Router addFlags(int flag) {
        this.internalCallback.getExtras().addFlags(flag);
        return this;
    }

    public Router setAnim(int enterAnim, int exitAnim) {
        this.internalCallback.getExtras().setInAnimation(enterAnim);
        this.internalCallback.getExtras().setOutAnimation(exitAnim);
        return this;
    }

    public Router setOptions(IntentParams options) {
        this.internalCallback.getExtras().putValue(Constants.KEY_ACTIVITY_OPTIONS, options);
        return this;
    }

    public Router addExtras(IntentParams extras) {
        this.internalCallback.getExtras().addExtras(extras);
        return this;
    }

    public Router setExecutor(Executor executor) {
        this.internalCallback.getExtras().putValue(Constants.KEY_ACTION_EXECUTOR, executor);
        return this;
    }

    /**
     * Restore a Routing event from last uri and extras.
     *
     * @param uri    last uri
     * @param extras last extras
     * @return The restored routing find by {@link Router#getRoute()}
     */
    public static IRoute resume(String uri, RouteBundleExtras extras) {
        IRoute route = Router.create(uri).getRoute();
        if (route instanceof BaseRoute) {
            ((BaseRoute) route).replaceExtras(extras);
        }
        return route;
    }

    /**
     * launch 路由任务.
     *
     * @param context context to launched
     */
    public void open(Ability context) {
        getRoute().open(context);
    }


    /**
     * Get route by uri, you should get a route by this way and set some extras data before open
     *
     * @return An IRoute object.it will be BrowserRoute, ActivityRoute or ActionRoute.
     */
    public IRoute getRoute() {
        IRoute route = getLocalRoute();
        if (!(route instanceof IRoute.EmptyRoute)) {
            return route;
        }
        route = HostServiceWrapper.create(url, internalCallback);
        if (route instanceof IRoute.EmptyRoute) {
            notifyNotFound(String.format("find Route by %s failed:", uri));
        }
        return route;
    }

    private IRoute getLocalRoute() {
        try {
            RouteRule rule;
            if (!Utils.isValidUri(uri)) {
                return new IRoute.EmptyRoute(internalCallback);
            } else if ((rule = ActionRoute.findRule(uri, Cache.TYPE_ACTION_ROUTE)) != null) {
                return new ActionRoute().create(uri, rule, new IntentParams(), internalCallback);
            } else if ((rule = ActivityRoute.findRule(uri, Cache.TYPE_ACTIVITY_ROUTE)) != null) {
                return new ActivityRoute().create(uri, rule, new IntentParams(), internalCallback);
            } else if (BrowserRoute.canOpenRouter(uri)) {
                return BrowserRoute.getInstance().setUri(uri);
            } else {
                return new IRoute.EmptyRoute(internalCallback);
            }
        } catch (Exception e) {
            internalCallback.onOpenFailed(e);
            return new IRoute.EmptyRoute(internalCallback);
        }
    }

    /**
     * <p>
     * Get {@link IBaseRoute} by uri, it could be one of {@link IActivityRoute} or {@link IActionRoute}.
     * and you can add some { } data and {@link RouteInterceptor} into it.
     * </p>
     *
     * @return returns an {@link IBaseRoute} finds by uri or {@link IBaseRoute.EmptyBaseRoute} for not found
     */
    public IBaseRoute getBaseRoute() {
        IRoute route = getRoute();
        if (route instanceof IBaseRoute) {
            return (IBaseRoute) route;
        }

        notifyNotFound(String.format("find BaseRoute by %s failed, but is %s", uri, route.getClass().getSimpleName()));
        return new IBaseRoute.EmptyBaseRoute(internalCallback);
    }

    /**
     * Get {@link IActivityRoute} by uri,you should get a route by this way and set some extras data before open
     *
     * @return returns an {@link IActivityRoute} finds by uri or {@link IActivityRoute.EmptyActivityRoute} for not found.
     */
    public IActivityRoute getActivityRoute() {
        IRoute route = getRoute();
        if (route instanceof IActivityRoute) {
            return (IActivityRoute) route;
        }

        // return an empty route to avoid NullPointException
        notifyNotFound(String.format("find ActivityRoute by %s failed, but is %s", uri, route.getClass().getSimpleName()));
        return new IActivityRoute.EmptyActivityRoute(internalCallback);
    }

    /**
     * Get {@link IActionRoute} by uri,you should get a route by this way and set some extras data before open
     *
     * @return returns an {@link IActionRoute} finds by uri or {@link IActionRoute.EmptyActionRoute} for not found.
     */
    public IActionRoute getActionRoute() {
        IRoute route = getRoute();
        if (route instanceof IActionRoute) {
            return (IActionRoute) route;
        }

        notifyNotFound(String.format("find ActionRoute by %s failed, but is %s", uri, route.getClass().getSimpleName()));
        // return a empty route to avoid NullPointException
        return new IActionRoute.EmptyActionRoute(internalCallback);
    }

    private void notifyNotFound(String msg) {
        internalCallback.onOpenFailed(new NotFoundException(msg));
    }
}
