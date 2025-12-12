package com.sajid.college_app.services;

import com.sajid.college_app.dtos.BranchResponse;
import com.sajid.college_app.dtos.ClassIdAndSection;
import com.sajid.college_app.dtos.SimpleBranchResponse;
import com.sajid.college_app.exceptions.ResourceNotFoundException;
import com.sajid.college_app.helpers.AutoMapper;
import com.sajid.college_app.repositories.ClassRepository;
import com.sajid.college_app.services.keys.BranchKey;
import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.Scheme;
import com.sajid.college_app.models.SimpleBranch;
import com.sajid.college_app.models.raw.RawStudent;
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

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final SchemeRepository schemeRepository;
    private final SimpleBranchRepository simpleBranchRepository;
    private final ClassRepository classRepository;
    private final AutoMapper autoMapper;

    @Transactional
    public Map<BranchKey, Branch> bulkSaveBranches(List<RawStudent> rawStudents){
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

    public List<SimpleBranchResponse> getDistinctBranches() {
        List<SimpleBranch> simpleBranches = simpleBranchRepository.findAll();
        return simpleBranches.stream()
                .map(autoMapper::mapSimpleBranchToSimpleBranchResponse)
                .collect(toList());
    }

    public List<Integer> getDistinctSemestersByBranchCode(int branchCode) {
        List<Integer> semesters = classRepository.findDistinctSemestersBySimpleBranch_BranchCode(branchCode);
        if (semesters.isEmpty()) {
            return List.of();
        }
        return semesters;
    }

    public List<ClassIdAndSection> getDistinctSectionsByBranchCodeAndSemester(int branchCode, int semester) {
        List<Object[]> classIdsAndSections = classRepository.findClassIdsAndSectionsByBranchCodeAndSemester(branchCode, semester);
        if (classIdsAndSections.isEmpty()) {
            return List.of();
        }
        return classIdsAndSections.stream()
                .map(objArr -> new ClassIdAndSection((Integer) objArr[0], (String) objArr[1]))
                .toList();
    }
}
