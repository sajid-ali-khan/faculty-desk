package com.sajid.college_app.services;

import com.sajid.college_app.dtos.DetailedSessionResponse;
import com.sajid.college_app.dtos.SessionResponse;
import com.sajid.college_app.exceptions.ResourceNotFoundException;
import com.sajid.college_app.helpers.AutoMapper;
import com.sajid.college_app.models.AttendanceRecord;
import com.sajid.college_app.models.ClassSubject;
import com.sajid.college_app.models.Session;
import com.sajid.college_app.models.Student;
import com.sajid.college_app.repositories.AttendanceRecordRepository;
import com.sajid.college_app.repositories.ClassSubjectRepository;
import com.sajid.college_app.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final ClassSubjectRepository classSubjectRepository;
    private final AutoMapper autoMapper;
    private final ZoneId zoneId = ZoneId.of("Asia/Kolkata");
    private final AttendanceRecordRepository attendanceRecordRepository;

    public List<SessionResponse> getSessionsByClassSubjectIdAndDate(long classSubjectId, LocalDate date){
        ClassSubject classSubject = classSubjectRepository.findById(classSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Class subject with id = " + classSubjectId + " not found."));

        Instant startTime = date.atStartOfDay(zoneId).toInstant();
        Instant endTime = date.plusDays(1).atStartOfDay(zoneId).toInstant();

        return sessionRepository.findByClassSubjectAndCreatedAtBetween(classSubject, startTime, endTime).stream()
                .map(autoMapper::mapSessionToSessionResponse)
                .toList();
    }

    @Transactional
    public DetailedSessionResponse createNewServiceByClassSubjectId(long classSubjectId) {
//        create a session
        ClassSubject classSubject = classSubjectRepository.findById(classSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Class subject with id = " + classSubjectId + " not found."));
        Session session = new Session();
        session.setClassSubject(classSubject);
        session = sessionRepository.save(session);
//        fetch all students of that class
        List<Student> students = classSubject.getCollegeClass().getStudents();
//        create attendance records using standard for loop

        for (Student student : students) {
            AttendanceRecord attendanceRecord = new AttendanceRecord();
            attendanceRecord.setSession(session);
            attendanceRecord.setStudent(student);
            attendanceRecord.setPresent(false); // default to absent
            session.getAttendanceRecords().add(attendanceRecord);
        }
        attendanceRecordRepository.saveAll(session.getAttendanceRecords());
//        create the dto and return
        return autoMapper.mapSessionToDetailedSessionResponse(session);
    }

    public void deleteSessionById(long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with id = " + sessionId + " not found."));
        sessionRepository.delete(session);
    }

    public void updateSessionStatus(long sessionId, Map<Long, Boolean> attendanceUpdates) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with id = " + sessionId + " not found."));

        List<AttendanceRecord> attendanceRecords = session.getAttendanceRecords();
        for (int i = 0; i < attendanceRecords.size(); i++) {
            long attendanceRecordId = attendanceRecords.get(i).getId();
            Boolean present = attendanceUpdates.get(attendanceRecordId);
            attendanceRecords.get(i).setPresent(present);
        }
        session.setAttendanceRecords(attendanceRecords);
        sessionRepository.save(session);
    }
}
