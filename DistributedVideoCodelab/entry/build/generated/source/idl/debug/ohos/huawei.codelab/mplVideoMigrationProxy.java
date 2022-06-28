

package com.huawei.codelab;

import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.rpc.MessageParcel;
import ohos.rpc.MessageOption;
import ohos.system.version.SystemVersion;

public class mplVideoMigrationProxy implements ImplVideoMigration {
    private static final String DESCRIPTOR = "com.huawei.codelab.ImplVideoMigration";

    private static final int COMMAND_FLY_IN = IRemoteObject.MIN_TRANSACTION_ID + 0;
    private static final int COMMAND_PLAY_CONTROL = IRemoteObject.MIN_TRANSACTION_ID + 1;
    private static final int COMMAND_FLY_OUT = IRemoteObject.MIN_TRANSACTION_ID + 2;

    private final IRemoteObject remote;
    private static final int ERR_OK = 0;

    public mplVideoMigrationProxy(
        /* [in] */ IRemoteObject remote) {
        this.remote = remote;
    }

    @Override
    public IRemoteObject asObject() {
        return remote;
    }

    @Override
    public void flyIn(
        /* [in] */ int _startTimemiles) throws RemoteException {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        if (SystemVersion.getApiVersion() >= 6) {
            data.updateDataVersion(remote);
        }
        data.writeInterfaceToken(DESCRIPTOR);
        data.writeInt(_startTimemiles);

        try {
            remote.sendRequest(COMMAND_FLY_IN, data, reply, option);
            reply.readException();
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public void playControl(
        /* [in] */ int _controlCode,
        /* [in] */ int _extras) throws RemoteException {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        if (SystemVersion.getApiVersion() >= 6) {
            data.updateDataVersion(remote);
        }
        data.writeInterfaceToken(DESCRIPTOR);
        data.writeInt(_controlCode);
        data.writeInt(_extras);

        try {
            remote.sendRequest(COMMAND_PLAY_CONTROL, data, reply, option);
            reply.readException();
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public int flyOut() throws RemoteException {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        if (SystemVersion.getApiVersion() >= 6) {
            data.updateDataVersion(remote);
        }
        data.writeInterfaceToken(DESCRIPTOR);
        try {
            remote.sendRequest(COMMAND_FLY_OUT, data, reply, option);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }
};

