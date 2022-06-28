

package com.huawei.codelab;

import com.huawei.codelab.mplVideoMigrationProxy;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.MessageOption;

public abstract class mplVideoMigrationStub extends RemoteObject implements ImplVideoMigration {
    private static final String DESCRIPTOR = "com.huawei.codelab.ImplVideoMigration";

    private static final int COMMAND_FLY_IN = IRemoteObject.MIN_TRANSACTION_ID + 0;
    private static final int COMMAND_PLAY_CONTROL = IRemoteObject.MIN_TRANSACTION_ID + 1;
    private static final int COMMAND_FLY_OUT = IRemoteObject.MIN_TRANSACTION_ID + 2;

    private static final int ERR_OK = 0;
    private static final int ERR_RUNTIME_EXCEPTION = -1;

    public mplVideoMigrationStub(
        /* [in] */ String descriptor) {
        super(descriptor);
    }

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    public static ImplVideoMigration asInterface(IRemoteObject object) {
        if (object == null) {
            return null;
        }

        ImplVideoMigration result = null;
        IRemoteBroker broker = object.queryLocalInterface(DESCRIPTOR);
        if (broker != null) {
            if (broker instanceof ImplVideoMigration) {
                result = (ImplVideoMigration)broker;
            }
        } else {
            result = new mplVideoMigrationProxy(object);
        }

        return result;
    }

    @Override
    public boolean onRemoteRequest(
        /* [in] */ int code,
        /* [in] */ MessageParcel data,
        /* [out] */ MessageParcel reply,
        /* [in] */ MessageOption option) throws RemoteException {
        String token = data.readInterfaceToken();
        if (!DESCRIPTOR.equals(token)) {
            return false;
        }
        switch (code) {
            case COMMAND_FLY_IN: {
                int _startTimemiles = data.readInt();
                flyIn(_startTimemiles);
                reply.writeNoException();
                return true;
            }
            case COMMAND_PLAY_CONTROL: {
                int _controlCode = data.readInt();
                int _extras = data.readInt();
                playControl(_controlCode, _extras);
                reply.writeNoException();
                return true;
            }
            case COMMAND_FLY_OUT: {
                int result;
                result = flyOut();
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            }
            default:
                return super.onRemoteRequest(code, data, reply, option);
        }
    }
};

