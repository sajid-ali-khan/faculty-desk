package com.sajid.college_app.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String shortForm;
    private String fullForm;

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;
}
