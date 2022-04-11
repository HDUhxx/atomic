package com.lzh.nonview.router.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ActivityLauncher
 *
 * @since 2021-04-06
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface ActivityLauncher {
    /**
     * value
     *
     * @return Class
     */
    Class value();
}
