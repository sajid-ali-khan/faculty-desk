package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.UpdateClassAssignmentsRequest;
import com.sajid.college_app.services.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{classId}/assignments")
    public ResponseEntity<?> updateAssignmentsByClassId(
            @PathVariable("classId") Integer classId,
            @RequestBody List<UpdateClassAssignmentsRequest> request) {
        classService.updateAssignmentsByClassId(classId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{classId}/report")
    public ResponseEntity<?> getClassReport(@PathVariable("classId") Integer classId) {
        var report = classService.getClassReport(classId);
        return ResponseEntity.ok(report);
    }
}
