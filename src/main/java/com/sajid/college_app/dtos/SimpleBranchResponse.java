package com.sajid.college_app.dtos;

public record SimpleBranchResponse(
        int id,
        int branchCode,
        String branchName,
        String abbreviation
) {
}
