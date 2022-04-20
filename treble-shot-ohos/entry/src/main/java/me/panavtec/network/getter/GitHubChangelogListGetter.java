package me.panavtec.network.getter;

import me.panavtec.config.AppConfig;
import me.panavtec.network.getter.updatewithgithub.RemoteServer;
import me.panavtec.title.hmadapter.about.AboutChangeLogListProvider.VersionObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * created by: veli
 * date: 9/12/18 5:51 PM
 */
public class GitHubChangelogListGetter {

    public static List<VersionObject> get()
    {
        List<VersionObject> versionObjects = new ArrayList<>();
        RemoteServer server = new RemoteServer(AppConfig.URI_REPO_APP_UPDATE);

        try {
            String result = server.connect(null, null);

            JSONArray releases = new JSONArray(result);

            if (releases.length() > 0) {
                for (int iterator = 0; iterator < releases.length(); iterator++) {
                    JSONObject currentObject = releases.getJSONObject(iterator);

                    versionObjects.add(new VersionObject(
                            currentObject.getString("tag_name"),
                            currentObject.getString("name"),
                            currentObject.getString("body")
                    ));
                }
            }
        } catch (Exception e) {
            versionObjects.add(new VersionObject("Exception", "Exception", "Exception"));
            e.printStackTrace();
        }

        return versionObjects;
    }

}

