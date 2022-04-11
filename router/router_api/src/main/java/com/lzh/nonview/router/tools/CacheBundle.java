package com.lzh.nonview.router.tools;

import com.lzh.nonview.router.interceptors.RouteInterceptor;
import com.lzh.nonview.router.route.RouteCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * CacheBundle
 *
 * @since 2021-04-06
 */
public class CacheBundle {

    private static CacheBundle INSTANCE = new CacheBundle();

    private ArrayList<RouteInterceptor> interceptors = null;
    private RouteCallback callback;
    private HashMap<String, Object> additionalMap = null;

    /**
     * 构造
     */
    private CacheBundle() {
    }

    /**
     * get
     *
     * @return CacheBundle
     */
    public static CacheBundle get() {
        return INSTANCE;
    }

    public ArrayList<RouteInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(ArrayList<RouteInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public RouteCallback getCallback() {
        return callback;
    }

    public void setCallback(RouteCallback callback) {
        this.callback = callback;
    }

    public HashMap<String, Object> getAdditionalMap() {
        return additionalMap;
    }

    public void setAdditionalMap(HashMap<String, Object> additionalMap) {
        this.additionalMap = additionalMap;
    }
}
