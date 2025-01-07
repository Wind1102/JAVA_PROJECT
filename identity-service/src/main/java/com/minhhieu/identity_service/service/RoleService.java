package com.minhhieu.identity_service.service;


import com.minhhieu.identity_service.dto.request.RoleRequest;
import com.minhhieu.identity_service.dto.response.RoleResponse;
import com.minhhieu.identity_service.entity.Role;
import com.minhhieu.identity_service.mapper.RoleMapper;
import com.minhhieu.identity_service.repository.PermissionRepository;
import com.minhhieu.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void deleteById(String role) {
        roleRepository.deleteById(role);
    }

}
