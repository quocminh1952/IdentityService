package com.minh1952.identityservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINT = {"/user","/auth/verifytoken","auth/login"};

    @Value("${jwt.secretkey}")
    private String secretkey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // Cấu Hình request
        httpSecurity.authorizeHttpRequests(request ->
                request
                        //cho tất cả yêu cầu POST đến /user không cần xác thực
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
                        // chỉ Admin mới gửi method Get đến /users
                        .requestMatchers(HttpMethod.GET,"/users").hasAuthority("ROLE_ADMIN")
                        //tất cả các yêu cầu khác phải xác thực
                        .anyRequest().authenticated());

        // cho Spring biết sẽ làm việc với một resourceSever là oauth2 để xác thực người dùng
        // mọi request đến hệ thống phải được xác thực jwt token
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder()) // cấu hình JwtDecoder để giải mã JWT
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()) //chuyển scope thành role
                )
                );

        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        return httpSecurity.build();
    }

    // Bộ phận giải mã token
    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretkey.getBytes(),"HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    };

    // mặc định JWT sẽ chuyển đổi claim scope thành các quyền hạn để phục vụ việc cấu hình security
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        // đối tượng chịu trách nhiệm chuyển đổi claim thành authorities - mặc định là scope
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // setAuthorityPrefix để chuyển đổi tiền tố cho trường tử scope: "SCOPE_AMIN" ->  scope : "ROLE_ADMIN"
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // đối tượng JwtAuthenticationConverter sử dụng để chuyển đổi JWT token thành quyền hạn
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }


    // tạo phương thức mã hóa mật khẩu:
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


}
