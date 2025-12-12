package com.sajid.college_app.services;

import com.sajid.college_app.dtos.ClassAssignmentResponse;
import com.sajid.college_app.exceptions.ResourceNotFoundException;
import com.sajid.college_app.helpers.AutoMapper;
import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.CollegeClass;
import com.sajid.college_app.models.raw.RawStudent;
import com.sajid.college_app.repositories.ClassRepository;
import com.sajid.college_app.services.keys.BranchKey;
import com.sajid.college_app.services.keys.ClassKey;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;
    private final AutoMapper autoMapper;

    @Transactional
    public Map<ClassKey, CollegeClass> bulkSaveClasses(List<RawStudent> rawStudents, Map<BranchKey, Branch> branchMap) {
        Map<ClassKey, CollegeClass> classMap = classRepository.findAll().stream().collect(Collectors.toMap(
                c -> new ClassKey(
                        new BranchKey(
                                c.getBranch().getScheme().getSchemeCode(),
                                c.getBranch().getSimpleBranch().getBranchCode()
                        ),
                        c.getSemester(),
                        c.getSection()),
                Function.identity()
        ));

        List<CollegeClass> newCollegeClasses = rawStudents.stream()
                .map(rs -> new ClassKey(
                        new BranchKey(
                                rs.getScheme(),
                                Integer.parseInt(rs.getBranch().substring(0, 1))
                        ),
                        rs.getSem(),
                        rs.getSection()
                ))
                .distinct()
                .filter(key -> !classMap.containsKey(key) && branchMap.containsKey(key.branchKey()))
                .map(key -> {
                    CollegeClass collegeClass = new CollegeClass();
                    collegeClass.setBranch(branchMap.get(key.branchKey()));
                    collegeClass.setSemester(key.semester());
                    collegeClass.setSection(key.section());
                    classMap.put(key, collegeClass);
                    return collegeClass;
                })
                .toList();

        classRepository.saveAll(newCollegeClasses);
        return classMap;
    }


    public ClassAssignmentResponse getAssignmentsByClassId(int classId) {
        CollegeClass collegeClass = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class with id " + classId + " not found"));

        return autoMapper.mapClassToClassAssignmentResponse(collegeClass);
    }
}
