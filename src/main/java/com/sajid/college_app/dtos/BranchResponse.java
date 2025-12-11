package com.sajid.college_app.dtos;

public record BranchResponse(
        int id,
        String branchName,
        String abbreviation,
        int branchCode,
        String scheme
) {
}
