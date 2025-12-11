package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsByRollNumber(String rollNumber);

    @Query("SELECT s FROM Student s WHERE s.name LIKE upper(concat('%',:name,'%'))")
    Page<Student> findByNamePattern(@Param("name")String name, Pageable pageable);
}