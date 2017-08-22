package com.sarafinmahtab.tnsassistant.teacher.marksheet;

/**
 * Created by Arafin on 8/23/2017.
 */

public class MarkListItem {

    String candidateID, candidateFirstName, candidateLastName, candidateReg, markSheetID;

    public MarkListItem(String candidateID, String candidateFirstName, String candidateLastName, String candidateReg, String markSheetID) {
        this.candidateID = candidateID;
        this.candidateFirstName = candidateFirstName;
        this.candidateLastName = candidateLastName;
        this.candidateReg = candidateReg;
        this.markSheetID = markSheetID;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public String getCandidateName() {
        return candidateFirstName + " " + candidateLastName;
    }

    public String getCandidateReg() {
        return candidateReg;
    }

    public String getMarkSheetID() {
        return markSheetID;
    }
}
