

package com.huawei.codelab;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

public interface ImplVideoMigration extends IRemoteBroker {

    void flyIn(
        /* [in] */ int _startTimemiles) throws RemoteException;

    void playControl(
        /* [in] */ int _controlCode,
        /* [in] */ int _extras) throws RemoteException;

    int flyOut() throws RemoteException;
};

