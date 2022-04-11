package com.lzh.nonview.router.module;

import ohos.aafwk.content.IntentParams;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.net.Uri;

import java.util.HashMap;

public class RemoteRule implements Sequenceable {
    // 目标类别名称
    private String name;
    // RouteRule中的参数
    private HashMap params;
    // IRemoteFactory创建的额外捆绑包.
    private IntentParams extra;
    // type in [Action, Activity]
    private int type;
    private RouteRule rule;

  public RemoteRule() {}

  public static RemoteRule create(RouteRule rule, IntentParams extra) {
    RemoteRule remote = new RemoteRule();
     remote.name = rule.getRuleClz();
     remote.params = rule.getParams();
     remote.type = (rule instanceof ActivityRouteRule) ? 0 : 1;
     remote.extra = extra;
    return remote;
    }

  public IntentParams getExtra() {
        return extra;
    }
  public RouteRule getRule() {
        if (rule != null) {
            return rule;
        }
        switch (type) {
            case 0:
                rule = new ActivityRouteRule(name).setParams(params);
                break;
            default:
                rule = new ActionRouteRule(name).setParams(params);
        }
        return rule;
    }


    @Override
  public boolean unmarshalling(Parcel parcel) {
    name = parcel.readString();
    params= (HashMap) parcel.readMap();
    type=parcel.readInt();
     return  parcel.readSequenceable(extra);}


  @Override
  public boolean marshalling(Parcel parcel) {
   parcel.writeSequenceable(extra);
   parcel.writeMap(params);
   return parcel.writeString(name) &&  parcel.writeInt(type);
  }

    /**
     *  序列化对象的内部构造器，必须实现
     */
    public static final Sequenceable.Producer PRODUCER = new Sequenceable.Producer() {
        @Override
        public RemoteRule createFromParcel(Parcel parcel) {
            RemoteRule instance =new RemoteRule();
            instance.unmarshalling(parcel);
            return instance;
        }
    };
}
