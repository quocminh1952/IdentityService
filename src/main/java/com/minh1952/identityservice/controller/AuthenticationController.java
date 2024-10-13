package com.minh1952.identityservice.controller;

import com.minh1952.identityservice.dto.request.VerifyTokenRequest;
import com.minh1952.identityservice.dto.response.ApiResponse;
import com.minh1952.identityservice.dto.request.AuthRequest;
import com.minh1952.identityservice.dto.response.AuthResponse;
import com.minh1952.identityservice.dto.response.VerifyTokenRepsonse;
import com.minh1952.identityservice.service.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
// @RequiredArgsConstructor tìm cac biến là final hoặc notnull và tạo instance cho chúng
// sau đó tạo tự động tạo Construct và inject các instance vào constructor
public class AuthenticationController {

    AuthService AuthService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> Authenticate(@RequestBody AuthRequest request){
        var result = AuthService.Authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/verifytoken")
    ApiResponse<VerifyTokenRepsonse> Authenticate(@RequestBody VerifyTokenRequest request) throws ParseException, JOSEException {
        var result = AuthService.VerifyToken(request);
        return ApiResponse.<VerifyTokenRepsonse>builder()
                .result(result)
                .build();
    }

}
