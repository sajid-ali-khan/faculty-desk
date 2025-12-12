package com.sajid.college_app.controllers;

import com.sajid.college_app.repositories.ClassRepository;
import com.sajid.college_app.services.BranchService;
import com.sajid.college_app.services.ClassService;
import com.sajid.college_app.services.FileToEntityService;
import com.sajid.college_app.services.SubjectService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/branches")
@AllArgsConstructor
public class BranchController {
    private final BranchService branchService;
    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<?> getDistinctBranches() {
        return ResponseEntity.ok(branchService.getDistinctBranches());
    }
    @GetMapping("/branchCode/{branchCode}/semesters")
    public ResponseEntity<?> getDistinctSemestersByBranchCode(@PathVariable int branchCode) {
        return ResponseEntity.ok(branchService.getDistinctSemestersByBranchCode(branchCode));
    }

    @GetMapping("/branchCode/{branchCode}/semesters/{semester}/sections")
    public ResponseEntity<?> getDistinctSectionsByBranchCodeAndSemester(@PathVariable int branchCode, @PathVariable int semester) {
        return ResponseEntity.ok(branchService.getDistinctSectionsByBranchCodeAndSemester(branchCode, semester));
    }
}
