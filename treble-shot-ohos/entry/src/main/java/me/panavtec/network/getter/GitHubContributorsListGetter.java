package me.panavtec.network.getter;

import me.panavtec.config.AppConfig;
import me.panavtec.network.getter.updatewithgithub.RemoteServer;
import me.panavtec.title.hmadapter.about.AboutContributorListProvider.ContributorObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * created by: Veli
 * date: 16.03.2018 15:46
 */

public class GitHubContributorsListGetter {

    public static List<ContributorObject> get() {
        List<ContributorObject> contributorObjects = new ArrayList<>();
        RemoteServer server = new RemoteServer(AppConfig.URI_REPO_APP_CONTRIBUTORS);

        try {
            String result = server.connect(null, null);

            JSONArray releases = new JSONArray(result);

            if (releases.length() > 0) {
                for (int iterator = 0; iterator < releases.length(); iterator++) {
                    JSONObject currentObject = releases.getJSONObject(iterator);

                    contributorObjects.add(new ContributorObject(currentObject.getString("login"),
                            currentObject.getString("url"),
                            currentObject.getString("avatar_url")));
                }
            }
        } catch (Exception e) {
            contributorObjects.add(new ContributorObject("Exception", "Exception","Exception"));
            e.printStackTrace();
        }

        return contributorObjects;
    }
}
