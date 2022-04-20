package me.panavtec.title.hminterface;

import com.ohos.trebleshot.object.NetworkDevice;

public interface DeviceActionInterface {
    void update(NetworkDevice device);
    void remove(NetworkDevice device);
    void close(NetworkDevice device);
}
