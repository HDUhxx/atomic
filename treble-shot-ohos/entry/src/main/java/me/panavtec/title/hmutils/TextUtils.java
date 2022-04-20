package me.panavtec.title.hmutils;


import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.utils.AddressedInterface;
import me.panavtec.title.ResourceTable;
import ohos.app.Context;

import java.util.HashMap;

/**
 * created by: Veli
 * date: 12.11.2017 11:14
 */

public class TextUtils {

    public static int getAdapterName(String adapterName)
    {
        HashMap<String, Integer> associatedNames = new HashMap<>();

        associatedNames.put("wlan", ResourceTable.String_text_interfaceWireless);
        associatedNames.put("p2p", ResourceTable.String_text_interfaceWifiDirect);
        associatedNames.put("bt-pan", ResourceTable.String_text_interfaceBluetooth);
        associatedNames.put("eth", ResourceTable.String_text_interfaceEthernet);
        associatedNames.put("tun", ResourceTable.String_text_interfaceVPN);
        associatedNames.put("unk", ResourceTable.String_text_interfaceUnknown);

        for (String displayName : associatedNames.keySet())
            if (adapterName.startsWith(displayName))
                return associatedNames.get(displayName);

        return -1;
    }

    public static String getAdapterName(Context context, NetworkDevice.Connection connection)
    {
        return getAdapterName(context, connection.adapterName);
    }

    public static String getAdapterName(Context context, AddressedInterface addressedInterface)
    {
        return getAdapterName(context, addressedInterface.getNetworkInterface().getDisplayName());
    }

    public static String getAdapterName(Context context, String adapterName)
    {
        int adapterNameResource = getAdapterName(adapterName);

        if (adapterNameResource == -1)
            return adapterName;

        return context.getString(adapterNameResource);
    }
    
    
    public static String getLetters(String text, int length) {
        if (text == null || text.length() == 0)
            text = "?";

        int breakAfter = --length;
        StringBuilder stringBuilder = new StringBuilder();

        for (String letter : text.split(" ")) {
            if (stringBuilder.length() > breakAfter)
                break;

            if (letter.length() == 0)
                continue;

            stringBuilder.append(letter.charAt(0));
        }

        return stringBuilder.toString().toUpperCase();
    }


    public static boolean searchWord(String word, String searchThis) {
        return searchThis == null
                || searchThis.length() == 0
                || word.toLowerCase().contains(searchThis.toLowerCase());
    }

    public static String trimText(String text, int length) {
        if (text == null || text.length() <= length)
            return text;

        return text.substring(0, length);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
