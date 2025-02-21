package com.minhhieu.identity_service.controller;

import com.minhhieu.identity_service.dto.response.ApiResponse;
import com.minhhieu.identity_service.dto.response.UserResponse;
import com.minhhieu.identity_service.dto.request.UserCreationRequest;
import com.minhhieu.identity_service.dto.request.UserUpdateRequest;
import com.minhhieu.identity_service.entity.Users;
import com.minhhieu.identity_service.mapper.UserMapper;
import com.minhhieu.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping()
    ApiResponse<Users> createUser(@RequestBody @Valid UserCreationRequest request){
        log.info("Controller: Create USer");
        ApiResponse<Users> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createRequet(request));
        return apiResponse;
    }

    @GetMapping()
    List<UserResponse> getAllUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        List<Users> allUsers = userService.getUsers();
        List<UserResponse> allUserResponses = allUsers.stream().map(user -> userMapper.toUserResponse(user)).collect(Collectors.toList());
        return allUserResponses;
    }

    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable("userId") String userId){
        var user = userService.getUserById(userId);
        return userMapper.toUserResponse(user);
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfor(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfor())
                .build();
    }

    @PutMapping("/{userId}")
    Users updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId,request);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
    }
}
