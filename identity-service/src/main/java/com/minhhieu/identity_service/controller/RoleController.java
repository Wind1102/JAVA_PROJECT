package com.minhhieu.identity_service.controller;


import com.minhhieu.identity_service.dto.request.PermissionRequest;
import com.minhhieu.identity_service.dto.request.RoleRequest;
import com.minhhieu.identity_service.dto.response.ApiResponse;
import com.minhhieu.identity_service.dto.response.PermissionReponse;
import com.minhhieu.identity_service.dto.response.RoleResponse;
import com.minhhieu.identity_service.service.PermissionService;
import com.minhhieu.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String role){
        roleService.deleteById(role);
        return ApiResponse.<Void>builder().build();
    }
}
