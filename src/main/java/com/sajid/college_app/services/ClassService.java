package com.sajid.college_app.services;

import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.Class;
import com.sajid.college_app.models.raw.Student;
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

    @Transactional
    public Map<ClassKey, Class> bulkSaveClasses(List<Student> rawStudents, Map<BranchKey, Branch> branchMap) {
        Map<ClassKey, Class> classMap = classRepository.findAll().stream().collect(Collectors.toMap(
                c -> new ClassKey(
                        new BranchKey(
                                c.getBranch().getScheme().getSchemeCode(),
                                c.getBranch().getSimpleBranch().getBranchCode()
                        ),
                        c.getSemester(),
                        c.getSection()),
                Function.identity()
        ));

        List<Class> newClasses = rawStudents.stream()
                .map(rs -> new ClassKey(
                        new BranchKey(
                                rs.getScheme(),
                                Integer.parseInt(rs.getBranch().substring(0, 1))
                        ),
                        rs.getSem(),
                        rs.getSection()
                ))
                .filter(key -> !classMap.containsKey(key) && branchMap.containsKey(key.branchKey()))
                .map(key -> {
                    Class _class = new Class();
                    _class.setBranch(branchMap.get(key.branchKey()));
                    _class.setSemester(key.semester());
                    _class.setSection(key.section());
                    classMap.put(key, _class);
                    return _class;
                })
                .toList();

        classRepository.saveAll(newClasses);
        return classMap;
    }
}
