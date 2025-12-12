package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    boolean existsByShortFormAndFullForm(String shortForm, String fullForm);
}
