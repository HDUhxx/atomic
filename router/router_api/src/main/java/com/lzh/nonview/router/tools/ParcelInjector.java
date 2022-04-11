package com.lzh.nonview.router.tools;

import ohos.aafwk.content.IntentParams;

/**
 * @program: Router_master
 * @description
 * @create: 2021-03-23 11:10
 **/
public interface ParcelInjector<T> {

    /**
     * 用于从Bundle数据容器中，取出数据并注入到实体类中对应的被{@link }所注解的字段中去。
     * @param entity 被注入的实体类对象。
     * @param bundle {@link ohos.aafwk.content.IntentParams}数据容器
     */
    void toEntity(T entity, IntentParams bundle);

    /**
     * 用于从实体类entity中，将对应的被{@link }注解过的字段的值。注入到Bundle数据容器中。
     * @param entity 实体类entity
     * @param bundle Bundle数据容器
     */
    void toBundle(T entity, IntentParams bundle);

    /**
     * <p>提供一个空实现的注入器。用于提供出去避免空指针
     */
    ParcelInjector NONE_INJECTOR = new ParcelInjector() {
        @Override
        public void toEntity(Object entity, IntentParams bundle) {}

        @Override
        public void toBundle(Object entity, IntentParams bundle) {}
    };

}
