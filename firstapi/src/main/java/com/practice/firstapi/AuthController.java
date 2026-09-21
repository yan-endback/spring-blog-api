package com.practice.firstapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService, AuthService authService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.authService = authService;
    }
    @PostMapping ("/auth/login")
    public TokenResponse login(@RequestBody LoginRequest request){
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),request.password()
        ));
        String role = auth.getAuthorities().iterator().next().getAuthority();
        return new TokenResponse(jwtService.generateToken(request.username(),role));
    }
    @GetMapping("/")
    public String home() {
        return "Авторизация прошла успешно! Добро пожаловать.";
    }
    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request){
        authService.register(request.username(),request.password());
    }
    @GetMapping ("/auth/me")
    public MeResponse me (@AuthenticationPrincipal UserDetails user){
        String role = user.getAuthorities().iterator().next().getAuthority();
        return new MeResponse(user.getUsername(), role);
    }

}
