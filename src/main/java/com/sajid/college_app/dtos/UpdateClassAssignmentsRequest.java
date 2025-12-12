package com.sajid.college_app.dtos;

public record UpdateClassAssignmentsRequest(
        long classSubjectId,
        int facultyId
) {
}
