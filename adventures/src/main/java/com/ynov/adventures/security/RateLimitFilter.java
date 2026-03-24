package com.ynov.adventures.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filtre de rate limiting par IP — 60 requêtes/minute.
 * Adapté du template spring-security-4-template-api-rest.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    public static final int RATE_LIMIT = 60;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(RATE_LIMIT)
                .refillIntervally(RATE_LIMIT, Duration.ofMinutes(1))
                .build();
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {

        String key = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket());

        var probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.setHeader("RateLimit-Limit", String.valueOf(RATE_LIMIT));
            response.setHeader("RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Retry-After", "60");
        response.getWriter().write(
                "{\"type\":\"about:blank\",\"title\":\"Too Many Requests\",\"status\":429," +
                "\"detail\":\"Limite de 60 requêtes/minute atteinte. Réessayez dans 60 secondes.\"}"
        );
    }
}
