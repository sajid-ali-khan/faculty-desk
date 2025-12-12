package com.sajid.college_app.controllers;

import com.sajid.college_app.services.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
@AllArgsConstructor
public class ClassController {
    private final ClassService classService;

    @GetMapping("/{classId}/assignments")
    public ResponseEntity<?> getAssignmentsByClassId(@PathVariable("classId") Integer classId) {
        var assignments = classService.getAssignmentsByClassId(classId);
        return ResponseEntity.ok(assignments);
    }
}
