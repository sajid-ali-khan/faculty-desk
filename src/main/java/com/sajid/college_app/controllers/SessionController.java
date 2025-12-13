package com.sajid.college_app.controllers;

import com.sajid.college_app.services.SessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@AllArgsConstructor
@Slf4j
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

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<?> deleteSession(
            @PathVariable("sessionId") long sessionId
    ){
        sessionService.deleteSessionById(sessionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<?> updateSessionStatus(
            @PathVariable("sessionId") long sessionId,
            @RequestBody Map<Long, Boolean> attendanceUpdates
    ){
        log.info("Updating session {} with attendance updates: {}", sessionId, attendanceUpdates);
        sessionService.updateSessionStatus(sessionId, attendanceUpdates);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getDetailedSession(
            @PathVariable("sessionId") long sessionId
    ){
        var detailedSession = sessionService.getDetailedSessionById(sessionId);
        return ResponseEntity.ok(detailedSession);
    }

}
