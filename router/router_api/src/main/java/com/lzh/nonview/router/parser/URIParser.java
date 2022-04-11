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
package com.lzh.nonview.router.parser;


import com.lzh.nonview.router.tools.Utils;
import ohos.agp.utils.TextTool;
import ohos.utils.net.Uri;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 解析器将uri解析为scheme / host / params .etc
 * Created by lzh on 16/9/5.
 */
public class URIParser {

    private Uri uri;
    private String route;
    private Map<String,String> params;

    public URIParser(Uri uri) {
        this.uri = uri;
        parse();
    }

    private void parse() {
        this.route = Utils.format(uri.getScheme() + "://" + uri.getDecodedHost() + uri.getDecodedPath());
        String query = uri.getEncodedQuery();
        if (!TextTool.isNullOrEmpty(query)) {
            params = parseParams(query);
        } else {
            params = new HashMap<>();
        }
    }

    /**
     * 解析参数形式查询字符串
     * <p>
     * To 支持解析列表捆绑，使用 {@link IdentityHashMap} to hold key-value
     * </p>
     * @param query 在uri中查询
     * @return 映射包含由uri中的查询解析的键值数据
     */
    static Map<String,String> parseParams(String query) {
        Map<String,String> params = new IdentityHashMap<>();
        String[] split = query.split("&");
        for (String param : split) {
            if (!param.contains("=")) {
                continue;
            }
            int index = param.indexOf('=');
            //noinspection RedundantStringConstructorCall
            String pp = param.substring(0, index);
            params.put(pp, URLDecoder.decode(param.substring(index + 1)));
        }
        return params;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getRoute() {
        return route;
    }
}
