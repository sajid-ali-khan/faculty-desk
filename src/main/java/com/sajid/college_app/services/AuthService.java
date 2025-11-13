package com.sajid.college_app.services;


import com.sajid.college_app.dtos.JwtResponse;
import com.sajid.college_app.dtos.LoginRequest;
import com.sajid.college_app.jwt.JwtUtils;
import com.sajid.college_app.models.Faculty;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    JwtUtils jwtUtils;
    AuthenticationManager authenticationManager;

    public Optional<JwtResponse> loginUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getFacultyCode(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Faculty userDetails = (Faculty) authentication.getPrincipal();
        String jwt = jwtUtils.generateTokenFromUsername(userDetails);

        return Optional.of(new JwtResponse(
                jwt,
                "Bearer",
                userDetails.getUsername(),
                userDetails.getRole().name()
        ));
    }
}