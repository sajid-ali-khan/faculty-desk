package com.sajid.college_app.dtos;

public record EnhancedFacultyAssignmentResponse(
        int id,
        String branchName,
        String branchAbbreviation,
        String subjectName,
        String subjectAbbreviation,
        String subjectType,
        int semester,
        String section,
        int classId,
        int subjectId,
        long classSubjectId,
        int sessionsCount,
        int presentCount,
        int totalAttendanceRecords,
        double avgAttendancePercentage,
        boolean markedToday
) {
}
