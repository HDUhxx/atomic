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
package com.lzh.nonview.router.route;


import com.lzh.nonview.router.tools.Utils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.utils.net.Uri;

/**
 * 通过浏览器打开uri的路由工具
 *
 * @since 2021-04-06
 */
public class BrowserRoute implements IRoute {
    private static final BrowserRoute ROUTE = new BrowserRoute();
    Uri uri;
    public static BrowserRoute getInstance() {
        return ROUTE;
    }

    @Override
    public void open(Ability context) {
        Intent intent2 = new Intent();
        Operation operationCommonComponts = new Intent.OperationBuilder()
                .withUri(uri)
                .build();
        intent2.setOperation(operationCommonComponts);
        context.startAbility(intent2);
    }

    /**
     * canOpenRouter
     *
     * @param uri
     * @return boolean
     */
    public static boolean canOpenRouter(Uri uri) {
        return Utils.isHttp(uri.getScheme());
    }

    /**
     * setUri
     *
     * @param uri
     * @return IRoute
     */
    public IRoute setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

}
