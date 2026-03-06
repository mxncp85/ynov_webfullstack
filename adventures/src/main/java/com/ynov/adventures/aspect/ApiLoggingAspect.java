package com.ynov.adventures.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.adventures.exception.AventurierBusinessException;
import com.ynov.adventures.exception.AventurierNotFoundException;
import com.ynov.adventures.service.ApiLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiLoggingAspect {

    private final ApiLogService apiLogService;
    private final ObjectMapper objectMapper;

    @Around("execution(* com.ynov.adventures.controller..*(..))")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String endpoint = extractEndpointPattern(joinPoint);
        
        // Extraire les paramètres
        Map<String, String> pathVariables = extractPathVariables(joinPoint);
        Map<String, String> requestParams = extractRequestParams(request);
        Object requestBody = extractRequestBody(joinPoint);
        
        Object result = null;
        Object responseBody = null;
        Integer statusCode = null;
        String logLevel = "INFO";
        String errorMessage = null;
        Boolean success = true;
        
        try {
            result = joinPoint.proceed();
            
            // Extraire la réponse
            if (result instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                statusCode = responseEntity.getStatusCode().value();
                responseBody = responseEntity.getBody();
                
                // Déterminer le niveau de log en fonction du status code
                if (statusCode >= 500) {
                    logLevel = "ERROR";
                    success = false;
                } else if (statusCode >= 400) {
                    logLevel = "WARN";
                    success = false;
                }
            } else {
                statusCode = 200;
                responseBody = result;
            }
            
            return result;
            
        } catch (AventurierNotFoundException e) {
            // Erreur client - 404
            statusCode = 404;
            logLevel = "WARN";
            errorMessage = e.getMessage();
            success = false;
            throw e;
            
        } catch (AventurierBusinessException e) {
            // Erreur client - 422
            statusCode = 422;
            logLevel = "WARN";
            errorMessage = e.getMessage();
            success = false;
            throw e;
            
        } catch (MethodArgumentNotValidException e) {
            // Erreur client - 400
            statusCode = 400;
            logLevel = "WARN";
            errorMessage = "Validation error: " + e.getMessage();
            success = false;
            throw e;
            
        } catch (Exception e) {
            // Erreur serveur - 500
            statusCode = 500;
            logLevel = "ERROR";
            errorMessage = e.getMessage();
            success = false;
            throw e;
            
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Logger de manière asynchrone
            apiLogService.logApiCall(
                    method,
                    endpoint,
                    uri,
                    pathVariables,
                    requestParams,
                    requestBody,
                    responseBody,
                    statusCode,
                    logLevel,
                    errorMessage,
                    executionTime,
                    success
            );
        }
    }

    private String extractEndpointPattern(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // Chercher les annotations de mapping
        String pattern = "";
        Class<?> clazz = joinPoint.getTarget().getClass();
        
        // Pattern de la classe
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
            if (classMapping.value().length > 0) {
                pattern = classMapping.value()[0];
            }
        }
        
        // Pattern de la méthode
        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            pattern += mapping.value().length > 0 ? mapping.value()[0] : "";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping mapping = method.getAnnotation(PostMapping.class);
            pattern += mapping.value().length > 0 ? mapping.value()[0] : "";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            PutMapping mapping = method.getAnnotation(PutMapping.class);
            pattern += mapping.value().length > 0 ? mapping.value()[0] : "";
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            PatchMapping mapping = method.getAnnotation(PatchMapping.class);
            pattern += mapping.value().length > 0 ? mapping.value()[0] : "";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
            pattern += mapping.value().length > 0 ? mapping.value()[0] : "";
        }
        
        return pattern;
    }

    private Map<String, String> extractPathVariables(ProceedingJoinPoint joinPoint) {
        Map<String, String> pathVariables = new HashMap<>();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        
        for (int i = 0; i < method.getParameters().length; i++) {
            if (method.getParameters()[i].isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = method.getParameters()[i].getAnnotation(PathVariable.class);
                String paramName = pathVariable.value().isEmpty() ? method.getParameters()[i].getName() : pathVariable.value();
                pathVariables.put(paramName, args[i] != null ? args[i].toString() : null);
            }
        }
        
        return pathVariables;
    }

    private Map<String, String> extractRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            }
        });
        return params;
    }

    private Object extractRequestBody(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        
        for (int i = 0; i < method.getParameters().length; i++) {
            if (method.getParameters()[i].isAnnotationPresent(RequestBody.class)) {
                return args[i];
            }
        }
        
        return null;
    }
}
