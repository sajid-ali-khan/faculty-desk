package com.sajid.college_app.helpers;

import com.sajid.college_app.dtos.*;
import com.sajid.college_app.models.*;
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

    @Mapping(target = "branchName", source = "shortForm")
    @Mapping(target = "abbreviation", source = "fullForm")
    SimpleBranchResponse mapSimpleBranchToSimpleBranchResponse(SimpleBranch simpleBranch);

    @Mapping(target = "branchName", source = "branch.simpleBranch.shortForm")
    @Mapping(target = "branchAbbreviation", source = "branch.simpleBranch.fullForm")
    @Mapping(target = "subjectAssignments", source = "classSubjects")
    ClassAssignmentResponse mapClassToClassSubjectResponse(CollegeClass collegeClass);

    @Mapping(target = "subjectName", source = "classSubject.subject.shortForm")
    @Mapping(target = "subjectAbbreviation", source = "classSubject.subject.fullForm")
    @Mapping(target = "subjectType", source = "classSubject.subject.subjectType")
    @Mapping(target = "facultyId", source = "faculty.id")
    @Mapping(target = "facultyName", source = "faculty.name")
    @Mapping(target = "facultyCode", source = "faculty.facultyCode")
    SubjectAssignment mapClassSubjectToSubjectAssignment(ClassSubject classSubject, Faculty faculty);
}
