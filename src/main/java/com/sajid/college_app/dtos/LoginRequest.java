package com.sajid.college_app.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String facultyCode;
    private String password;
}
