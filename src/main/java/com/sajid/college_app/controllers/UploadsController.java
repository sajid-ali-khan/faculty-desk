package com.sajid.college_app.controllers;

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
@RequestMapping("/api/uploads")
@AllArgsConstructor
public class UploadsController {
    private final FileToEntityService fileToEntityService;

    @SneakyThrows
    @PostMapping("/students")
    public ResponseEntity<?> bulkUploadStudents(@RequestParam("file") MultipartFile file){
        fileToEntityService.bulkUploadStudents(file);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/courses")
    public ResponseEntity<?> bulkUploadCourses(@RequestParam("file") MultipartFile file){
        fileToEntityService.bulkUploadCourses(file);
        return ResponseEntity.ok().build();
    }
}
