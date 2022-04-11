package com.lzh.nonview.router.demo.pojo;

import com.lzh.nonview.router.anno.RouterRule;
import com.lzh.nonview.router.route.ICreatorInjector;

import ohos.aafwk.content.IntentParams;

/**
 * 测试bean
 *
 * @since 2021-03-20
 **/
@RouterRule("creator/user")
public class User implements ICreatorInjector {
    private String name;

    /**
     * 空构造
     */

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void inject(IntentParams bundle) {
    }

    @Override
    public String toString() {
        return "User{"
                + "name='" + name + '\''
                + '}';
    }
}

