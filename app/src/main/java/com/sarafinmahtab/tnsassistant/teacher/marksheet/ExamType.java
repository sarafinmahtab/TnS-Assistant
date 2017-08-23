package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.widget.Toast;

/**
 * Created by Arafin on 8/24/2017.
 */

public class ExamType {
    private String test1, test2, attendance, viva, final_exam;

    //SETTER
    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public void setViva(String viva) {
        this.viva = viva;
    }

    public void setFinal_exam(String final_exam) {
        this.final_exam = final_exam;
    }

    //GETTER
    public String getTest1() {
        return test1;
    }

    public String getTest2() {
        return test2;
    }

    public String getAttendance() {
        return attendance;
    }

    public String getViva() {
        return viva;
    }

    public String getFinal_exam() {
        return final_exam;
    }
}
