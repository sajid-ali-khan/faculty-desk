package com.sajid.college_app.dtos;

import java.util.List;

public record ClassAssignmentResponse(
        int id,
        String branchName,
        String branchAbbreviation,
        int semester,
        String section,
        List<SubjectAssignment> subjectAssignments
) {
}
