package com.sarafinmahtab.tnsassistant.teacher.studentlist;

import android.graphics.Bitmap;

import com.sarafinmahtab.tnsassistant.ServerAddress;

/**
 * Created by Arafin on 8/11/2017.
 */

public class StudentItem {

    private String stdListID, stdListDisplay, stdListName, stdListReg, stdListMail, stdListPhone;

    public StudentItem(String stdListID, String stdListDisplay, String stdListName, String stdListReg, String stdListMail, String stdListPhone) {
        this.stdListID = stdListID;
        this.stdListDisplay = ServerAddress.getMyUploadServerAddress().concat(stdListDisplay);
        this.stdListName = stdListName;
        this.stdListReg = stdListReg;
        this.stdListMail = stdListMail;
        this.stdListPhone = stdListPhone;
    }

    //sETTERS
    public void setStdListID(String stdListID) {
        this.stdListID = stdListID;
    }

    public void setStdListDisplay(String stdListDisplay) {
        this.stdListDisplay = stdListDisplay;
    }

    public void setStdListName(String stdListName) {
        this.stdListName = stdListName;
    }

    public void setStdListReg(String stdListReg) {
        this.stdListReg = stdListReg;
    }

    public void setStdListMail(String stdListMail) {
        this.stdListMail = stdListMail;
    }

    public void setStdListPhone(String stdListPhone) {
        this.stdListPhone = stdListPhone;
    }

    //GETTERS
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

    public String getStdListMail() {
        return stdListMail;
    }

    public String getStdListPhone() {
        return stdListPhone;
    }
}
