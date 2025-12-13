package com.sajid.college_app.dtos;

public record FacultyAssignmentResponse(
        int id,
        String branchName,
        String branchAbbreviation,
        String subjectName,
        String subjectAbbreviation,
        int semester,
        String section
) {
}
