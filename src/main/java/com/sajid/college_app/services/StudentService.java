package com.sajid.college_app.services;

import com.sajid.college_app.dtos.StudentResponse;
import com.sajid.college_app.helpers.StudentMapper;
import com.sajid.college_app.models.CollegeClass;
import com.sajid.college_app.models.Student;
import com.sajid.college_app.models.raw.RawStudent;
import com.sajid.college_app.repositories.StudentRepository;
import com.sajid.college_app.services.keys.BranchKey;
import com.sajid.college_app.services.keys.ClassKey;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public void bulkSaveStudents(List<RawStudent> rawStudents, Map<ClassKey, CollegeClass> classMap){
        Map<String, Student> studentMap = studentRepository.findAll().stream()
                .collect(Collectors.toMap(Student::getRollNumber, Function.identity()));

        List<Student> newStudents = rawStudents.stream()
                .filter(rs -> !studentMap.containsKey(rs.getRoll()))
                .map(rs -> {
                    Student student = new Student();
                    student.setRollNumber(rs.getRoll());
                    student.setName(rs.getName());

                    ClassKey ckey = new ClassKey(
                            new BranchKey(rs.getScheme(), Integer.parseInt(rs.getBranch().substring(0, 1))),
                            rs.getSem(),
                            rs.getSection()
                    );

                    student.setCollegeClass(classMap.get(ckey));

                    return student;
                })
                .toList();

        studentRepository.saveAll(newStudents);
    }

    public List<Student> getStudentsByRollNumberLike(String rollNumber){
        return studentRepository.findByRollNumberContaining(rollNumber);
    }

    public List<StudentResponse> getAllStudents(){
        return studentRepository.findAll().stream().map(studentMapper::mapStudentToStudentResponse).toList();
    }
}
