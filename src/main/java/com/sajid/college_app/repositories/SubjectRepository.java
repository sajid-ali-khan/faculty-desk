package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    boolean existsByShortFormAndFullForm(String shortForm, String fullForm);
}
