package com.dcmd.dmiracore.service.user.mapper;

import com.dcmd.dmiracore.model.Role;
import com.dcmd.dmiracore.model.User;
import com.dcmd.dmiracore.payload.user.UserResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponse mapEntityToResponse(User user) {
        return UserResponse.Builder.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(mapRolesToNames(user.getRoles()))
                .build();
    }

    public Set<String> mapRolesToNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .map(Objects::toString)
                .collect(Collectors.toSet());

    }
}
