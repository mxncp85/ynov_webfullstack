package com.ynov.adventures.repository;

import com.ynov.adventures.document.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
    
    /**
     * Trouve les logs par endpoint
     */
    List<ApiLog> findByEndpoint(String endpoint);
    
    /**
     * Trouve les logs par niveau de log
     */
    List<ApiLog> findByLogLevel(String logLevel);
    
    /**
     * Trouve les logs dans une plage de dates
     */
    List<ApiLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Trouve les logs d'erreur (success = false)
     */
    List<ApiLog> findBySuccessFalse();
}
