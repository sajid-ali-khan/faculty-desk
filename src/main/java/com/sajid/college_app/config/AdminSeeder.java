package com.sajid.college_app.config;

import com.sajid.college_app.models.Faculty;
import com.sajid.college_app.models.FacultyRole;
import com.sajid.college_app.repositories.FacultyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Slf4j
public class AdminSeeder implements ApplicationRunner {
    FacultyRepository facultyRepository;
    BCryptPasswordEncoder passwordEncoder;

    public AdminSeeder(FacultyRepository facultyRepository, BCryptPasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        String adminUName="admin@gprec";
        String adminPassword = "admin@1584";

        if (args.getOptionValues("seed") == null){
            log.info("Admin seeder skipped.");
            return;
        }

        if (facultyRepository.existsByFacultyCode(adminUName)){
            log.info("Admin already exists.");
            return;
        }

        Faculty admin = Faculty.builder()
                .facultyCode(adminUName)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .role(FacultyRole.ADMIN)
                .name("admin")
                .build();

        facultyRepository.save(admin);
        log.info("Admin created successfully");
    }
}
