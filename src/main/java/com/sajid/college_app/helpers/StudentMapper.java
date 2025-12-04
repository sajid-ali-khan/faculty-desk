package com.sajid.college_app.helpers;

import com.sajid.college_app.dtos.StudentResponse;
import com.sajid.college_app.models.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponse mapStudentToStudentResponse(Student student);
}
