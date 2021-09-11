/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ohos.samples.networkmanagement.slice;

import ohos.samples.networkmanagement.ResourceTable;
import ohos.samples.networkmanagement.utils.ThreadPoolUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.DataFlowStatistics;
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;
import ohos.net.NetStatusCallback;
import ohos.rpc.RemoteException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * NetRequestSlice
 */
public class NetRequestSlice extends AbilitySlice {

    private static final String TAG = NetRequestSlice.class.getSimpleName();

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, TAG);

    private long rx;

    private long tx;

    private Text inputText;

    private Text outText;

    private NetManager netManager;

    private Text statisticsText;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_net_request_slice);

        initComponents();
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        Component startButton = findComponentById(ResourceTable.Id_start_button);
        inputText = (Text) findComponentById(ResourceTable.Id_input_text);
        outText = (Text) findComponentById(ResourceTable.Id_out_text);
        statisticsText = (Text) findComponentById(ResourceTable.Id_statistics_text);
        startButton.setClickedListener(this::netRequest);
    }

    /**
     * 1 使用NetManager对象获取网路实例和网络状态
     * 2 利用NetManager对象获取NetHandle对象
     * 3 使用NetHandle对象指明连接代理和url，用URLConnection对象接收
     * 4 connection = (HttpURLConnection) urlConnection
     * 5 使用connection指定连接方式，并进行连接
     * 6 获取数据，urlConnection.getInputStream()
     * 7 输出数据，outputStream.toByteArray()
     * @param component
     */
    private void netRequest(Component component) {
        //  创建一个指定网络实例。
        netManager = NetManager.getInstance(null);
        //  检查网络是否激活
        if (!netManager.hasDefaultNet()) {
            return;
        }
        // 线程执行
        ThreadPoolUtil.submit(() -> {
            // 获取默认激活的数据网络。
            // NetHandle保存数据网络的句柄。您可以将套接字绑定到 NetHandle 实例以访问 Internet。
            NetHandle netHandle = netManager.getDefaultNet();
            //  接收默认数据网络的状态更改通知。
            netManager.addDefaultNetStatusCallback(callback);

            HttpURLConnection connection = null;
            //  ByteArrayOutputStream 这个类实现了一个输出流，其中数据被写入一个字节数组。 缓冲区会随着数据写入而自动增长。可以使用 toByteArray() 和 toString() 检索数据。
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                //   请求的URL
                String urlString = inputText.getText();
                URL url = new URL(urlString);
                //  使用指定的代理访问 URL。
                //  java.net.Proxy.NO_PROXY 代表 DIRECT 连接的代理设置，基本上告诉协议处理程序不要使用任何代理
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection = (HttpURLConnection) urlConnection;
                }
                connection.setRequestMethod("GET");
                connection.connect();
                trafficDataStatistics(false);
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    byte[] cache = new byte[2 * 1024];
                    int len = inputStream.read(cache);
                    while (len != -1) {
                        outputStream.write(cache, 0, len);
                        len = inputStream.read(cache);
                    }
                } catch (IOException e) {
                    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                }
                String result = new String(outputStream.toByteArray());
                getUITaskDispatcher().asyncDispatch(() -> outText.setText(result));
                trafficDataStatistics(true);
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
                HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });
    }

    private final NetStatusCallback callback = new NetStatusCallback() {
        @Override
        public void onAvailable(NetHandle handle) {
            HiLog.info(LABEL_LOG, "%{public}s", "NetStatusCallback onAvailable");
        }

        @Override
        public void onBlockedStatusChanged(NetHandle handle, boolean blocked) {
            HiLog.info(LABEL_LOG, "%{public}s", "NetStatusCallback onBlockedStatusChanged");
        }
    };

    private void trafficDataStatistics(boolean isStart) {
        int uid = 0;
        try {
            uid = getBundleManager().getUidByBundleName(getBundleName(), 0);
        } catch (RemoteException e) {
            HiLog.error(LABEL_LOG, "%{public}s", "trafficDataStatistics RemoteException");
        }
        if (isStart) {
            rx = DataFlowStatistics.getUidRxBytes(uid);
            tx = DataFlowStatistics.getUidTxBytes(uid);
        } else {
            rx = DataFlowStatistics.getUidRxBytes(uid) - rx;
            tx = DataFlowStatistics.getUidTxBytes(uid) - tx;
            getUITaskDispatcher().asyncDispatch(() -> statisticsText.setText(
                "TrafficDataStatistics:" + System.lineSeparator() + "Uplink traffic:" + rx + System.lineSeparator()
                    + "Downstream traffic:" + tx));
        }
    }
}
