package com.safetravel.travel_safe_platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청 DTO.
 */
@Getter
@Setter
public class UserRegisterRequest {

    /** 로그인 아이디 */
    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    private String username;

    /** 이메일 */
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email;

    /** 비밀번호 */
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password;

    /** 비밀번호 확인 */
    private String passwordConfirm;

    /** 닉네임 */
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickname;
}
