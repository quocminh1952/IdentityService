package com.minh1952.identityservice.service;

import com.minh1952.identityservice.dto.request.UserCreationRequest;
import com.minh1952.identityservice.dto.request.UserUpdateRequest;
import com.minh1952.identityservice.dto.response.UserResponse;
import com.minh1952.identityservice.entity.User;
import com.minh1952.identityservice.enums.Role;
import com.minh1952.identityservice.exception.AppException;
import com.minh1952.identityservice.exception.ErrorCode;
import com.minh1952.identityservice.mapper.UserMapper;
import com.minh1952.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        // set độ mạnh cho mã hóa ( độ mạnh quá cao => ảnh hưởng độ xử lý hệ thống)
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        // mã hóa password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // vì role là 1 kiểu Set<String> nên phải truyền vào setRoles kiểu tương ứng:
        Set<String> Roles = new HashSet<>(); // HashSet : không cho phép lưu cac gia trị trùng lặp
        Roles.add(Role.USER.name());

        user.setRoles(Roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')") // Kiểm tra quyền trước khi gọi method
    public List<UserResponse> getUsers(){
        log.info("only role admin can access this method !");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }
    // Kiểm tra quyền sau khi method dc gọi .. nếu k phải ADMIN thì sẽ chặn k cho trả về
    //@PostAuthorize("hasRole('ADMIN')")
    // User có thể lấy được thông tin của chính mình
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    // khi user đăng nhập vào hệ thống -> SecurityContextHolder lưu trữ thông tin của người dùng
    public UserResponse myInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User byUserName = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(byUserName);
    }

}
