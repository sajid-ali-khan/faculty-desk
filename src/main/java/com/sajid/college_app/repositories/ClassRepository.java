package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.CollegeClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassRepository extends JpaRepository<CollegeClass, Integer> {
    List<CollegeClass> findByBranchAndSemester(Branch branch, int semester);

    @Query("""
            select distinct c.semester from CollegeClass c
            where c.branch.simpleBranch.branchCode = :branchCode
            """)
    List<Integer> findDistinctSemestersBySimpleBranch_BranchCode(@Param("branchCode") int branchCode);

    @Query("""
            select c.id, c.section from CollegeClass c
            where c.branch.simpleBranch.branchCode = :branchCode and c.semester = :semester
            """)
    List<Object[]> findClassIdsAndSectionsByBranchCodeAndSemester(@Param("branchCode") int branchCode, @Param("semester") int semester);
}
