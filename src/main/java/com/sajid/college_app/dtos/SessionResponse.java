package com.sajid.college_app.dtos;

import java.time.LocalDateTime;

public record SessionResponse(
        long id,
        LocalDateTime updatedAt,
        LocalDateTime createdAt,
        int presentCount,
        int totalCount
) {
}
