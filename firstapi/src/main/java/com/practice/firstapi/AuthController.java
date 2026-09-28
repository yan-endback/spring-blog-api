package com.practice.firstapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final RefreshTokenService refreshTokenService;
    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService, AuthService authService,RefreshTokenService refreshTokenService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }
    @PostMapping ("/auth/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request){
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),request.password()
        ));
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String access = jwtService.generateToken(request.username(), role);
        RefreshToken refresh = refreshTokenService.create(request.username());
        return new TokenResponse(access, refresh.getToken());
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
    @PostMapping ("/auth/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request){
        RefreshToken newRefresh = refreshTokenService.rotate(request.refreshToken());
        AppUser user = newRefresh.getUser();
        String role = "ROLE_" + user.getRole().name();   // USER -> ROLE_USER
        String access = jwtService.generateToken(user.getUsername(), role);
        return new TokenResponse(access, newRefresh.getToken());
    }
    @PostMapping ("/auth/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request){
        refreshTokenService.revoke(request.refreshToken());
        return ResponseEntity.noContent().build();
    }


}
