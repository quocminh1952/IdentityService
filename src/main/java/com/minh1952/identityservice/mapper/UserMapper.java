package com.minh1952.identityservice.mapper;

import com.minh1952.identityservice.dto.request.UserCreationRequest;
import com.minh1952.identityservice.dto.request.UserUpdateRequest;
import com.minh1952.identityservice.dto.response.UserResponse;
import com.minh1952.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
