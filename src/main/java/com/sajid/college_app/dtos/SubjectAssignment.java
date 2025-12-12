package com.sajid.college_app.dtos;

public record SubjectAssignment(
        int id,
        String subjectName,
        String subjectAbbreviation,
        String subjectType,
        String facultyId,
        String facultyName,
        String facultyCode
) {
}
