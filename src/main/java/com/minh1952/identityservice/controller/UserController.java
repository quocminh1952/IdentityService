package com.minh1952.identityservice.controller;

import com.minh1952.identityservice.dto.response.ApiResponse;
import com.minh1952.identityservice.dto.request.UserCreationRequest;
import com.minh1952.identityservice.dto.request.UserUpdateRequest;
import com.minh1952.identityservice.dto.response.UserResponse;
import com.minh1952.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){

        var authentication = SecurityContextHolder.getContext().getAuthentication(); // authentication : đại diện cho thông tin nguoi dung dang xac thuc
        log.warn("Username: " + authentication.getName()); // lấy tên người dùng
        log.warn("Role    : " + authentication.getAuthorities());// lấy quyền người dùng
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId,request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId){
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> myInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.myInfo())
                .build();
    }

}
