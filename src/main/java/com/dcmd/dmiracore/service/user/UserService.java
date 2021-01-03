package com.dcmd.dmiracore.service.user;

import com.dcmd.dmiracore.model.User;
import com.dcmd.dmiracore.payload.user.UserResponse;
import com.dcmd.dmiracore.repository.UserRepository;
import com.dcmd.dmiracore.service.user.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public ResponseEntity<Set<UserResponse>> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        Set<UserResponse> userResponses = allUsers.stream()
                .map(user -> userMapper.mapEntityToResponse(user))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(userResponses);
    }
}
