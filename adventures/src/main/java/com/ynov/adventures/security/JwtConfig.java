package com.ynov.adventures.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Configuration JWT via Spring Security OAuth2 Resource Server (Nimbus).
 *
 * Approche : JWT signé en HS256 (secret partagé).
 *   - JwtEncoder : signe et génère les tokens (utilisé dans JwtService)
 *   - JwtDecoder : valide les tokens reçus (utilisé automatiquement par le Resource Server)
 *
 * En production : privilégier RSA/ECDSA ou une solution OIDC externe.
 */
@Configuration
public class JwtConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Signe et encode les JWT côté serveur (génération du token après login/register).
     */
    @Bean
    public JwtEncoder jwtEncoder(@Value("${app.jwt.secret}") String secretB64) {
        var key = new SecretKeySpec(Base64.getDecoder().decode(secretB64), "HmacSHA256");
        return NimbusJwtEncoder.withSecretKey(key)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * Valide et décode les JWT reçus en header Authorization.
     * Utilisé automatiquement par le Resource Server Spring Security.
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${app.jwt.secret}") String secretB64) {
        var key = new SecretKeySpec(Base64.getDecoder().decode(secretB64), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
