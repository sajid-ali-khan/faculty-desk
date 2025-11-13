package com.sajid.college_app.services;

import com.opencsv.exceptions.CsvValidationException;
import com.sajid.college_app.dtos.keys.BranchKey;
import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.raw.Course;
import com.sajid.college_app.models.raw.Student;
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

    public void bulkUploadCourses(MultipartFile file) throws CsvValidationException {
        List<Student> rawStudents = csvProcessingService.process(file, Student.class);
        Map<BranchKey, Branch> branchMap = branchService.bulkSaveBranches(rawStudents);
    }
}
