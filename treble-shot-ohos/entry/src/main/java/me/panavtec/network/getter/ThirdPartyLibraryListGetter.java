package me.panavtec.network.getter;

import me.panavtec.title.hmadapter.about.ThirdPartyLIBListProvider.ModuleItem;
import ohos.app.Context;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * created by: veli
 * date: 7/20/18 8:56 PM
 */
public class ThirdPartyLibraryListGetter {

    public static final String LIB_JSON_PATH = "resources/rawfile/libraries_index.json";

    public static List<ModuleItem> get(Context context)
    {
        try {
            InputStream inputStream = context.getResourceManager().getRawFileEntry(LIB_JSON_PATH).openRawFile();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int read;
            while ((read = inputStream.read()) != -1) {
                outputStream.write(read);
            }

            JSONObject jsonObject = new JSONObject(outputStream.toString());
            JSONArray dependenciesArray = jsonObject.getJSONArray("dependencies");

            ArrayList<ModuleItem> returnedList = new ArrayList<>();

            for (int i = 0; i < dependenciesArray.length(); i++)
                returnedList.add(new ModuleItem(dependenciesArray.getJSONObject(i)));

            return returnedList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
