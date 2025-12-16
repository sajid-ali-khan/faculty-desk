package com.sajid.college_app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class SubjectReport {

    @JsonIgnore
    public int presentCount;

    @JsonIgnore
    public int totalCount;
    int subjectId;
    String subjectName;
    public double attendancePercentage;

    public SubjectReport(int subjectId, String subjectName){
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.presentCount = 0;
        this.totalCount = 0;
        this.attendancePercentage = 0.0;
    }
}
