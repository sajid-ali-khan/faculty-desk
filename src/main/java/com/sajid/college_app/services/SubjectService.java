package com.sajid.college_app.services;

import com.sajid.college_app.models.Subject;
import com.sajid.college_app.models.SubjectType;
import com.sajid.college_app.models.raw.Course;
import com.sajid.college_app.repositories.SubjectRepository;
import com.sajid.college_app.services.keys.SubjectKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public Map<SubjectKey, Subject> bulkSaveSubjects(List<Course> rawCourses){
        Map<SubjectKey, Subject> subjectMap = subjectRepository.findAll().stream()
                .collect(Collectors.toMap(sub -> new SubjectKey(sub.getShortForm(), sub.getFullForm()), Function.identity()));

        List<Subject> newSubjects = rawCourses.stream()
                .map(rc -> new SubjectKey(rc.getSCode(), rc.getSubName()))
                .distinct()
                .filter(key -> !subjectMap.containsKey(key))
                .map(key -> {
                    Subject subject = new Subject();
                    subject.setShortForm(key.shortForm());
                    subject.setFullForm(key.fullForm());
                    subject.setSubjectType((key.shortForm().endsWith("(P)")) ? SubjectType.LAB: SubjectType.THEORY);

                    subjectMap.put(key, subject);
                    return subject;
                })
                .toList();

        subjectRepository.saveAll(newSubjects);
        return subjectMap;
    }
}
