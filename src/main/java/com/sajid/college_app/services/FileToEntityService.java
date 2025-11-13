package com.sajid.college_app.services;

import com.opencsv.exceptions.CsvValidationException;
import com.sajid.college_app.models.Class;
import com.sajid.college_app.services.keys.BranchKey;
import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.raw.Student;
import com.sajid.college_app.services.keys.ClassKey;
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

    public void bulkUploadCourses(MultipartFile file) throws CsvValidationException {
        List<Student> rawStudents = csvProcessingService.process(file, Student.class);
        Map<BranchKey, Branch> branchMap = branchService.bulkSaveBranches(rawStudents);
        Map<ClassKey, Class> classMap = classService.bulkSaveClasses(rawStudents, branchMap);
    }
}
