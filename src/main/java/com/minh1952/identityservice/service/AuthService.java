package com.minh1952.identityservice.service;

import com.minh1952.identityservice.dto.request.AuthRequest;
import com.minh1952.identityservice.dto.request.VerifyTokenRequest;
import com.minh1952.identityservice.dto.response.AuthResponse;
import com.minh1952.identityservice.dto.response.VerifyTokenRepsonse;
import com.minh1952.identityservice.entity.User;
import com.minh1952.identityservice.enums.Role;
import com.minh1952.identityservice.exception.AppException;
import com.minh1952.identityservice.exception.ErrorCode;
import com.minh1952.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.secretkey}")
    private String secretKey;

    public VerifyTokenRepsonse VerifyToken(VerifyTokenRequest request) throws ParseException, JOSEException {
        boolean result = false;

        // lấy token từ request
        String token = request.getToken();

        // giải mã token thành 1 đối tượng ( header + payload + signature )
        SignedJWT signedJWT =  SignedJWT.parse(token);

        // tạo Verifier để kiếm tra signature ( signature(chữ ký) = HMACSHA256( base64UrlEncode(header) + "." + base64UrlEncode(payload),secretkey)
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

        // sử dụng verifier vừa tạo để kiểm tra signature
        boolean verified = signedJWT.verify(verifier);

        // kiểm tra token đã hết hạn hay chưa
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if(verified && expiryTime.after(new Date())){
            result = true;
        }
        return VerifyTokenRepsonse.builder()
                .valid(result)
                .build();
    }

    public AuthResponse Authenticate (AuthRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                            .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(),user.getPassword());

        // đăng nhập không thành công
        if(!authenticated)
            throw new AppException(ErrorCode.NON_AUTHENTICATED);

        String token = generateToken(user);

        return  AuthResponse.builder()
                .Authenticate(authenticated)
                .token(token)
                .build();
    }

    // Tạo token khi login
    private String generateToken(User user){
        //create header
        JWSHeader jwtHeader = new JWSHeader(JWSAlgorithm.HS512);
        //create payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // đại diện thông tin đăng nhập
                .issuer("minh1952") // người phát hành
                .issueTime(new Date()) // thời gian phát hành
                .expirationTime(new Date(System.currentTimeMillis() + 3600*1000))
                .claim("scope", BuildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwtHeader,payload);

        //create signature ( chứ ký trong JWT )
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize(); // trả về một chuỗi đại diện cho JWT
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }

    }

    // Phương thức truyền giá trị scope cho JWT : lấy ra cac quyền và tạo thành một chuỗi cách nhau bởi space
    private String BuildScope(User user){
        return user.getRoles().stream() // stream : Api để xử lý dữ liệu cho các tập dữ liệu (Set, List ...)
                .collect(Collectors.joining(" ")); // bộ thu collect sử dụng Collectors.joining để tạo ra một chuỗi có dấu phân cách
    }


}
