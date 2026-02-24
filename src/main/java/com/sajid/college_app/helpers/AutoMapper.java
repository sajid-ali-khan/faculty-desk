package com.sajid.college_app.helpers;

import com.sajid.college_app.dtos.*;
import com.sajid.college_app.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface AutoMapper {
    @Mapping(source="id", target="id")
    StudentResponse mapStudentToStudentResponse(Student student);

    @Mapping(target = "branchName", source = "shortForm")
    @Mapping(target = "abbreviation", source = "fullForm")
    SimpleBranchResponse mapSimpleBranchToSimpleBranchResponse(SimpleBranch simpleBranch);

    @Mapping(target = "branchName", source = "branch.simpleBranch.shortForm")
    @Mapping(target = "branchAbbreviation", source = "branch.simpleBranch.fullForm")
    @Mapping(target = "subjectAssignments", source = "classSubjects")
    ClassAssignmentResponse mapClassToClassAssignmentResponse(CollegeClass collegeClass);

    @Mapping(target = "subjectId", source = "subject.id")
    @Mapping(target = "subjectAbbreviation", source = "subject.shortForm")
    @Mapping(target = "subjectName", source = "subject.fullForm")
    @Mapping(target = "subjectType", source = "subject.subjectType")
    @Mapping(target = "facultyId", source = "faculty.id")
    @Mapping(target = "facultyName", source = "faculty.name")
    @Mapping(target = "facultyCode", source = "faculty.facultyCode")
    SubjectAssignment mapClassSubjectToSubjectAssignment(ClassSubject classSubject);

    @Mapping(target = "facultyName", source = "name")
    FacultyResponse mapFacultyToFacultyResponse(Faculty faculty);

    @Mapping(target = "branchName", source = "collegeClass.branch.simpleBranch.fullForm")
    @Mapping(target = "branchAbbreviation", source = "collegeClass.branch.simpleBranch.shortForm")
    @Mapping(target = "subjectName", source = "subject.shortForm")
    @Mapping(target = "subjectAbbreviation", source = "subject.fullForm")
    @Mapping(target = "semester", source = "collegeClass.semester")
    @Mapping(target = "section", source = "collegeClass.section")
    FacultyAssignmentResponse mapClassSubjectToFacultyAssignmentResponse(ClassSubject classSubject);


    SessionResponse mapSessionToSessionResponse(Session session);

    default LocalDateTime instantToLocalDate(Instant instant) {
        return instant == null
                ? null
                : instant.atZone(ZoneId.of("Asia/Kolkata")).toLocalDateTime();
    }

    @Mapping(target = "studentRoll", source = "student.rollNumber")
    @Mapping(target = "studentName", source = "student.name")
    AttendanceRecordResponse mapAttendanceRecordToAttendanceRecordResponse(AttendanceRecord attendanceRecord);

    default Map<Long, AttendanceRecordResponse> mapAttendanceRecordListToMap(List<AttendanceRecord> attendanceRecords){
        return attendanceRecords.stream()
                .map(this::mapAttendanceRecordToAttendanceRecordResponse)
                .collect(Collectors.toMap(
                        AttendanceRecordResponse::id, Function.identity()
                ));
    }

    @Mapping(target = "attendanceRecordMap", source = "attendanceRecords")
    DetailedSessionResponse mapSessionToDetailedSessionResponse(Session session);
}
