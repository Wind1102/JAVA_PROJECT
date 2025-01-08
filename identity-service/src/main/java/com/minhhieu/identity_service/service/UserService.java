package com.minhhieu.identity_service.service;

import com.minhhieu.identity_service.dto.response.UserResponse;
import com.minhhieu.identity_service.enums.Role;
import com.minhhieu.identity_service.exception.AppException;
import com.minhhieu.identity_service.dto.request.UserCreationRequest;
import com.minhhieu.identity_service.dto.request.UserUpdateRequest;
import com.minhhieu.identity_service.entity.Users;
import com.minhhieu.identity_service.exception.ErrorCode;
import com.minhhieu.identity_service.mapper.UserMapper;
import com.minhhieu.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserMapper userMapper;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public Users createRequet(UserCreationRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Users user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        return userRepository.save(user);
    }

    public UserResponse getMyInfor(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Users user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.INVALID_USERNAME));
        return userMapper.toUserResponse(user);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getUsers(){
        log.info("Get all users:");
        return userRepository.findAll();
    }

    public Users updateUser(String id,UserUpdateRequest request){
        var user = getUserById(id);
        userMapper.toUser(request,user);
        return userRepository.save(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public Users getUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
}
