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
import com.lzh.nonview.router.extras.RouteBundleExtras;
import com.lzh.nonview.router.interceptors.RouteInterceptor;
import com.lzh.nonview.router.interceptors.RouteInterceptorAction;
import ohos.aafwk.content.IntentParams;

import java.util.List;

/**
 *基于{@link IRoute}和{@link RouteInterceptorAction}，
 * 它的子类可以是<br> *
 * {@link IActionRoute}
 * 和{@link IActivityRoute}
 * * @param <T>的实型之一
 */
public interface IBaseRoute<T extends IBaseRoute> extends IRoute, RouteInterceptorAction<T>{
    /**
     * add extra bundle data to {@link RouteBundleExtras}
     * @param extras bundle data
     * @return {@link IBaseRoute}
     * @see IActionRoute
     * @see IActivityRoute
     */
    T addExtras(IntentParams extras);

    /**
     * 向容器添加拦截器
     * @param interceptor 拦截器实例
     * @return The real type
     */
    @Override
    T addInterceptor(RouteInterceptor interceptor);

    /**
     * 从容器中移除拦截器
     * @param interceptor interceptor instance
     * @return The real type
     */
    @Override
    T removeInterceptor(RouteInterceptor interceptor);

    /**
     * 删除您之前设置的所有拦截器
     * @return The real type
     */
    @Override
    T removeAllInterceptors();

    /**
     * 获取您之前设置的所有拦截器
     * @return all of interceptors
     */
    @Override
    List<RouteInterceptor> getInterceptors();

    @SuppressWarnings("unchecked")
    class EmptyBaseRoute<T extends IBaseRoute> extends EmptyRoute implements IBaseRoute<T> {

        public EmptyBaseRoute(InternalCallback internal) {
            super(internal);
        }

        @Override
        public T addExtras(IntentParams extras) {
            internal.getExtras().addExtras(extras);
            return (T) this;
        }

        @Override
        public T addInterceptor(RouteInterceptor interceptor) {
            internal.getExtras().addInterceptor(interceptor);
            return (T) this;
        }

        @Override
        public T removeInterceptor(RouteInterceptor interceptor) {
            internal.getExtras().removeInterceptor(interceptor);
            return (T) this;
        }

        @Override
        public T removeAllInterceptors() {
            internal.getExtras().removeAllInterceptors();
            return (T) this;
        }

        @Override
        public List<RouteInterceptor> getInterceptors() {
            return internal.getExtras().getInterceptors();
        }
    }
}
