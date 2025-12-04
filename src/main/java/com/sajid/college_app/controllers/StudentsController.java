package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.StudentResponse;
import com.sajid.college_app.models.Student;
import com.sajid.college_app.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentsController {

    private final StudentService studentService;

    @GetMapping("/list")
    public List<StudentResponse> getStudents(){
        return studentService.getAllStudents();
    }
}
