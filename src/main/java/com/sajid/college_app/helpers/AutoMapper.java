package com.sajid.college_app.helpers;

import com.sajid.college_app.dtos.BranchResponse;
import com.sajid.college_app.dtos.StudentResponse;
import com.sajid.college_app.dtos.SubjectResponse;
import com.sajid.college_app.models.Branch;
import com.sajid.college_app.models.Student;
import com.sajid.college_app.models.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoMapper {
    @Mapping(source="id", target="id")
    StudentResponse mapStudentToStudentResponse(Student student);

    @Mapping(target = "branchName", source = "simpleBranch.shortForm")
    @Mapping(target = "abbreviation", source = "simpleBranch.fullForm")
    @Mapping(target = "branchCode", source = "simpleBranch.branchCode")
    @Mapping(target = "scheme", source = "scheme.schemeCode")
    BranchResponse mapBranchToBranchResponse(Branch branch);

    @Mapping(target = "subjectName", source = "shortForm")
    @Mapping(target = "abbreviation", source = "fullForm")
    @Mapping(target = "subjectType", source = "subjectType")
    SubjectResponse mapSubjectToSubjectResponse(Subject subject);
}
