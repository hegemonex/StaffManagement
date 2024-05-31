package ge.epam.staffmanagement.controller;

import ge.epam.staffmanagement.config.JwtUtil;
import ge.epam.staffmanagement.entity.User;
import ge.epam.staffmanagement.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

//TODO: provide documentation for swagger pls
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/")
    public RedirectView redirectToLogin() {
        return new RedirectView("/login.html");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        //FIXME: would be better to validate the changes on the DTO layer.
        //FIXME: I would recommend creating UserDTO and use javax || jakarta validation and then you can change
        //FIXME: registerUser(@RequestBody User user) -> registerUser(@RequestBody @Validate UserDto user) {
        String username = user.getUsername();
        String password = user.getPassword();

        if (username == null || username.length() < 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username must be at least 5 characters long.");
        }
        if (password == null || password.length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 8 characters long.");
        }

        userDetailsService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //FIXME: you have duplications of next 3 rows -> move them to separate method
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String accessToken = jwtUtil.createToken(new HashMap<>(), userDetails.getUsername(), JwtUtil.ACCESS_TOKEN_VALIDITY);
        String refreshToken = jwtUtil.createToken(new HashMap<>(), userDetails.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDITY);

        Map<String, String> response = new HashMap<>();
        //FIXME: avoid of using magic strings (and you have suplicates of them) https://deviq.com/antipatterns/magic-strings
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.createToken(new HashMap<>(), userDetails.getUsername(), JwtUtil.ACCESS_TOKEN_VALIDITY);
        String newRefreshToken = jwtUtil.createToken(new HashMap<>(), userDetails.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDITY);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", newRefreshToken);

        return ResponseEntity.ok(response);
    }
}
