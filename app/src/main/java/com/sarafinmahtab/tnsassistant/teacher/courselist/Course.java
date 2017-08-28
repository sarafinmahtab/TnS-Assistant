package com.sarafinmahtab.tnsassistant.teacher.courselist;

/**
 * Created by Arafin on 6/8/2017.
 */

public class Course {

    private String teacherID, courseID, courseCode, courseTitle, session, credit;

    public Course(String teacherID, String courseID, String courseCode, String courseTitle, String session, String credit) {
        this.teacherID = teacherID;
        this.courseID = courseID;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.session = session;
        this.credit = credit;
    }

    public String getTeacherID() {
        return  teacherID;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getSession() {
        return session;
    }

    public String getCredit() {
        return credit;
    }
}
