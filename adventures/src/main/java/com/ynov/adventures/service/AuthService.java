package com.ynov.adventures.service;

import com.ynov.adventures.domain.User;
import com.ynov.adventures.domain.UserRole;
import com.ynov.adventures.exception.UserAlreadyExistsException;
import com.ynov.adventures.generated.model.AuthResponse;
import com.ynov.adventures.generated.model.LoginInput;
import com.ynov.adventures.generated.model.RegisterInput;
import com.ynov.adventures.generated.model.UserInfo;
import com.ynov.adventures.repository.UserRepository;
import com.ynov.adventures.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Crée un compte avec le rôle VIEWER par défaut et retourne un JWT immédiatement.
     */
    public AuthResponse register(RegisterInput input) {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new UserAlreadyExistsException("Un compte avec cet email existe déjà.");
        }
        if (userRepository.existsByUsername(input.getUsername())) {
            throw new UserAlreadyExistsException("Ce nom d'utilisateur est déjà utilisé.");
        }

        User user = User.builder()
                .username(input.getUsername())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(UserRole.VIEWER)
                .build();

        user = userRepository.save(user);

        // Créer un Authentication à partir du User persisté pour générer le JWT
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        return buildAuthResponse(jwtService.generate(auth), user);
    }

    /**
     * Vérifie les credentials (email + mot de passe) et retourne un JWT.
     */
    public AuthResponse login(LoginInput input) {
        // authenticationManager utilise CustomUserDetailsService.loadUserByUsername(email)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + input.getEmail()));

        return buildAuthResponse(jwtService.generate(auth), user);
    }

    private AuthResponse buildAuthResponse(String token, User user) {
        com.ynov.adventures.generated.model.UserRole generatedRole =
                com.ynov.adventures.generated.model.UserRole.valueOf(user.getRole().name());

        UserInfo userInfo = new UserInfo()
                .id(user.getId())
                .username(user.getDisplayUsername())   // nom d'affichage
                .email(user.getEmail())
                .role(generatedRole);

        return new AuthResponse().token(token).user(userInfo);
    }
}
