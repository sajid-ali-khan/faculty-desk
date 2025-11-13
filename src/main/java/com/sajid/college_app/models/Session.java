package com.sajid.college_app.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "class_subject_id")
    private ClassSubject classSubject;

    @Column(nullable = false)
    private int presentCount;

    @Column(nullable = false)
    private int totalCount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "session")
    List<AttendanceRecord> attendanceRecords;


    @PrePersist
    private void onCreate(){
        this.createdAt = Instant.now();
    }

    @PreUpdate
    private void onUpdate(){
        this.updatedAt = Instant.now();
    }
}
