package ge.epam.staffmanagement.controller;

import ge.epam.staffmanagement.config.JwtService;
import ge.epam.staffmanagement.entity.User;
import ge.epam.staffmanagement.model.UserDTO;
import ge.epam.staffmanagement.service.impl.CustomUserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Api(value = "Authentication Controller", description = "REST APIs for user authentication")
@RestController
@Validated
public class AuthController {
    private static final String TOKEN_TYPE_ACCESS = "accessToken";
    private static final String TOKEN_TYPE_REFRESH = "refreshToken";
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @ApiOperation(value = "Redirect to login page", response = RedirectView.class)
    @GetMapping("/")
    public RedirectView redirectToLogin() {
        return new RedirectView("/login.html");
    }

    @ApiOperation(value = "Register a new user", response = ResponseEntity.class)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDTO userDTO) {
        //FIXME: you don't need it after markup with @Validated the controller and @Valid on Dto. Delete code bellow
//        String username = userDTO.getUsername();
//        String password = userDTO.getPassword();
//        if (username == null || username.length() < 5) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username must be at least 5 characters long.");
//        }
//        if (password == null || password.length() < 8) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 8 characters long.");
//        }
        userDetailsService.saveUser(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @ApiOperation(value = "Authenticate user and get JWT tokens", response = ResponseEntity.class)
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> response = getTokens(user.getUsername());
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Refresh JWT access token", response = ResponseEntity.class)
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get(TOKEN_TYPE_REFRESH);
        if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String username = jwtService.extractUsername(refreshToken);
        Map<String, String> response = getTokens(username);
        return ResponseEntity.ok(response);
    }

    private Map<String, String> getTokens(String userName) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        String newAccessToken = jwtService.createToken(new HashMap<>(), userDetails.getUsername(), JwtService.ACCESS_TOKEN_VALIDITY);
        String newRefreshToken = jwtService.createToken(new HashMap<>(), userDetails.getUsername(), JwtService.REFRESH_TOKEN_VALIDITY);
        Map<String, String> response = new HashMap<>();
        response.put(TOKEN_TYPE_ACCESS, newAccessToken);
        response.put(TOKEN_TYPE_REFRESH, newRefreshToken);
        return response;
    }
}
