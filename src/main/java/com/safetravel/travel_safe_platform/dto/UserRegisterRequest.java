package com.safetravel.travel_safe_platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank(message = "아이디 필수")
    private String username;

    @Email(message = "이메일 형식 이상함")
    private String email;

    @Size(min = 4, message = "비밀번호 4자 이상")
    private String password;
}