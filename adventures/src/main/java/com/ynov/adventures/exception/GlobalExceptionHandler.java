package com.ynov.adventures.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions d'aventurier non trouvé
     */
    @ExceptionHandler(AventurierNotFoundException.class)
    public ProblemDetail handleAventurierNotFound(AventurierNotFoundException ex) {
        log.warn("Aventurier non trouvé: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Aventurier non trouvé");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("about:blank"));

        return problemDetail;
    }

    /**
     * Gère les exceptions métier
     */
    @ExceptionHandler(AventurierBusinessException.class)
    public ProblemDetail handleAventurierBusinessException(AventurierBusinessException ex) {
        log.warn("Erreur métier: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Erreur de validation métier");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("about:blank"));

        return problemDetail;
    }

    /**
     * Gère les erreurs de validation de requête
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Erreur de validation: {}", ex.getBindingResult());

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing + "; " + replacement
                ));

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Erreur de validation");
        problemDetail.setDetail("Les données fournies sont invalides");
        problemDetail.setProperty("fieldErrors", fieldErrors);
        problemDetail.setType(URI.create("about:blank"));

        return problemDetail;
    }

    /**
     * Gère les exceptions génériques
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("Erreur serveur interne", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Erreur serveur interne");
        problemDetail.setDetail("Une erreur interne est survenue");
        problemDetail.setType(URI.create("about:blank"));

        return problemDetail;
    }
}
