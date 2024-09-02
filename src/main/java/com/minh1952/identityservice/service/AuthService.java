package com.minh1952.identityservice.service;

import com.minh1952.identityservice.dto.request.AuthRequest;
import com.minh1952.identityservice.exception.AppException;
import com.minh1952.identityservice.exception.ErrorCode;
import com.minh1952.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;

    public boolean Authenticate (AuthRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                            .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(),user.getPassword());
    }

}
