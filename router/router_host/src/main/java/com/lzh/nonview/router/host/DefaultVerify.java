package com.lzh.nonview.router.host;


import ohos.app.Context;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

import java.util.List;

/**
 * <b>Default impl for {@link RemoteVerify}</b>
 *
 * <p>以确保远程客户端应与服务使用相同的程序包名称。.
 */
final class DefaultVerify implements RemoteVerify{

    @Override
    public boolean verify(Context context,int uid) throws RemoteException {
        String packageName = context.getBundleName();
        List<String> packages = context.getBundleManager().getBundlesForUid(uid);
        for (String pack: packages) {
            if (packageName.equals(pack)) {
                return true;
            }
        }
        return false;
    }
}
