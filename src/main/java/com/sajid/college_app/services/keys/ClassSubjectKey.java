package com.sajid.college_app.services.keys;

public record ClassSubjectKey(
        BranchKey branchKey,
        int semester,
        SubjectKey subjectKey
) {
}
