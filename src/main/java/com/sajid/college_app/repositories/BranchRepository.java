package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    List<Branch> findByScheme(Scheme scheme);
}
