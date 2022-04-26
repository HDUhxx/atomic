package com.lfk.justwe_webserver;

import com.lfk.justwe_webserver.WebServer.Interface.OnLogResult;
import com.lfk.justwe_webserver.WebServer.Interface.OnPostData;
import com.lfk.justwe_webserver.WebServer.Interface.OnWebFileResult;
import com.lfk.justwe_webserver.WebServer.Interface.OnWebStringResult;
import com.lfk.justwe_webserver.WebServer.WebServer;
import com.lfk.justwe_webserver.WebServer.WebServerDefault;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.ScrollView;
import ohos.agp.components.Text;

import java.io.File;
import java.util.HashMap;

public class MainAbility extends Ability implements OnLogResult {
    private WebServer server;
    private Text textView;
    private ScrollView scrollView;
    private boolean open = false;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        textView = (Text) findComponentById(ResourceTable.Id_main_log);
        scrollView = (ScrollView) findComponentById(ResourceTable.Id_scrollview);

        server = new WebServer(this, this);
        server.initWebService();

        server.apply("/lfk", new OnWebStringResult() {
            @Override
            public String OnResult() {
                return "=======";
            }
        });

        server.apply("/main", new OnWebFileResult() {
            @Override
            public File returnFile() {
                return new File(WebServerDefault.WebServerFiles + "/" + "welcome.html");
            }
        });


        server.apply("/lfkdsk", new OnPostData() {
            @Override
            public String OnPostData(HashMap<String, String> hashMap) {
                String S = hashMap.get("LFKDSK");
                return "=_=";
            }
        });

        Image fab = (Image) findComponentById(ResourceTable.Id_fab) ;
        fab.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (!open) {
                    server.startWebService();
                    open = true;
                } else {
                    server.stopWebService();
                    open = false;
                }
            }
        });
    }

    @Override
    public void OnResult(String log) {
//        Log.e("log", log);
        textView.append(log + "\n");
        scrollView.fluentScrollYTo(scrollView.getBottom());
    }

    @Override
    public void OnError(String error) {
//        Log.e("error", error);
    }

    @Override
    protected void onStop() {
        super.onStop();
        server.callOffWebService();
    }

}
