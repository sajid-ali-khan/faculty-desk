package com.sajid.college_app.services;

import com.opencsv.exceptions.CsvValidationException;
import com.sajid.college_app.models.Class;
import com.sajid.college_app.models.Subject;
import com.sajid.college_app.models.raw.Course;
import com.sajid.college_app.services.keys.BranchKey;
import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.raw.RawStudent;
import com.sajid.college_app.services.keys.ClassKey;
import com.sajid.college_app.services.keys.SubjectKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class FileToEntityService {
    private final CsvProcessingService csvProcessingService;
    private final BranchService branchService;
    private final ClassService classService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    public void bulkUploadStudents(MultipartFile file) throws CsvValidationException {
        List<RawStudent> rawStudents = csvProcessingService.process(file, RawStudent.class);
        Map<BranchKey, Branch> branchMap = branchService.bulkSaveBranches(rawStudents);
        Map<ClassKey, Class> classMap = classService.bulkSaveClasses(rawStudents, branchMap);
        studentService.bulkSaveStudents(rawStudents, classMap);
    }

    public void bulkUploadCourses(MultipartFile file) throws CsvValidationException {
        List<Course> rawCourses = csvProcessingService.process(file, Course.class);
        Map<SubjectKey, Subject> subjectMap = subjectService.bulkSaveSubjects(rawCourses);
        log.info("Subjects saved successfully.");
    }
}
