package com.sajid.college_app.services;

import com.sajid.college_app.models.Faculty;
import com.sajid.college_app.models.raw.Employee;
import com.sajid.college_app.repositories.FacultyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

//    public void searchFaculty(String name) {
//        List<Faculty> faculties = facultyRepository.findByNameContainingIgnoreCase(name);
//        log.info("Found {} faculties matching '{}'", faculties.size(), name);
//        for (Faculty faculty : faculties) {
//            log.info("Faculty Code: {}, Name: {}", faculty.getFacultyCode(), faculty.getName());
//        }
//    }
}