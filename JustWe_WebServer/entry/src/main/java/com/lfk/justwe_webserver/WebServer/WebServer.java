package com.lfk.justwe_webserver.WebServer;

import com.lfk.justwe_webserver.WebServer.Interface.OnLogResult;
import com.lfk.justwe_webserver.WebServer.Interface.OnPermissionFile;
import com.lfk.justwe_webserver.WebServer.Interface.OnWebResult;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.bundle.ElementName;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
//import com.lfk.justweengine.Utils.logger.Logger;

import java.io.File;
import java.util.HashMap;

/**
 * WebServer
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/6.
 */
public class WebServer {
    private Ability engine;
    private static HashMap<String, OnWebResult> webServerRule;
    private OnLogResult logResult;
    private WebServerService webServerService;
    private Integer webPort = null;
    private IAbilityConnection serviceConnection;
    private final int ERROR = -1;
    private final int LOG = 1;

    public WebServer(Ability engine) {
        this.engine = engine;
        init();
    }

    public WebServer(Ability engine, OnLogResult logResult) {
        this.engine = engine;
        this.logResult = logResult;
        init();
    }

    public WebServer(Ability engine, OnLogResult logResult, int webPort) {
        this.engine = engine;
        this.logResult = logResult;
        this.webPort = webPort;
        init();
    }

    private void init() {
        webServerRule = new HashMap<>();

        WebServerService.init(engine);

        serviceConnection = new IAbilityConnection() {
            @Override
            public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int resultCode) {
                webServerService = ((WebServerService.LocalBinder) iRemoteObject).getService();
                if (logResult != null)
                    logResult.OnResult(WebServerDefault.WebServerServiceConnected);
            }

            @Override
            public void onAbilityDisconnectDone(ElementName elementName, int resultCode) {
                webServerService = null;
                if (logResult != null)
                    logResult.OnResult(WebServerDefault.WebServerServiecDisconnected);
            }
        };
    }

    public void startWebService() {
        if (webServerService != null) {
            try {
                webServerService.startServer(new MessageHandler(EventRunner.current()),
                        (webPort == null) ? WebServerDefault.WebDefaultPort : webPort);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopWebService() {
        if (webServerService != null) {
            webServerService.stopServer();
        }
    }

    public void initWebService() {
        WebServerDefault.init(engine.getContext());
        // 连接Service
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(engine.getBundleName())
                .withAbilityName(WebServerService.class)
                .build();
        intent.setOperation(operation);
        engine.connectAbility(intent, serviceConnection);
    }

    public void callOffWebService() {
        engine.disconnectAbility(serviceConnection);
    }

    public void apply(String rule, OnWebResult result) {
        webServerRule.put(rule, result);
    }

    public void apply(final String rule) {
        webServerRule.put(rule, new OnPermissionFile() {
            @Override
            public File OnPermissionFile(String name) {
//                Logger.e(WebServerDefault.WebServerFiles  + rule + name);
                return new File(WebServerDefault.WebServerFiles  + rule + name);
            }
        });
    }

    public static OnWebResult getRule(String rule) {
        return webServerRule.get(rule);
    }

    public void setLogResult(OnLogResult logResult) {
        this.logResult = logResult;
    }

    public class MessageHandler extends EventHandler {

        public MessageHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        public void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId) {
                case LOG:
                    logResult.OnResult(event.object.toString());
                    break;
                case ERROR:
                    logResult.OnError(event.object.toString());
                    break;
            }
        }

        public void OnError(String str) {
            InnerEvent event = InnerEvent.get();
            event.eventId = ERROR;
            event.object = str;
            sendEvent(event);
        }

        public void OnResult(String str) {
            InnerEvent event = InnerEvent.get();
            event.eventId = LOG;
            event.object = str;
            sendEvent(event);
        }
    }
}
