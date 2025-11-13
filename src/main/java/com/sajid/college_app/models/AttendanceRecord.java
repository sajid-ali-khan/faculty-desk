package com.sajid.college_app.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "attendance_records",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id", "session_id"})})
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Student student;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Session session;

    private boolean present;
}
