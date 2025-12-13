package com.sajid.college_app.dtos;

public record AttendanceRecordResponse(
        long id,
        String studentRoll,
        String studentName,
        boolean present
) {
}
