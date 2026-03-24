package com.ynov.adventures.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration Spring Security — adapté du template spring-security-4-template-api-rest.
 *
 * Principes :
 *   - API REST stateless (pas de session, pas de CSRF)
 *   - Authentification par JWT Bearer (OAuth2 Resource Server)
 *   - /auth/register et /auth/login sont publics
 *   - Tous les autres endpoints exigent un token valide
 *   - Les rôles (VIEWER / ADMIN) sont gérés par @PreAuthorize dans les controllers
 *   - Rate limiting via RateLimitFilter (60 req/min/IP)
 *   - CORS configuré pour le front (localhost:3000 / localhost:5173)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS selon le bean corsConfigurationSource()
                .cors(Customizer.withDefaults())

                // Pas de CSRF (API stateless sans session cookie)
                .csrf(AbstractHttpConfigurer::disable)

                // H2 Console nécessite des iframes (même origine)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                // Stateless : chaque requête doit apporter son token
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Préflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Endpoints publics (pas de token requis)
                        .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()
                        // Console H2 (dev uniquement)
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        // Tout le reste requiert un token valide
                        // Les rôles spécifiques sont gérés par @PreAuthorize
                        .anyRequest().authenticated()
                )

                // Resource Server JWT : lit "Authorization: Bearer <token>"
                // et délègue la validation au JwtDecoder (voir JwtConfig)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
                        // 401 : token absent ou invalide/expiré
                        .authenticationEntryPoint((request, response, ex) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(
                                    "{\"type\":\"about:blank\",\"title\":\"Non authentifié\",\"status\":401," +
                                    "\"detail\":\"Token JWT absent ou expiré. Connectez-vous via POST /auth/login.\"}"
                            );
                        })
                        // 403 : token valide mais rôle insuffisant
                        .accessDeniedHandler((request, response, ex) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(
                                    "{\"type\":\"about:blank\",\"title\":\"Accès refusé\",\"status\":403," +
                                    "\"detail\":\"Le rôle ADMIN est requis pour effectuer cette action.\"}"
                            );
                        })
                );

        return http.build();
    }

    /**
     * Convertit le claim "scope" du JWT en autorités Spring Security.
     *
     * Le scope contient "ROLE_VIEWER" ou "ROLE_ADMIN" (stocké tel quel à la génération).
     * setAuthorityPrefix("") : on garde le préfixe ROLE_ déjà présent.
     * hasRole('ADMIN') → vérifie "ROLE_ADMIN" → correspond au scope "ROLE_ADMIN" ✓
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {
        JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
        gac.setAuthoritiesClaimName("scope");
        gac.setAuthorityPrefix(""); // "ROLE_VIEWER" / "ROLE_ADMIN" déjà préfixés

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(gac);
        return converter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * CORS — autorise le front (localhost:3000 / localhost:5173).
     * En production : remplacer par le domaine réel.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        cfg.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
