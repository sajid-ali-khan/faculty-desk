package com.sajid.college_app.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "schemes")
public class Scheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String schemeCode;
}
