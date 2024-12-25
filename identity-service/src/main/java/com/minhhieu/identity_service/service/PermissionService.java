package com.minhhieu.identity_service.service;


import com.minhhieu.identity_service.dto.request.PermissionRequest;
import com.minhhieu.identity_service.dto.response.PermissionReponse;
import com.minhhieu.identity_service.entity.Permission;
import com.minhhieu.identity_service.mapper.PermissionMapper;
import com.minhhieu.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionReponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    };

    public List<PermissionReponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
