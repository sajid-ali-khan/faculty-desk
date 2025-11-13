package com.sajid.college_app.services.keys;

public record ClassKey(
        BranchKey branchKey,
        int semester,
        String section
) {
}
