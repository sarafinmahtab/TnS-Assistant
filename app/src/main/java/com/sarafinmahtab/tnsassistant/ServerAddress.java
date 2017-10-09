package com.sarafinmahtab.tnsassistant;

/**
 * Created by Arafin on 8/28/2017.
 */

public class ServerAddress {
    private static String myServerAddress = "http://192.168.0.63/TnSAssistant/";
    private static String myUploadServerAddress = "http://192.168.0.63/TnSAssistant/uploads/";

    public static void setMyServerAddress(String myServerAddress) {
        ServerAddress.myServerAddress = myServerAddress;
    }

    public static void setMyUploadServerAddress(String myUploadServerAddress) {
        ServerAddress.myUploadServerAddress = myUploadServerAddress;
    }

    public static String getMyServerAddress() {
        return myServerAddress;
    }

    public static String getMyUploadServerAddress() {
        return myUploadServerAddress;
    }
}
