package com.sajid.college_app.services;

import com.sajid.college_app.dtos.FacultyAssignmentResponse;
import com.sajid.college_app.dtos.FacultyResponse;
import com.sajid.college_app.exceptions.ResourceNotFoundException;
import com.sajid.college_app.helpers.AutoMapper;
import com.sajid.college_app.models.ClassSubject;
import com.sajid.college_app.models.Faculty;
import com.sajid.college_app.models.raw.Employee;
import com.sajid.college_app.repositories.FacultyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AutoMapper autoMapper;

    @Transactional
    public void bulkSaveFaculty(List<Employee> rawEmployees){
        Set<String> incomingIds = rawEmployees.stream()
                .map(Employee::getEmpId)
                .collect(Collectors.toSet());


        Set<String> existingCodes = facultyRepository.findByFacultyCodeIn(incomingIds)
                .stream()
                .map(Faculty::getFacultyCode)
                .collect(Collectors.toSet());

        List<Faculty> newFaculties = rawEmployees.parallelStream() // <--- KEY CHANGE
                .filter(re -> !existingCodes.contains(re.getEmpId()))
                .map(re -> {
                    Faculty faculty = new Faculty();
                    faculty.setFacultyCode(re.getEmpId());
                    faculty.setName(re.getName());
                    // This is the heavy operation, now distributed across CPU cores
                    faculty.setPasswordHash(passwordEncoder.encode(re.getPwd()));
                    faculty.setSalutation(re.getSalutation());
                    faculty.setGender(re.getGender());
                    return faculty;
                })
                .toList();

        if (!newFaculties.isEmpty()) {
            facultyRepository.saveAll(newFaculties);
            log.info("Successfully saved {} new faculties", newFaculties.size());
        }
    }

    public List<FacultyResponse> searchFacultyByName(String name, PageRequest pageRequest) {
        List<Faculty> faculties = facultyRepository.findByNameContainingIgnoreCase(name, pageRequest).getContent();
        log.info("Found {} faculties matching name '{}'", faculties.size(), name);
        return faculties.stream()
                .map(autoMapper::mapFacultyToFacultyResponse)
                .collect(Collectors.toList());
    }

    public List<FacultyResponse> searchFacultyByCode(String code, PageRequest pageRequest) {
        List<Faculty> faculties = facultyRepository.findByFacultyCodeContainingIgnoreCase(code, pageRequest).getContent();
        log.info("Found {} faculties matching code '{}'", faculties.size(), code);
        return faculties.stream()
                .map(autoMapper::mapFacultyToFacultyResponse)
                .collect(Collectors.toList());
    }

    public List<FacultyAssignmentResponse> getFacultyAssignments(int facultyId){
        List<ClassSubject> assignedClassSubjects = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with Id " + facultyId + " not found."))
                .getAssignedClassSubjects();

        return assignedClassSubjects.stream()
                .map(autoMapper::mapClassSubjectToFacultyAssignmentResponse)
                .toList();
    }

    public FacultyResponse getFacultyById(int facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with Id " + facultyId + " not found."));
        return autoMapper.mapFacultyToFacultyResponse(faculty);
    }

    public Pair<Boolean, String> resetFacultyPassword(int facultyId, String newPassword, String oldPassword) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with Id " + facultyId + " not found."));
        boolean changed = true;
        String message;
        if (oldPassword != null && !passwordEncoder.matches(oldPassword, faculty.getPasswordHash())) {
            message = "Old password is incorrect. Password not changed.";
            log.info("Old password is incorrect for facultyId = {}. Password not changed.", facultyId);
            changed = false;
        } else {
            faculty.setPasswordHash(passwordEncoder.encode(newPassword));
            facultyRepository.save(faculty);
            log.info("Password reset successfully for facultyId = {}", facultyId);
            message = "Password reset successfully.";
        }
        return Pair.of(changed, message);
    }
}