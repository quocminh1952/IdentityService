package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.dto.request.AuthRequest;
import com.devteria.identityservice.dto.response.AuthResponse;
import com.devteria.identityservice.service.AuthService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
// @RequiredArgsConstructor tìm cac biến là final và tạo instance
// sau đó tạo tự động tạo Construct và inject các instace vào constructor
public class AuthenticationController {

    AuthService AuthService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> Authentication(@RequestBody AuthRequest request){
        boolean resultLogin = AuthService.Authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .result(AuthResponse.builder().Authenticate(resultLogin).build())
                .build();
    }

}
