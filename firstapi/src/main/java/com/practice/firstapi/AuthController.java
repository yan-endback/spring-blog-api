package com.practice.firstapi;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }
    @PostMapping("/auth/login")
    public TokenResponse login(@RequestBody LoginRequest request){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(), request.password()));
        return new TokenResponse(jwtService.generateToken(request.username()));
    }

}
