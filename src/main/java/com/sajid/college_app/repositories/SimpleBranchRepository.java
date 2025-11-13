package com.sajid.college_app.repositories;

import com.sajid.college_app.models.SimpleBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleBranchRepository extends JpaRepository<SimpleBranch, Integer> {
    boolean existsByBranchCode(int branchCode);
}
