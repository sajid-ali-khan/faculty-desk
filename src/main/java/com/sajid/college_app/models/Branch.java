package com.sajid.college_app.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "branches", uniqueConstraints = {@UniqueConstraint(columnNames = {"scheme_id", "simple_branch_id"})})
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "simple_branch_id")
    private SimpleBranch simpleBranch;
}
