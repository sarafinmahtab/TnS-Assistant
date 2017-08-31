package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import java.util.ArrayList;

/**
 * Created by Arafin on 8/31/2017.
 */

public class CourseCustomize {

    private ArrayList<Boolean> checkedAvgArray;

    private String customTT1Name, customTT2Name, customAttendanceName, customVivaName, customFinalName;
    private String customTT1Percent, customTT2Percent, customAttendancePercent, customVivaPercent, customFinalPercent;

    //SETTERS
    public void setCheckedAvgArray(ArrayList<Boolean> checkedAvgArray) {
        this.checkedAvgArray = checkedAvgArray;
    }

    public void setCustomTT1Name(String customTT1Name) {
        this.customTT1Name = customTT1Name;
    }

    public void setCustomTT2Name(String customTT2Name) {
        this.customTT2Name = customTT2Name;
    }

    public void setCustomAttendanceName(String customAttendanceName) {
        this.customAttendanceName = customAttendanceName;
    }

    public void setCustomVivaName(String customVivaName) {
        this.customVivaName = customVivaName;
    }

    public void setCustomFinalName(String customFinalName) {
        this.customFinalName = customFinalName;
    }

    public void setCustomTT1Percent(String customTT1Percent) {
        this.customTT1Percent = customTT1Percent;
    }

    public void setCustomTT2Percent(String customTT2Percent) {
        this.customTT2Percent = customTT2Percent;
    }

    public void setCustomAttendancePercent(String customAttendancePercent) {
        this.customAttendancePercent = customAttendancePercent;
    }

    public void setCustomVivaPercent(String customVivaPercent) {
        this.customVivaPercent = customVivaPercent;
    }

    public void setCustomFinalPercent(String customFinalPercent) {
        this.customFinalPercent = customFinalPercent;
    }

    //GETTERS

    public ArrayList<Boolean> getCheckedAvgArray() {
        return checkedAvgArray;
    }

    public String getCustomTT1Name() {
        return customTT1Name;
    }

    public String getCustomTT2Name() {
        return customTT2Name;
    }

    public String getCustomAttendanceName() {
        return customAttendanceName;
    }

    public String getCustomVivaName() {
        return customVivaName;
    }

    public String getCustomFinalName() {
        return customFinalName;
    }

    public String getCustomTT1Percent() {
        return customTT1Percent;
    }

    public String getCustomTT2Percent() {
        return customTT2Percent;
    }

    public String getCustomAttendancePercent() {
        return customAttendancePercent;
    }

    public String getCustomVivaPercent() {
        return customVivaPercent;
    }

    public String getCustomFinalPercent() {
        return customFinalPercent;
    }
}
