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

package com.lzh.nonview.router.tools;


import com.lzh.nonview.router.exception.InterceptorException;
import com.lzh.nonview.router.extras.RouteBundleExtras;
import com.lzh.nonview.router.interceptors.RouteInterceptor;
import com.lzh.nonview.router.parser.URIParser;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.IntentParams;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工具类
 *
 * @ noinspection checkstyle:WriteTag
 */
@SuppressWarnings("unchecked")
public class Utils {
    private final static String Slash = "/";

    /**
     * 调整方案是http还是https
     *
     * @param scheme param scheme scheme
     * @return 如果为http或https，则返回true
     */
    public static boolean isHttp(String scheme) {
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    /**
     * 转换
     *
     * @param url url
     * @return url
     */
    public static String format(String url) {
        if (url.endsWith(Slash)) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * 检查拦截器
     *
     * @param uri uri
     * @param extras 携带数据
     * @param context 上下文
     * @param interceptors 拦截器
     */
    public static void checkInterceptor(Uri uri, RouteBundleExtras extras, Ability context, List<RouteInterceptor> interceptors) {
        for (RouteInterceptor interceptor : interceptors) {
            if (interceptor.intercept(uri, extras, context)) {
                extras.putValue(Constants.KEY_RESUME_CONTEXT, context);
                interceptor.onIntercepted(uri, extras, context);
                throw new InterceptorException(interceptor);
            }
        }
    }

    public static IntentParams parseToBundle(URIParser parser) {
        IntentParams bundle = new IntentParams();
        Map<String, String> params = parser.getParams();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            bundle.setParam(entry.getKey(), entry.getValue());
        }
        return bundle;
    }

    /**
     * 判断能力是否正在运行且不是终止状态
     *
     * @param activity 上下文
     * @return 是否正在运行
     */
    public static boolean isValid(Ability activity) {
        return activity != null
                && !activity.isTerminating();
    }

    /**
     * 判断uri是否为空，或者主机地址为空
     *
     * @param uri
     * @return 是否为空
     */
    public static boolean isValidUri(Uri uri) {
        return uri != null && !TextTool.isNullOrEmpty(uri.getScheme()) && !TextTool.isNullOrEmpty(uri.getDecodedHost());
    }
}
