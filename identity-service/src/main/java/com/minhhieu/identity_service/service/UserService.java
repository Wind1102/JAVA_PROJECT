package com.minhhieu.identity_service.service;

import com.minhhieu.identity_service.dto.request.UserCreationRequest;
import com.minhhieu.identity_service.dto.request.UserUpdateRequest;
import com.minhhieu.identity_service.entity.Users;
import com.minhhieu.identity_service.mapper.UserMapper;
import com.minhhieu.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    public Users createRequet(UserCreationRequest request){
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    public List<Users> getUsers(){
        return userRepository.findAll();
    }

    public Users updateUser(String id,UserUpdateRequest request){
        var user = getUserById(id);
        userMapper.updateUserFromRequest(request,user);
        return userRepository.save(user);
    }

    public Users getUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
}
