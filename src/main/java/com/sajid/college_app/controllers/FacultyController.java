package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.FacultyResponse;
import com.sajid.college_app.services.FacultyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faculties")
@AllArgsConstructor
@Slf4j
public class FacultyController {
    private final FacultyService facultyService;

    @GetMapping("/search")
    public ResponseEntity<?> searchFaculties(
            @RequestParam String query,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        List<FacultyResponse> faculties;
        faculties = facultyService.searchByNameOrCode(query, PageRequest.of(page, size));
        return ResponseEntity.ok(faculties);
    }

    @GetMapping
    public ResponseEntity<?> getAllFaculties(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) String query
    ) {
        log.debug("GetAllFaculties: page = {}, size = {}", page, size);
        if (query == null || query.isEmpty()) {
            Page<FacultyResponse> faculties = facultyService.getAllFaculties(PageRequest.of(page, size));
            return ResponseEntity.ok(faculties);
        }
        Page<FacultyResponse> faculties = facultyService.searchByNameOrCodeAndReturnAsPage(query, PageRequest.of(page, size));
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{facultyId}")
    public ResponseEntity<?> getById(
            @PathVariable("facultyId") int facultyId
    ) {
        log.debug("GetFacultyById: facultyId = {}", facultyId);
        FacultyResponse faculty = facultyService.getFacultyById(facultyId);
        return ResponseEntity.ok(faculty);
    }


    @GetMapping("/{facultyId}/assignments")
    public ResponseEntity<?> getFacultyAssignments(@PathVariable("facultyId") int facultyId){
        log.debug("GetFacultyAssignments: facultyId = {}", facultyId);
        return ResponseEntity.ok(facultyService.getFacultyAssignments(facultyId));
    }

    @GetMapping("/{facultyId}/assignments-enhanced")
    public ResponseEntity<?> getFacultyEnhancedAssignments(@PathVariable("facultyId") int facultyId){
        log.debug("GetFacultyEnhancedAssignments: facultyId = {}", facultyId);
        return ResponseEntity.ok(facultyService.getFacultyEnhancedAssignments(facultyId));
    }

    @PutMapping("/{facultyId}/reset-password" )
    public ResponseEntity<?> resetFacultyPassword(@PathVariable("facultyId") int facultyId, @RequestBody Map<String, String> requestBody){
        log.debug("ResetFacultyPassword: facultyId = {}", facultyId);
        Pair<Boolean, String> pair = facultyService.resetFacultyPassword(facultyId, requestBody.get("newPassword"), requestBody.get("oldPassword"));
        if (!pair.getFirst()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", pair.getSecond()
            ));
        }
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Password reset successfully."
        ));
    }
}
