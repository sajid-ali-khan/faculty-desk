package com.sajid.college_app.helpers;

import com.sajid.college_app.dtos.StudentResponse;
import com.sajid.college_app.models.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(source="id", target="id")
    StudentResponse mapStudentToStudentResponse(Student student);
}
