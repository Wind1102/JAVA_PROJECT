package com.minhhieu.identity_service.mapper;


import com.minhhieu.identity_service.dto.request.PermissionRequest;
import com.minhhieu.identity_service.dto.response.PermissionReponse;
import com.minhhieu.identity_service.entity.Permission;
import com.minhhieu.identity_service.repository.PermissionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionReponse toPermissionResponse(Permission permission);
}
