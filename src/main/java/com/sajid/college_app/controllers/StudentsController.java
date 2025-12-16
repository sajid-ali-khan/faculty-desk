package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.StudentResponse;
import com.sajid.college_app.models.Student;
import com.sajid.college_app.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentsController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<?> getStudents(@RequestParam(required = false, defaultValue = "0") int pageNo,
                                      @RequestParam(required = false, defaultValue = "10") int pageSize,
                                      @RequestParam(required = false, defaultValue = "id") String sortBy,
                                      @RequestParam(required = false, defaultValue = "asc") String sortDir,
                                      @RequestParam(required = false) String query) {
        Sort sort = null;
        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else{
            sort = Sort.by(sortBy).descending();
        }
        if (query == null){
            return ResponseEntity.ok(studentService.getAllStudents(PageRequest.of(pageNo, pageSize, sort)));
        }
        return ResponseEntity.ok(studentService.searchStudentsByName(query, PageRequest.of(pageNo, pageSize, sort)));
    }
}
