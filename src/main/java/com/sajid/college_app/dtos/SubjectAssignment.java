package com.sajid.college_app.dtos;

public record SubjectAssignment(
        int id,
        int subjectId,
        String subjectName,
        String subjectAbbreviation,
        String subjectType,
        int facultyId,
        String facultyName,
        String facultyCode
) {
}
