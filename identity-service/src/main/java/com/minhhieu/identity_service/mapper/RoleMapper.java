package com.minhhieu.identity_service.mapper;


import com.minhhieu.identity_service.dto.request.RoleRequest;
import com.minhhieu.identity_service.dto.response.RoleResponse;
import com.minhhieu.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);
}
