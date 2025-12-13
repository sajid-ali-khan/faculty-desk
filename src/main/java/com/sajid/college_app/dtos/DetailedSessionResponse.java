package com.sajid.college_app.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record DetailedSessionResponse(
        long id,
        LocalDateTime createdAt,
        Map<Long, AttendanceRecordResponse> attendanceRecordMap
) {
}
