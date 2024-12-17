package com.minhhieu.identity_service.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    public Users createRequet(UserCreationRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Users user = userMapper.toUser(request);
        return userRepository.save(user);
    }

    public List<Users> getUsers(){
        return userRepository.findAll();
    }

    public Users updateUser(String id,UserUpdateRequest request){
        var user = getUserById(id);
        userMapper.toUser(request,user);
        return userRepository.save(user);
    }

    public Users getUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
}
