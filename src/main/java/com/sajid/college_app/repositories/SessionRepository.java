package com.sajid.college_app.repositories;

import com.sajid.college_app.models.ClassSubject;
import com.sajid.college_app.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByClassSubjectAndCreatedAtBetween(ClassSubject classSubject, Instant start, Instant end);

    @Query("SELECT COUNT(s) FROM Session s WHERE s.classSubject.id = :classSubjectId")
    int countSessionsByClassSubjectId(@Param("classSubjectId") long classSubjectId);

    @Query("SELECT SUM(s.presentCount) FROM Session s WHERE s.classSubject.id = :classSubjectId")
    Integer sumPresentCountByClassSubjectId(@Param("classSubjectId") long classSubjectId);

    @Query("SELECT SUM(s.totalCount) FROM Session s WHERE s.classSubject.id = :classSubjectId")
    Integer sumTotalCountByClassSubjectId(@Param("classSubjectId") long classSubjectId);

    @Query("SELECT COUNT(s) > 0 FROM Session s WHERE s.classSubject.id = :classSubjectId AND CAST(s.createdAt AS date) = CAST(:date AS date)")
    boolean existsSessionCreatedToday(@Param("classSubjectId") long classSubjectId, @Param("date") Instant date);

    List<Session> findByClassSubjectId(long classSubjectId);
}
