package com.sajid.college_app.services;

import com.sajid.college_app.dtos.ClassAssignmentResponse;
import com.sajid.college_app.dtos.StudentReport;
import com.sajid.college_app.dtos.SubjectReport;
import com.sajid.college_app.dtos.UpdateClassAssignmentsRequest;
import com.sajid.college_app.exceptions.ResourceNotFoundException;
import com.sajid.college_app.helpers.AutoMapper;
import com.sajid.college_app.models.*;
import com.sajid.college_app.models.raw.RawStudent;
import com.sajid.college_app.repositories.ClassRepository;
import com.sajid.college_app.repositories.ClassSubjectRepository;
import com.sajid.college_app.repositories.FacultyRepository;
import com.sajid.college_app.services.keys.BranchKey;
import com.sajid.college_app.services.keys.ClassKey;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;
    private final AutoMapper autoMapper;
    private final FacultyRepository facultyRepository;
    private final ClassSubjectRepository classSubjectRepository;

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


    @Transactional
    public void updateAssignmentsByClassId(Integer classId, List<UpdateClassAssignmentsRequest> request) {
        List<ClassSubject> classSubjects = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class with id " + classId + " not found"))
                .getClassSubjects();

        Map<Long, ClassSubject> classSubjectMap = classSubjects.stream()
                .collect(Collectors.toMap(ClassSubject::getId, Function.identity()));

        List<ClassSubject> updatedClassSubjects =  new ArrayList<>();
        for (UpdateClassAssignmentsRequest req : request) {
            if (classSubjectMap.containsKey(req.classSubjectId())) {
                ClassSubject classSubject = classSubjectMap.get(req.classSubjectId());
                classSubject.setFaculty(facultyRepository.findById(req.facultyId())
                        .orElseThrow(() -> new ResourceNotFoundException("Faculty with id " + req.facultyId() + " not found")));

                updatedClassSubjects.add(classSubject);
            } else {
                throw new ResourceNotFoundException("ClassSubject with id " + req.classSubjectId() + " not found in class " + classId);
            }
        }

        classSubjectRepository.saveAll(updatedClassSubjects);
    }

    public Map<Integer, StudentReport> getClassReport(Integer classId) {
        CollegeClass collegeClass = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class with id " + classId + " not found"));

        Map<Integer, StudentReport> reportMap = collegeClass.getStudents().stream()
                .collect(Collectors.toMap(
                        Student::getId,
                        student -> new StudentReport(
                                student.getId(),
                                student.getRollNumber(),
                                student.getName(),
                                0.0,
                                new HashMap<>()
                        )
                ));

        for (ClassSubject classSubject: collegeClass.getClassSubjects()){
            int subjectId = classSubject.getSubject().getId();
            String subjectName = classSubject.getSubject().getShortForm();
            for (Session session: classSubject.getSessionList()){
                for (AttendanceRecord att: session.getAttendanceRecords()){
                    int studentId =att.getStudent().getId();
                    if (! reportMap.get(studentId).subjectsReports().containsKey(subjectId)){
                        SubjectReport subjectReport = new SubjectReport(subjectId, subjectName);
                        reportMap.get(studentId).subjectsReports().put(subjectId, subjectReport);
                    }
                    reportMap.get(studentId).subjectsReports().get(subjectId).totalCount += 1;
                    if (att.isPresent()) {
                        reportMap.get(studentId).subjectsReports().get(subjectId).presentCount += 1;
                    }
                }
            }
        }

        for (StudentReport studentReport: reportMap.values()){
            double totalPercentage = 0.0;
            int subjectCount = studentReport.subjectsReports().size();
            for (SubjectReport subjectReport: studentReport.subjectsReports().values()){
                if (subjectReport.totalCount > 0){
                    subjectReport.attendancePercentage = (subjectReport.presentCount * 100.0) / subjectReport.totalCount;
                } else {
                    subjectReport.attendancePercentage = 0.0;
                }
                totalPercentage += subjectReport.attendancePercentage;
            }
            if (subjectCount > 0){
                double overallPercentage = totalPercentage / subjectCount;
                // Update the overall percentage in the record
                StudentReport updatedReport = new StudentReport(
                        studentReport.studentId(),
                        studentReport.studentRoll(),
                        studentReport.studentName(),
                        overallPercentage,
                        studentReport.subjectsReports()
                );
                reportMap.put(studentReport.studentId(), updatedReport);
            }
        }

        return reportMap;
    }
}
