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
package com.lzh.nonview.router.exception;

import com.lzh.nonview.router.interceptors.RouteInterceptor;

/**
 * Throw this exception when routing events were intercepted。
 * @author haoge
 */
public class InterceptorException extends RuntimeException {

    private final RouteInterceptor interceptor;

    /**
     * 拦截器异常
     * @param interceptor 拦截器
     */
    public InterceptorException(RouteInterceptor interceptor) {
        super("This route action has been intercepted");
        this.interceptor = interceptor;
    }

    /**
     * Provide users with access to the interceptor
     * @return The interceptor who intercept the event
     */
    public RouteInterceptor getInterceptor() {
        return interceptor;
    }
}
