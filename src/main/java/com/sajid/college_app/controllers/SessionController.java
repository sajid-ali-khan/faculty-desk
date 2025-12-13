package com.sajid.college_app.controllers;

import com.sajid.college_app.dtos.SessionResponse;
import com.sajid.college_app.services.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@AllArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    public List<SessionResponse> getSessionsByClassSubjectIdAndDate(
            @RequestParam(name = "classSubjectId")long classSubjectId,
            @RequestParam(name = "date") LocalDate date
            ){
        return sessionService.getSessionsByClassSubjectIdAndDate(classSubjectId, date);
    }

}
