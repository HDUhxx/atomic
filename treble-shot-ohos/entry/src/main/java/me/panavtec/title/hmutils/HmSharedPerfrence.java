package me.panavtec.title.hmutils;

import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

public final class HmSharedPerfrence {
    //    Context context;
    static DatabaseHelper databaseHelper;

    //    public HmSharedPerfrence(Context context) {
//        this.context = context;
//        databaseHelper = new DatabaseHelper(context);
//    }
    private HmSharedPerfrence() {
    }

    private static HmSharedPerfrence perfrence;

    public static HmSharedPerfrence getInstance(Context context) {
        if (perfrence == null) {
            perfrence = new HmSharedPerfrence();
//                databaseHelper = new DatabaseHelper(context.getApplicationContext());
            databaseHelper = new DatabaseHelper(context);
        }
        return perfrence;
    }

    public boolean isFrist(String name) {
        Preferences preferences = databaseHelper.getPreferences(name);
//        clickCounter = preferences.getInt(counterKey, 0);
        return preferences.getBoolean(name, false);
    }

    public void saveFrist(String name) {
        Preferences preferences = databaseHelper.getPreferences(name);
        preferences.putBoolean(name, true);
        preferences.flush();

    }

    public String getParam(String name) {
//        databaseHelper = new DatabaseHelper(context);
        Preferences preferences = databaseHelper.getPreferences(name);
        return preferences.getString(name, "");
    }

    public void saveParam(String name, String param) {
        Preferences preferences = databaseHelper.getPreferences(name);
        preferences.putString(name, param);
        preferences.flush();
    }

    public void apendParam(String name, String param) {
        Preferences preferences = databaseHelper.getPreferences(name);
        String str = getParam(name);
        String newString = str.endsWith("&&") ? str + param : str + "&&" + param;
        preferences.putString(name, newString);
        preferences.flush();
    }

    public void cutParam(String name, String param) {
        String s = getParam(name);
        String medString = s.replace(param, "");
        String latsString = medString.replace("&&&&", "&&");
        if (latsString.length() > 2 && latsString.startsWith("&&")) {
            latsString = latsString.substring(2, latsString.length());
        }
        if (latsString.length() > 2 && latsString.substring(latsString.length() - 3, latsString.length() - 1).equals("&&")) {
            latsString = latsString.substring(0, latsString.length() - 3);
        }
        if (latsString.length() <= 2) {
            latsString = "";
        }
        saveParam(name, latsString);

    }

    public void replaceParam(String name, String oldName, String newName) {
        String s = getParam(name);
        String lastSting = s.replace(oldName, newName);
        saveParam(name, lastSting);
    }

    public void saveOtherInConFile(String name, String param) {
        Preferences preferences = databaseHelper.getPreferences(HmAddConstant.HM_SHAREPERENCE_COMFILE);
        preferences.putString(name, param);
        preferences.flush();
    }

    public String getOtherInConFile(String name) {
        Preferences preferences = databaseHelper.getPreferences(HmAddConstant.HM_SHAREPERENCE_COMFILE);
        return preferences.getString(name, "");
    }
}
