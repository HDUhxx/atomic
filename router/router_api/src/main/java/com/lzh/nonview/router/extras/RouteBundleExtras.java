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
package com.lzh.nonview.router.extras;


import com.lzh.nonview.router.interceptors.RouteInterceptor;
import com.lzh.nonview.router.route.IBaseRoute;
import com.lzh.nonview.router.route.RouteCallback;
import com.lzh.nonview.router.tools.CacheBundle;
import com.lzh.nonview.router.tools.CacheStore;
import ohos.aafwk.content.IntentParams;
import ohos.agp.utils.TextTool;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

import java.util.*;

/**
 * An extra container contains {@link RouteBundleExtras#extras} and {@link RouteBundleExtras#interceptors}
 * <p>
 *      <i>interceptors: the extra RouteInterceptor set by {@link IBaseRoute#addInterceptor(RouteInterceptor)}</i>
 * </p>
 *
 * @author haoge
 * @see com.lzh.nonview.router.route.IBaseRoute
 */
public final class RouteBundleExtras implements Sequenceable {

    private ArrayList<RouteInterceptor> interceptors = new ArrayList<>();
    private RouteCallback callback;
    private HashMap<String, Object> additionalMap = new HashMap<String, Object>();

    private IntentParams extras = new IntentParams();
    //面的附加功能仅支持ActivityRoute.
    private int requestCode = -1;
    private int inAnimation = -1;
    private int outAnimation = -1;
    private int flags = 0;

    public RouteBundleExtras() {}

    public void addInterceptor(RouteInterceptor interceptor) {
        if (interceptor != null && !getInterceptors().contains(interceptor)) {
            getInterceptors().add(interceptor);
        }
    }

    public void removeInterceptor(RouteInterceptor interceptor) {
        if (interceptor != null) {
            getInterceptors().remove(interceptor);
        }
    }

    public void removeAllInterceptors() {
        getInterceptors().clear();
    }

    public List<RouteInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setCallback(RouteCallback callback) {
        this.callback = callback;
    }

    public RouteCallback getCallback() {
        return callback;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getInAnimation() {
        return inAnimation;
    }

    public void setInAnimation(int inAnimation) {
        this.inAnimation = inAnimation;
    }

    public int getOutAnimation() {
        return outAnimation;
    }

    public void setOutAnimation(int outAnimation) {
        this.outAnimation = outAnimation;
    }

    public int getFlags() {
        return flags;
    }

    public void addFlags(int flags) {
        this.flags |= flags;
    }

    public <T> T getValue(String key) {
        if (TextTool.isNullOrEmpty(key)) {
            return null;
        }

        try {
            return (T) additionalMap.get(key);
        } catch (ClassCastException cast) {
            return null;
        }
    }

    public void putValue(String key, Object value) {
        if (TextTool.isNullOrEmpty(key) || value == null) {
            return;
        }

        additionalMap.put(key, value);
    }




    public IntentParams getExtras() {
        return extras;
    }

    public void addExtras(IntentParams extras) {
        if (extras != null) {
            Set<String> keySet = extras.keySet();
            for(String key : keySet) {
                Object value = extras.getParam(key);
                this.extras.setParam(key,value);
            }

        }
    }

    @Override
    public boolean marshalling(Parcel parcel) {
        parcel.writeSequenceable(extras);
        parcel.writeInt(requestCode);
        parcel.writeInt(inAnimation);
        parcel.writeInt(outAnimation);
        parcel.writeInt(this.flags);
        CacheBundle.get().setInterceptors(interceptors);
        CacheBundle.get().setCallback(callback);
        CacheBundle.get().setAdditionalMap( additionalMap);
//        int a=CacheStore.get().put(interceptors);
//        int b=CacheStore.get().put(callback);
//        int c=CacheStore.get().put(additionalMap);
//        parcel.writeInt(a);
//        parcel.writeInt(b);
//        parcel.writeInt(c);
        return true;
    }

    @Override
    public boolean unmarshalling(Parcel parcel) {
        this.requestCode = parcel.readInt();
        this.inAnimation = parcel.readInt();
        this.outAnimation = parcel.readInt();
        this.flags = parcel.readInt();

        this.interceptors = CacheBundle.get().getInterceptors();
        this.callback = CacheBundle.get().getCallback();
        this.additionalMap = CacheBundle.get().getAdditionalMap();
//        int aa=parcel.readInt();
//        int bb=parcel.readInt();
//        int cc=parcel.readInt();
//        System.out.println("读出来的 extras "+requestCode+","+inAnimation+","+outAnimation+","+this.flags+","+aa+","+bb+","+cc+"还有extras");
//        if(null == CacheBundle.get().getInterceptors() ){
//        } else {
//
//        }
//        if(null == CacheBundle.get()){
//        }else{
//            this.additionalMap = CacheStore.get().get( bb);
//        }
//        if(null == CacheStore.get().get( cc)){
//        }else{
//            this.additionalMap = CacheStore.get().get(cc);
//        }
        parcel.readSequenceable(extras);
        return true;
    }


    /**
     *  序列化对象的内部构造器，必须实现
     */
    public static final Sequenceable.Producer PRODUCER = new Sequenceable.Producer() {

        @Override
        public RouteBundleExtras createFromParcel(Parcel parcel) {
            RouteBundleExtras instance =new RouteBundleExtras();
            instance.unmarshalling(parcel);
            return instance;
        }
    };
}
