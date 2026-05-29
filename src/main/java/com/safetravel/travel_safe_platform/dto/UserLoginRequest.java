package com.safetravel.travel_safe_platform.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 폼 요청 DTO.
 */
@Getter
@Setter
public class UserLoginRequest {

    /** 로그인 아이디 */
    private String username;

    /** 비밀번호 */
    private String password;
}
