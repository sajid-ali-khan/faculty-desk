package com.sajid.college_app.services;

import com.sajid.college_app.dtos.SessionResponse;
import com.sajid.college_app.exceptions.ResourceNotFoundException;
import com.sajid.college_app.helpers.AutoMapper;
import com.sajid.college_app.models.ClassSubject;
import com.sajid.college_app.repositories.ClassSubjectRepository;
import com.sajid.college_app.repositories.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final ClassSubjectRepository classSubjectRepository;
    private final AutoMapper autoMapper;
    private final ZoneId zoneId = ZoneId.of("Asia/Kolkata");

    public List<SessionResponse> getSessionsByClassSubjectIdAndDate(long classSubjectId, LocalDate date){
        ClassSubject classSubject = classSubjectRepository.findById(classSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Class subject with id = " + classSubjectId + " not found."));

        Instant startTime = date.atStartOfDay(zoneId).toInstant();
        Instant endTime = date.plusDays(1).atStartOfDay(zoneId).toInstant();

        return sessionRepository.findByClassSubjectAndCreatedAtBetween(classSubject, startTime, endTime).stream()
                .map(autoMapper::mapSessionToSessionResponse)
                .toList();
    }
}
