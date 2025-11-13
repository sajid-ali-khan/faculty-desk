package com.sajid.college_app.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "simple_branches")
public class SimpleBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String branchCode;

    private String shortForm;
    private String fullForm;
}
