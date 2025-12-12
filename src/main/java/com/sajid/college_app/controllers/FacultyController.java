package com.sajid.college_app.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/faculties")
@AllArgsConstructor
public class FacultyController {

    @GetMapping("/search")
    public ResponseEntity<?> searchFaculties(
            @RequestParam String query,
            @RequestParam int page,
            @RequestParam int size
    ) {
        // Implement search logic here
        return ResponseEntity.ok().body("Search faculties endpoint");
    }
}
