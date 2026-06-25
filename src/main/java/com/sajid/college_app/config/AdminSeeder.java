package com.sajid.college_app.config;

import com.sajid.college_app.models.Faculty;
import com.sajid.college_app.models.FacultyRole;
import com.sajid.college_app.repositories.FacultyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Slf4j
@AllArgsConstructor
public class AdminSeeder implements ApplicationRunner {
    private final FacultyRepository facultyRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        String adminUName="admin@gprec";
        String adminPassword = "admin@1584";

        if (facultyRepository.existsByFacultyCode(adminUName)){
            log.info("Admin already exists.");
            return;
        }

        Faculty admin = new Faculty();
        admin.setFacultyCode(adminUName);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRole(FacultyRole.ADMIN);
        admin.setName("admin");

        facultyRepository.save(admin);
        log.info("Admin created successfully");
    }
}
