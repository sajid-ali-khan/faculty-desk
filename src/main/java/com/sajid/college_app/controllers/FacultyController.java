package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.FacultyResponse;
import com.sajid.college_app.services.FacultyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
            @RequestParam String searchBy,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        List<FacultyResponse> faculties;
        if (searchBy.equalsIgnoreCase("name")) {
            faculties = facultyService.searchFacultyByName(query, PageRequest.of(page, size));
        } else if (searchBy.equalsIgnoreCase("code")) {
            faculties = facultyService.searchFacultyByCode(query, PageRequest.of(page, size));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid searchBy parameter. Use 'name' or 'code'."
            ));
        }
        return ResponseEntity.ok(faculties);
    }


    @GetMapping("/{facultyId}/assignments")
    public ResponseEntity<?> getFacultyAssignments(@PathVariable("facultyId") int facultyId){
        log.debug("GetFacultyAssignments: facultyId = {}", facultyId);
        return ResponseEntity.ok(facultyService.getFacultyAssignments(facultyId));
    }
}
