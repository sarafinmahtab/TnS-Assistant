package com.sarafinmahtab.tnsassistant.student.stdcourses;

/**
 * Created by Arafin on 8/12/2017.
 */

public class CourseItem {

    private String stdListCourseID, stdListCourseCode, stdListCourseTitle, stdListCredit, stdListSemester;

    public CourseItem(String stdListCourseID, String stdListCourseCode, String stdListCourseTitle, String stdListCredit, String stdListSemester) {
        this.stdListCourseID = stdListCourseID;
        this.stdListCourseCode = stdListCourseCode;
        this.stdListCourseTitle = stdListCourseTitle;
        this.stdListCredit = stdListCredit;
        this.stdListSemester = stdListSemester;
    }

    public String getStdListCourseID() {
        return stdListCourseID;
    }

    public String getStdListCourseCode() {
        return stdListCourseCode;
    }

    public String getStdListCourseTitle() {
        return stdListCourseTitle;
    }

    public String getStdListCredit() {
        return stdListCredit;
    }

    public String getStdListSemester() {
        return stdListSemester;
    }
}
