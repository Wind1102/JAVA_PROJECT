package com.minhhieu.identity_service.mapper;

import com.minhhieu.identity_service.dto.request.UserCreationRequest;
import com.minhhieu.identity_service.dto.request.UserUpdateRequest;
import com.minhhieu.identity_service.dto.response.UserResponse;
import com.minhhieu.identity_service.entity.Users;
import org.apache.catalina.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    Users toUser(UserCreationRequest request);
    UserResponse toUserResponse(Users user);
    void toUser(UserUpdateRequest request, @MappingTarget Users users);

}
