package com.minhhieu.identity_service.controller;


import com.minhhieu.identity_service.dto.request.PermissionRequest;
import com.minhhieu.identity_service.dto.response.ApiResponse;
import com.minhhieu.identity_service.dto.response.PermissionReponse;
import com.minhhieu.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionReponse> create(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionReponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionReponse>> getAll(){
        return ApiResponse.<List<PermissionReponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission){
        permissionService.delete(permission);
        return ApiResponse.<Void>builder().build();
    }
}
