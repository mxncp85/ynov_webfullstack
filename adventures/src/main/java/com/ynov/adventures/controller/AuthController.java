package com.ynov.adventures.controller;

import com.ynov.adventures.generated.api.AuthApi;
import com.ynov.adventures.generated.model.AuthResponse;
import com.ynov.adventures.generated.model.LoginInput;
import com.ynov.adventures.generated.model.RegisterInput;
import com.ynov.adventures.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthResponse> register(RegisterInput registerInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerInput));
    }

    @Override
    public ResponseEntity<AuthResponse> login(LoginInput loginInput) {
        return ResponseEntity.ok(authService.login(loginInput));
    }

    @Override
    public ResponseEntity<Void> logout() {
        // JWT stateless : l'invalidation se fait côté client (suppression du token).
        // Pour un vrai blacklist, implémenter un token store (Redis, DB).
        return ResponseEntity.noContent().build();
    }
}
