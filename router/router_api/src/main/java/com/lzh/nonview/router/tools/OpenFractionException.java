package com.lzh.nonview.router.tools;

/**
 * @program: Router_master
 * @description
 * @create: 2021-03-15 14:21
 **/
public class OpenFractionException extends RuntimeException{
    //定义无参构造方法
    public OpenFractionException() {
        super();
    }

    //定义有参构造方法
    public OpenFractionException(String message) {
        super(message);
    }

}
