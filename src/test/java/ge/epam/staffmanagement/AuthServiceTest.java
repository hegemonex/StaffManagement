package ge.epam.staffmanagement;

import ge.epam.staffmanagement.config.JwtUtil;
import ge.epam.staffmanagement.controller.AuthController;
import ge.epam.staffmanagement.entity.User;
import ge.epam.staffmanagement.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Registration tests
    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("tekla");
        user.setPassword("12345678");

        ResponseEntity<String> response = authController.registerUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userDetailsService, times(1)).saveUser(user);
    }

    @Test
    public void testRegisterUser_ShortUsername() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("12345678");

        ResponseEntity<String> response = authController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username must be at least 5 characters long.", response.getBody());
        verify(userDetailsService, times(0)).saveUser(user);
    }

    @Test
    public void testRegisterUser_ShortPassword() {
        User user = new User();
        user.setUsername("tekla");
        user.setPassword("123");

        ResponseEntity<String> response = authController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password must be at least 8 characters long.", response.getBody());
        verify(userDetailsService, times(0)).saveUser(user);
    }

    // Login tests
    @Test
    public void testLoginUser_Success() {
        User user = new User();
        user.setUsername("tekla");
        user.setPassword("12345678");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(user.getUsername());
        when(jwtUtil.createToken(anyMap(), eq(user.getUsername()), eq(JwtUtil.ACCESS_TOKEN_VALIDITY))).thenReturn(accessToken);
        when(jwtUtil.createToken(anyMap(), eq(user.getUsername()), eq(JwtUtil.REFRESH_TOKEN_VALIDITY))).thenReturn(refreshToken);

        ResponseEntity<Map<String, String>> response = authController.loginUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accessToken, response.getBody().get("accessToken"));
        assertEquals(refreshToken, response.getBody().get("refreshToken"));
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.loginUser(user);
        });

        assertEquals("Bad credentials", exception.getMessage());
    }

    // Refresh token tests
    @Test
    public void testRefreshAccessToken_Success() {
        String refreshToken = "validRefreshToken";
        String username = "user";
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";

        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("refreshToken", refreshToken);

        UserDetails userDetails = mock(UserDetails.class);
        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
        when(jwtUtil.extractUsername(refreshToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(jwtUtil.createToken(anyMap(), eq(username), eq(JwtUtil.ACCESS_TOKEN_VALIDITY))).thenReturn(newAccessToken);
        when(jwtUtil.createToken(anyMap(), eq(username), eq(JwtUtil.REFRESH_TOKEN_VALIDITY))).thenReturn(newRefreshToken);

        ResponseEntity<?> responseEntity = authController.refreshAccessToken(tokenRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
        assertEquals(newAccessToken, responseBody.get("accessToken"));
        assertEquals(newRefreshToken, responseBody.get("refreshToken"));
    }

    @Test
    public void testRefreshAccessToken_InvalidToken() {
        String refreshToken = "invalidRefreshToken";

        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("refreshToken", refreshToken);

        when(jwtUtil.validateToken(refreshToken)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.refreshAccessToken(tokenRequest);
        });

        assertEquals("Invalid refresh token", exception.getMessage());
    }

    @Test
    public void testRefreshAccessToken_NullToken() {
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("refreshToken", null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.refreshAccessToken(tokenRequest);
        });

        assertEquals("Invalid refresh token", exception.getMessage());
    }
}
