package com.ynov.adventures.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.adventures.document.ApiLog;
import com.ynov.adventures.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;
    private final ObjectMapper objectMapper;

    /**
     * Sauvegarde un log d'API de manière asynchrone dans H2
     */
    @Async
    public void logApiCall(
            String method,
            String endpoint,
            String uri,
            Map<String, String> pathVariables,
            Map<String, String> requestParams,
            Object requestBody,
            Object responseBody,
            Integer statusCode,
            String logLevel,
            String errorMessage,
            Long executionTimeMs,
            Boolean success
    ) {
        try {
            // Log dans la console
            String logMessage = String.format("[%s] %s %s - Status: %d - Time: %dms - Success: %b",
                    logLevel, method, uri, statusCode, executionTimeMs, success);
            
            switch (logLevel.toUpperCase()) {
                case "ERROR":
                    log.error(logMessage + (errorMessage != null ? " - Error: " + errorMessage : ""));
                    break;
                case "WARN":
                    log.warn(logMessage + (errorMessage != null ? " - Error: " + errorMessage : ""));
                    break;
                default:
                    log.info(logMessage);
            }
            
            // Sauvegarder dans H2
            ApiLog apiLog = ApiLog.builder()
                    .timestamp(LocalDateTime.now())
                    .method(method)
                    .endpoint(endpoint)
                    .uri(uri)
                    .pathVariables(mapToString(pathVariables))
                    .requestParams(mapToString(requestParams))
                    .requestBody(objectToString(requestBody))
                    .responseBody(objectToString(responseBody))
                    .statusCode(statusCode)
                    .logLevel(logLevel)
                    .errorMessage(errorMessage)
                    .executionTimeMs(executionTimeMs)
                    .success(success)
                    .build();

            apiLogRepository.save(apiLog);
            
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde du log API dans H2: ", e);
        }
    }

    private String mapToString(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            return map.toString();
        }
    }

    private String objectToString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}
