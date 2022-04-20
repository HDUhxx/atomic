package com.ohos.trebleshot.utils;

import java.net.NetworkInterface;

/**
 * created by: Veli
 * date: 5.11.2017 15:02
 */

public class AddressedInterface
{
    private final NetworkInterface networkInterface;
    private final String associatedAddress;

    public AddressedInterface(NetworkInterface networkInterface, String associatedAddress)
    {
        this.networkInterface = networkInterface;
        this.associatedAddress = associatedAddress;
    }

    public String getAssociatedAddress()
    {
        return associatedAddress;
    }

    public NetworkInterface getNetworkInterface()
    {
        return networkInterface;
    }
}
