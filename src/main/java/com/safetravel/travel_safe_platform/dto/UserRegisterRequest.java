package com.safetravel.travel_safe_platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    private String username;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.") // 💡 null 방지를 위해 추가 권장
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password;

    // 추가!!
    private String passwordConfirm;

    // 💡 [여기에 필드 추가!] 화면의 th:field="*{nickname}"과 매핑됩니다.
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickname;
}