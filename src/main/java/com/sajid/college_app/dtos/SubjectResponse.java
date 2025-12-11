package com.sajid.college_app.dtos;

public record SubjectResponse(
        int id,
        String subjectName,
        String abbreviation,
        String subjectType
) {
}
