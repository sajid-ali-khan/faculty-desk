package com.sajid.college_app.repositories;

import com.sajid.college_app.models.ClassSubject;
import com.sajid.college_app.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByClassSubjectAndCreatedAtBetween(ClassSubject classSubject, Instant start, Instant end);
}
