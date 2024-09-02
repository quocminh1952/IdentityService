package com.minh1952.identityservice.controller;

import com.minh1952.identityservice.dto.response.ApiResponse;
import com.minh1952.identityservice.dto.request.AuthRequest;
import com.minh1952.identityservice.dto.response.AuthResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
// @RequiredArgsConstructor tìm cac biến là final và tạo instance
// sau đó tạo tự động tạo Construct và inject các instace vào constructor
public class AuthenticationController {

    com.minh1952.identityservice.service.AuthService AuthService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> Authentication(@RequestBody AuthRequest request){
        boolean resultLogin = AuthService.Authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .result(AuthResponse.builder().Authenticate(resultLogin).build())
                .build();
    }

}
