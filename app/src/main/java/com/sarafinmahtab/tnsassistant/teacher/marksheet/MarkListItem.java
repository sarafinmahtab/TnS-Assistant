package com.sarafinmahtab.tnsassistant.teacher.marksheet;

/**
 * Created by Arafin on 8/23/2017.
 */

public class MarkListItem {

    private String courseRegID, regNo, markSheetID, termTest1_Mark, termTest2_Mark, attendanceMark, vivaMark, finalExamMark, marksOutOf100;

    //SETTERS
    public void setCourseRegID(String courseRegID) {
        this.courseRegID = courseRegID;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public void setMarkSheetID(String markSheetID) {
        this.markSheetID = markSheetID;
    }

    public void setTermTest1_Mark(String termTest1_Mark) {
        this.termTest1_Mark = termTest1_Mark;
    }

    public void setTermTest2_Mark(String termTest2_Mark) {
        this.termTest2_Mark = termTest2_Mark;
    }

    public void setAttendanceMark(String attendanceMark) {
        this.attendanceMark = attendanceMark;
    }

    public void setVivaMark(String vivaMark) {
        this.vivaMark = vivaMark;
    }

    public void setFinalExamMark(String finalExamMark) {
        this.finalExamMark = finalExamMark;
    }

    public void setMarksOutOf100(String marksOutOf100) {
        this.marksOutOf100 = marksOutOf100;
    }

    //GETTERS
    public String getCourseRegID() {
        return courseRegID;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getMarkSheetID() {
        return markSheetID;
    }

    public String getTermTest1_Mark() {
        return termTest1_Mark;
    }

    public String getTermTest2_Mark() {
        return termTest2_Mark;
    }

    public String getAttendanceMark() {
        return attendanceMark;
    }

    public String getVivaMark() {
        return vivaMark;
    }

    public String getFinalExamMark() {
        return finalExamMark;
    }

    public String getMarksOutOf100() {
        return marksOutOf100;
    }
}
