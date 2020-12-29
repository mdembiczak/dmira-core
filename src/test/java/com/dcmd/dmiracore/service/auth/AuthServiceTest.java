package com.dcmd.dmiracore.service.auth;

import com.dcmd.dmiracore.model.Role;
import com.dcmd.dmiracore.payload.auth.JwtResponse;
import com.dcmd.dmiracore.payload.auth.LoginRequest;
import com.dcmd.dmiracore.payload.auth.SignupRequest;
import com.dcmd.dmiracore.payload.messages.MessageResponse;
import com.dcmd.dmiracore.repository.RoleRepository;
import com.dcmd.dmiracore.repository.UserRepository;
import com.dcmd.dmiracore.security.jwt.JwtUtils;
import com.dcmd.dmiracore.security.services.UserDetailsImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {
    public static final String TOKEN = "TOKEN";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "username@test.com";

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtils jwtUtils;

    @InjectMocks
    AuthService authService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuccessfulLogin() {
        UserDetailsImpl userDetails = mockUserDetails();
        mockAuthentication(userDetails);
        LoginRequest loginRequest = mockLoginRequest();
        mockJwtUtils();

        ResponseEntity<JwtResponse> response = authService.loginUser(loginRequest);

        assertThat(Objects.requireNonNull(response.getBody()).getAccessToken()).isEqualTo(TOKEN);
        assertThat(response.getBody().getUsername()).isEqualTo(USERNAME);
        assertThat(response.getBody().getRoles()).isEmpty();
    }

    @Test
    public void testFailIfUserIsAlreadyTaken() {
        SignupRequest signupRequest = mockSignupRequest(Collections.emptySet());
        when(authService.userRepository.existsByUsername(any())).thenReturn(true);

        ResponseEntity<MessageResponse> response = authService.registerUser(signupRequest);

        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("Error: Username is already taken.");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testFailIfEmailIsAlreadyUse() {
        SignupRequest signupRequest = mockSignupRequest(Collections.emptySet());
        when(authService.userRepository.existsByUsername(any())).thenReturn(false);
        when(authService.userRepository.existsByEmail(any())).thenReturn(true);

        ResponseEntity<MessageResponse> response = authService.registerUser(signupRequest);

        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("Error: Email is already use.");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testInputRolesAreEmpty() {
        SignupRequest signupRequest = mockSignupRequest(Collections.emptySet());
        when(authService.userRepository.existsByUsername(any())).thenReturn(false);
        when(authService.userRepository.existsByEmail(any())).thenReturn(false);
        when(authService.roleRepository.findRoleByName(any())).thenReturn(Optional.empty());

        try {
            authService.registerUser(signupRequest);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Error: Role is not found.");
        }
    }

    @Test
    public void testWhenInputRoleIsInvalid() {
        SignupRequest signupRequest = mockSignupRequest(Collections.singleton("NOT_EXISTING_ROLE"));
        when(authService.userRepository.existsByUsername(any())).thenReturn(false);
        when(authService.userRepository.existsByEmail(any())).thenReturn(false);
        Role role = mock(Role.class);
        when(authService.roleRepository.findRoleByName(any())).thenReturn(Optional.of(role));

        ResponseEntity<MessageResponse> response = authService.registerUser(signupRequest);

        Mockito.verify(authService.userRepository, times(1)).save(any());
        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("User registered successfully.");
    }

    private SignupRequest mockSignupRequest(Set<String> roles) {
        SignupRequest signupRequest = mock(SignupRequest.class);
        when(signupRequest.getUsername()).thenReturn(USERNAME);
        when(signupRequest.getPassword()).thenReturn(PASSWORD);
        when(signupRequest.getRoles()).thenReturn(roles);
        when(signupRequest.getEmail()).thenReturn(EMAIL);

        return signupRequest;
    }

    private UserDetailsImpl mockUserDetails() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn(USERNAME);
        return userDetails;
    }

    private void mockAuthentication(UserDetailsImpl userDetails) {
        Authentication authentication = mock(Authentication.class);
        when(authService.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    private LoginRequest mockLoginRequest() {
        LoginRequest loginRequest = Mockito.mock(LoginRequest.class);
        when(loginRequest.getUsername()).thenReturn(USERNAME);
        when(loginRequest.getPassword()).thenReturn(PASSWORD);
        return loginRequest;
    }

    private void mockJwtUtils() {
        when(authService.jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn(TOKEN);
    }
}
