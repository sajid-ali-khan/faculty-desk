package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, Integer> {
}
