package com.sajid.college_app.dtos;

import java.util.Map;

public record StudentReport(
        int studentId,
        String studentRoll,
        String studentName,
        double overallPercentage,
        Map<Integer, SubjectReport> subjectsReports
) {
}
