package com.sajid.college_app.services;

import com.sajid.college_app.dtos.keys.BranchKey;
import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.Scheme;
import com.sajid.college_app.models.SimpleBranch;
import com.sajid.college_app.models.raw.Course;
import com.sajid.college_app.models.raw.Student;
import com.sajid.college_app.repositories.BranchRepository;
import com.sajid.college_app.repositories.SchemeRepository;
import com.sajid.college_app.repositories.SimpleBranchRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final SchemeRepository schemeRepository;
    private final SimpleBranchRepository simpleBranchRepository;

    @Transactional
    public Map<BranchKey, Branch> bulkSaveBranches(List<Student> rawStudents){
        Map<String, Scheme> schemeMap = schemeRepository.findAll().stream().collect(Collectors.toMap(Scheme::getSchemeCode, Function.identity()));
        Map<Integer, SimpleBranch> simpleBranchMap = simpleBranchRepository.findAll().stream().collect(Collectors.toMap(SimpleBranch::getBranchCode, Function.identity()));

        Map<BranchKey, Branch> branchMap = branchRepository.findAll().stream().collect(Collectors.toMap(b -> new BranchKey(
                b.getScheme().getSchemeCode(),
                b.getSimpleBranch().getBranchCode()
        ), Function.identity()));

        List<Branch> newBranches = rawStudents.stream()
                .map(rc -> new BranchKey(
                        rc.getScheme(),
                        Integer.parseInt(rc.getBranch().substring(0, 1))
                ))
                .distinct()
                .filter(key -> !branchMap.containsKey(key) && schemeMap.containsKey(key.schemeCode()) && simpleBranchMap.containsKey(key.branchCode()))
                .map(key -> {
                    Branch branch = new Branch();
                    branch.setScheme(schemeMap.get(key.schemeCode()));
                    branch.setSimpleBranch(simpleBranchMap.get(key.branchCode()));
                    branchMap.put(key, branch);
                    return branch;
                })
                .toList();

        branchRepository.saveAll(newBranches);
        return branchMap;
    }
}
