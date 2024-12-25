package com.minhhieu.identity_service.service;


import com.minhhieu.identity_service.dto.request.RoleRequest;
import com.minhhieu.identity_service.dto.response.RoleResponse;
import com.minhhieu.identity_service.entity.Role;
import com.minhhieu.identity_service.mapper.RoleMapper;
import com.minhhieu.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        Role role = roleMapper.toRole(request);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }
}
