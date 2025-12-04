package com.sajid.college_app.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "class_subjects",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"college_class_id", "subject_id", "faculty_id"}),
        @UniqueConstraint(columnNames = {"college_class_id", "subject_id"}),
        })
public class ClassSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "college_class_id")
    private CollegeClass collegeClass;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Faculty faculty;

    @OneToMany(mappedBy = "classSubject", cascade = CascadeType.REMOVE)
    List<Session> sessionList = new ArrayList<>();
}
