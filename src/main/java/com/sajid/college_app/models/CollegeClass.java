package com.sajid.college_app.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "college_classes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"branch_id", "semester", "section"})})
public class CollegeClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private int semester;

    @Column(nullable = false)
    private String section;

    @OneToMany(mappedBy = "collegeClass", cascade = CascadeType.REMOVE)
    List<ClassSubject> classSubjects = new ArrayList<>();

    @OneToMany(mappedBy = "collegeClass")
    List<Student> students;
}
