package com.sarafinmahtab.tnsassistant.teacher.studentlist;

import android.graphics.Bitmap;

/**
 * Created by Arafin on 8/11/2017.
 */

public class StudentItem {

    private String stdListID, stdListDisplay, stdListName, stdListReg, stdListPhone;

    public StudentItem(String stdListID, String stdListDisplay, String stdListName, String stdListReg, String stdListPhone) {
        this.stdListID = stdListID;
        this.stdListDisplay = stdListDisplay;
        this.stdListName = stdListName;
        this.stdListReg = stdListReg;
        this.stdListPhone = stdListPhone;
    }

    public String getStdListID() {
        return stdListID;
    }

    public String getStdListDisplay() {
        return stdListDisplay;
    }

    public String getStdListName() {
        return stdListName;
    }

    public String getStdListReg() {
        return stdListReg;
    }

    public String getStdListPhone() {
        return stdListPhone;
    }
}
