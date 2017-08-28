package com.sarafinmahtab.tnsassistant;

/**
 * Created by Arafin on 8/28/2017.
 */

public class ServerAddress {
    private static String myServerAddress = "http://192.168.0.150/TnSAssistant/";

    public static void setMyServerAddress(String myServerAddress) {
        ServerAddress.myServerAddress = myServerAddress;
    }

    public static String getMyServerAddress() {
        return myServerAddress;
    }
}
