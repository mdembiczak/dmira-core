package com.dcmd.dmiracore.controller.auth;

import com.dcmd.dmiracore.payload.auth.LoginRequest;
import com.dcmd.dmiracore.payload.auth.SignupRequest;
import com.dcmd.dmiracore.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Hidden
    @RequestMapping(path = "/signin", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest);
    }

    @Hidden
    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.registerUser(signupRequest);
    }
}
