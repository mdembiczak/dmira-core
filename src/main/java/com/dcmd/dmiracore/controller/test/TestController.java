package com.dcmd.dmiracore.controller.test;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Hidden
    @GetMapping("/all")
    public String allAccess() {
        return "Public content";
    }

    @Hidden
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User content";
    }

    @Hidden
    @GetMapping("/owner")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public String moderatorAccess() {
        return "Owner content";
    }

    @Hidden
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String AdminAccess() {
        return "Admin content";
    }
}
