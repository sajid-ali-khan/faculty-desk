package com.sajid.college_app;

import com.sajid.college_app.models.Student;
import com.sajid.college_app.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
public class StudentRepositoryIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findByRollNumberContaining_shouldReturnStudentsFromRealDatabase() {
        // Test case 1: Search for '229x1a28' - should return 69 students
        List<Student> result1 = studentRepository.findByRollNumberContaining("229X1A28");
        assertThat(result1).hasSize(69);

        // Test case 2: Search for '229x1a2851' - should return student named 'sajid'
        List<Student> result2 = studentRepository.findByRollNumberContaining("229X1A2851");
        assertThat(result2).hasSize(1);
        assertThat(result2.get(0).getName()).isEqualToIgnoringCase("sajid");
    }
}
