package com.lzh.nonview.router;


import com.lzh.nonview.router.module.CreatorRouteRule;
import com.lzh.nonview.router.parser.URIParser;
import com.lzh.nonview.router.route.ICreatorInjector;
import com.lzh.nonview.router.tools.Cache;
import com.lzh.nonview.router.tools.RouterLog;
import com.lzh.nonview.router.tools.Utils;
import ohos.aafwk.content.IntentParams;
import ohos.utils.net.Uri;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author haoge on 2018/5/25
 */
public class InstanceRouter {

    private Uri uri;
    private IntentParams extra = new IntentParams();

    private InstanceRouter(Uri uri) {
        this.uri = uri;
    }

    static InstanceRouter build(String url) {
        return new InstanceRouter(Uri.parse(url));
    }

    public InstanceRouter addExtras(IntentParams extra2) {
        if (extra2 != null) {
            Set<String> keySet = extra2.keySet();
            for (String key : keySet) {
                Object value = extra2.getParam(key);
                this.extra.setParam(key, value);
            }
        }
        return this;
    }

    public <T> T createInstance() {
        try {
            Map<String, CreatorRouteRule> rules = Cache.getCreatorRules();
            URIParser parser = new URIParser(uri);
            String route = parser.getRoute();
            CreatorRouteRule rule = rules.get(route);
            if (rule == null) {
                RouterLog.d("Could not match rule for this uri");
                return null;
            }

            Object instance2 = rule.getTarget().newInstance();
            T instance = (T) instance2;

            if (instance instanceof ICreatorInjector) {
                IntentParams bundle = Utils.parseToBundle(parser);
                Field[] fields = instance.getClass().getDeclaredFields();
                String[] name = new String[fields.length];
                try {
                    for (int i = 0; i < name.length; i++) {
                        name[i] = fields[i].getName();
                        Set<String> set = bundle.keySet();
                        for (String key : set) {
                            if (key.equals(name[i])) {
                                invokeSet(instance, name[i], bundle.getParam(key));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ((ICreatorInjector) instance).inject(bundle);//回调回去了
            }

            return (T) instance;
        } catch (Throwable e) {
            RouterLog.e("Create target class from InstanceRouter failed. cause by:" + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 执行set方法
     *
     * @param o         执行对象
     * @param fieldName 属性
     * @param value     值
     */
    public static void invokeSet(Object o, String fieldName, Object value) {
        Method method = getSetMethod(o.getClass(), fieldName);
        try {
            method.invoke(o, new Object[]{value});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * java反射bean的set方法
     *
     * @param objectClass
     * @param fieldName
     * @return Method
     */
    @SuppressWarnings("unchecked")
    public static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
