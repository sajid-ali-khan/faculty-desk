package com.sajid.college_app.controllers;

import com.sajid.college_app.services.BranchService;
import com.sajid.college_app.services.FileToEntityService;
import com.sajid.college_app.services.SubjectService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/branch")
@AllArgsConstructor
public class BranchController {
    private final BranchService branchService;
    private final FileToEntityService fileToEntityService;
    private final SubjectService subjectService;

    @GetMapping("/scheme/{schemeCode}")
    public ResponseEntity<?> getBranchesByScheme(@PathVariable String schemeCode) {
        return ResponseEntity.ok(branchService.getBranchesByScheme(schemeCode));
    }

    @GetMapping("/{branchId}/semester/{semester}/subjects")
    public ResponseEntity<?> getSubjectsByBranchAndSemester(@PathVariable int branchId, @PathVariable int semester) {
        return ResponseEntity.ok(subjectService.getSubjectsByBranchAndSemester(branchId, semester));
    }

//    create a get method for fetching distinct branches
//    create a get method for fetching distinct semesters by branch code
//    create a get method for fetching distinct sections by branch code and semester
}
