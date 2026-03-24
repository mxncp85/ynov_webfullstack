package com.ynov.adventures.security;

/**
 * Remplacé par le Resource Server Spring Security OAuth2.
 *
 * La validation du token JWT Bearer est désormais entièrement gérée par :
 *   - JwtDecoder (JwtConfig) — vérifie signature, expiration
 *   - JwtAuthenticationConverter (SecurityConfig) — extrait le claim "scope" comme autorités
 *   - .oauth2ResourceServer(oauth2 -> oauth2.jwt(...)) dans SecurityConfig
 *
 * Ce fichier est conservé pour ne pas casser d'éventuelles références, mais n'est plus un @Component.
 */
class JwtAuthenticationFilter {
    // No-op — voir SecurityConfig.securityFilterChain()
}
