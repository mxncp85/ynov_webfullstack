package com.ynov.adventures.document;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(length = 10)
    private String method;
    
    @Column(length = 500)
    private String endpoint;
    
    @Column(length = 500)
    private String uri;
    
    @Column(length = 2000)
    private String pathVariables;
    
    @Column(length = 2000)
    private String requestParams;
    
    @Column(columnDefinition = "TEXT")
    private String requestBody;
    
    @Column(columnDefinition = "TEXT")
    private String responseBody;
    
    private Integer statusCode;
    
    @Column(length = 10)
    private String logLevel;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    private Long executionTimeMs;
    
    @Column(nullable = false)
    private Boolean success;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
