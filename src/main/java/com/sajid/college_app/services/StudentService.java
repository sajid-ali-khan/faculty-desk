package com.sajid.college_app.services;

import com.sajid.college_app.models.Class;
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

    public void bulkSaveStudents(List<RawStudent> rawStudents, Map<ClassKey, Class> classMap){
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

                    student.set_class(classMap.get(ckey));

                    return student;
                })
                .toList();

        studentRepository.saveAll(newStudents);
    }
}
