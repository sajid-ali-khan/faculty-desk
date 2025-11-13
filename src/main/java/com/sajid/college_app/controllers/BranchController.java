package com.sajid.college_app.controllers;

import com.sajid.college_app.services.BranchService;
import com.sajid.college_app.services.FileToEntityService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/branches")
@AllArgsConstructor
public class BranchController {
    private final BranchService branchService;
    private final FileToEntityService fileToEntityService;

    @SneakyThrows
    @PostMapping("/bulk-upload")
    public ResponseEntity<?> bulkUploadBranches(@RequestParam("file")MultipartFile file){
        fileToEntityService.bulkUploadCourses(file);
        return ResponseEntity.ok().build();
    }
}
