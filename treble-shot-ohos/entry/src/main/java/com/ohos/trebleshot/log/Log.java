package com.ohos.trebleshot.log;

public class Log {

    public static void d(String tag,String msg){
        System.out.println(tag+"--->"+msg);
    }

    public static void e(String tag,String msg){
        System.out.println(tag+"--->"+msg);
    }

    public static void d(String msg){
        d(null,msg);
    }

    public static void e(String msg){
        e(null,msg);
    }
}
