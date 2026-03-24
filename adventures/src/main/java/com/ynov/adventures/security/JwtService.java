package com.ynov.adventures.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

/**
 * Génère les tokens JWT après authentification.
 *
 * Le token contient :
 *   - subject  : email de l'utilisateur (principal Spring Security)
 *   - scope    : rôle complet ("ROLE_VIEWER" ou "ROLE_ADMIN")
 *   - issuer   : identifiant de l'application
 *   - issuedAt / expiresAt : durée de validité
 *
 * La validation des tokens reçus est entièrement gérée par
 * le Resource Server Spring Security via JwtDecoder (voir SecurityConfig).
 */
@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final String issuer;
    private final long expMinutes;

    public JwtService(
            JwtEncoder encoder,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.exp-minutes}") long expMinutes
    ) {
        this.encoder = encoder;
        this.issuer = issuer;
        this.expMinutes = expMinutes;
    }

    /**
     * Génère un JWT signé pour un utilisateur déjà authentifié.
     *
     * @param auth résultat de l'authentification Spring Security
     * @return token JWT signé (String)
     */
    public String generate(Authentication auth) {
        Instant now = Instant.now();

        // On joint les autorités en une seule chaîne dans le claim "scope"
        // ex: "ROLE_ADMIN" ou "ROLE_VIEWER"
        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(expMinutes, ChronoUnit.MINUTES))
                .subject(auth.getName())     // = email
                .claim("scope", scope)       // = "ROLE_VIEWER" ou "ROLE_ADMIN"
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
