package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Integer> {
    List<Class> findByBranchAndSemester(Branch branch, int semester);
}
