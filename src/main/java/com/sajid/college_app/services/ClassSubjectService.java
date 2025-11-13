package com.sajid.college_app.services;

import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.Class;
import com.sajid.college_app.models.ClassSubject;
import com.sajid.college_app.models.Subject;
import com.sajid.college_app.models.raw.Course;
import com.sajid.college_app.repositories.BranchRepository;
import com.sajid.college_app.repositories.ClassRepository;
import com.sajid.college_app.repositories.ClassSubjectRepository;
import com.sajid.college_app.services.keys.BranchKey;
import com.sajid.college_app.services.keys.ClassSubjectKey;
import com.sajid.college_app.services.keys.SubjectKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ClassSubjectService {
    private final ClassSubjectRepository classSubjectRepository;
    private final ClassRepository classRepository;
    private final BranchRepository branchRepository;

    public void bulkSaveClassSubjects(List<Course> rawCourses, Map<SubjectKey, Subject> subjectMap){
        Map<BranchKey, Branch> branchMap = branchRepository.findAll().stream()
                .collect(Collectors.toMap(b -> new BranchKey(b.getScheme().getSchemeCode(), b.getSimpleBranch().getBranchCode()), Function.identity()));

        Set<ClassSubjectKey> classSubjectMap = classSubjectRepository.findAll().stream()
                .map(cs -> new ClassSubjectKey(
                        new BranchKey(cs.get_class().getBranch().getScheme().getSchemeCode(),
                                cs.get_class().getBranch().getSimpleBranch().getBranchCode()),
                        cs.get_class().getSemester(),
                        new SubjectKey(cs.getSubject().getShortForm(), cs.getSubject().getFullForm())
                ))
                .collect(Collectors.toSet());

        List<ClassSubject> newClassSubjects = rawCourses.stream()
                .map(rc -> new ClassSubjectKey(
                        new BranchKey(
                                rc.getScheme(),
                                Integer.parseInt(rc.getBranch().substring(0, 1))
                        ),
                        rc.getSem(),
                        new SubjectKey(
                                rc.getSCode(),
                                rc.getSubName()
                        )
                ))
                .distinct()
                .filter(key -> !classSubjectMap.contains(key))
                .filter(key -> branchMap.containsKey(key.branchKey()) && subjectMap.containsKey(key.subjectKey()))
                .flatMap(key -> {
                    Branch branch = branchMap.get(key.branchKey());
                    List<Class> classes = classRepository.findByBranchAndSemester(branch, key.semester());
                    Subject subject = subjectMap.get(key.subjectKey());

                    return classes.stream()
                            .map(c -> {
                                ClassSubject classSubject = new ClassSubject();
                                classSubject.set_class(c);
                                classSubject.setSubject(subject);
                                return classSubject;
                            });
                })
                .toList();

        classSubjectRepository.saveAll(newClassSubjects);
    }
}
