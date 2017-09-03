package com.sarafinmahtab.tnsassistant.teacher.examsetup;

import java.io.Serializable;

/**
 * Created by Arafin on 8/31/2017.
 */

public class CourseCustomize {

    private static boolean[] checkedAvgArray = new boolean[5];

    private static String customTT1Name, customTT2Name, customAttendanceName, customVivaName, customFinalName;
    private static String customTT1Percent, customTT2Percent, customAttendancePercent, customVivaPercent, customFinalPercent;

    //SETTERS
    public static void setCheckedAvgArray(boolean[] checkedAvgArray) {
        CourseCustomize.checkedAvgArray = checkedAvgArray;
    }

    public static void setCustomTT1Name(String customTT1Name) {
        CourseCustomize.customTT1Name = customTT1Name;
    }

    public static void setCustomTT2Name(String customTT2Name) {
        CourseCustomize.customTT2Name = customTT2Name;
    }

    public static void setCustomAttendanceName(String customAttendanceName) {
        CourseCustomize.customAttendanceName = customAttendanceName;
    }

    public static void setCustomVivaName(String customVivaName) {
        CourseCustomize.customVivaName = customVivaName;
    }

    public static void setCustomFinalName(String customFinalName) {
        CourseCustomize.customFinalName = customFinalName;
    }

    public static void setCustomTT1Percent(String customTT1Percent) {
        CourseCustomize.customTT1Percent = customTT1Percent;
    }

    public static void setCustomTT2Percent(String customTT2Percent) {
        CourseCustomize.customTT2Percent = customTT2Percent;
    }

    public static void setCustomAttendancePercent(String customAttendancePercent) {
        CourseCustomize.customAttendancePercent = customAttendancePercent;
    }

    public static void setCustomVivaPercent(String customVivaPercent) {
        CourseCustomize.customVivaPercent = customVivaPercent;
    }

    public static void setCustomFinalPercent(String customFinalPercent) {
        CourseCustomize.customFinalPercent = customFinalPercent;
    }

    //GETTERS

    public static boolean[] getCheckedAvgArray() {
        return checkedAvgArray;
    }

    public static String getCustomTT1Name() {
        return customTT1Name;
    }

    public static String getCustomTT2Name() {
        return customTT2Name;
    }

    public static String getCustomAttendanceName() {
        return customAttendanceName;
    }

    public static String getCustomVivaName() {
        return customVivaName;
    }

    public static String getCustomFinalName() {
        return customFinalName;
    }

    public static String getCustomTT1Percent() {
        return customTT1Percent;
    }

    public static String getCustomTT2Percent() {
        return customTT2Percent;
    }

    public static String getCustomAttendancePercent() {
        return customAttendancePercent;
    }

    public static String getCustomVivaPercent() {
        return customVivaPercent;
    }

    public static String getCustomFinalPercent() {
        return customFinalPercent;
    }
}
