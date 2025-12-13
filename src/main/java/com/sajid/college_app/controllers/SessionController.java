package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.DetailedSessionResponse;
import com.sajid.college_app.dtos.SessionResponse;
import com.sajid.college_app.services.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@AllArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<?> getSessionsByClassSubjectIdAndDate(
            @RequestParam(name = "classSubjectId")long classSubjectId,
            @RequestParam(name = "date") LocalDate date
            ){
        var sessions = sessionService.getSessionsByClassSubjectIdAndDate(classSubjectId, date);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping
    public ResponseEntity<?> createNewSession(
            @RequestParam long classSubjectId
    ){
        var detailedSession = sessionService.createNewServiceByClassSubjectId(classSubjectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(detailedSession);
    }

}
