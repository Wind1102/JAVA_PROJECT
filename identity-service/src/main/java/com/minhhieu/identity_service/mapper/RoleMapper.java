package com.minhhieu.identity_service.mapper;


import com.minhhieu.identity_service.dto.request.RoleRequest;
import com.minhhieu.identity_service.dto.response.RoleResponse;
import com.minhhieu.identity_service.entity.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
    Role toRole(RoleRequest roleRequest);
}
