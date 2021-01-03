package com.dcmd.dmiracore.controller.user;

import com.dcmd.dmiracore.payload.user.UserResponse;
import com.dcmd.dmiracore.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;

    @GetMapping
    public ResponseEntity<Set<UserResponse>> getUsers() {
        return userService.getAllUsers();
    }
}
