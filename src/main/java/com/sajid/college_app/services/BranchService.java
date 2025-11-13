package com.sajid.college_app.services;

import com.sajid.college_app.repositories.BranchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
}
