package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsByRollNumber(String rollNumber);
}
