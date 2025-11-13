package com.sajid.college_app.services;

import com.sajid.college_app.models.Faculty;
import com.sajid.college_app.repositories.FacultyRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FacultyDetailsService implements UserDetailsService {
    private final FacultyRepository facultyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Faculty> faculty = facultyRepository.findByFacultyCode(username);
        if (faculty.isEmpty())
            throw new UsernameNotFoundException("The faculty with code = "+username + " not found");
        return faculty.get();
    }
}
