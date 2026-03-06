# Script de test complet - Version H2 uniquement
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests API avec H2 (SQL + Logs)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api/v1/aventuriers"
$headers = @{
    "Content-Type" = "application/json"
}

# Test 1: GET - Liste tous les aventuriers
Write-Host "Test 1: GET - Liste tous les aventuriers" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?page=1&limit=10" -Method Get
    Write-Host "✓ Succès - Trouvé $($response.pagination.total) aventuriers" -ForegroundColor Green
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""
Start-Sleep -Seconds 1

# Test 2: POST - Créer un nouvel aventurier
Write-Host "Test 2: POST - Créer un aventurier (Merlin)" -ForegroundColor Yellow
$newAventurier = @{
    nom = "Merlin"
    description = "Enchanteur légendaire"
    caracteristiques = @{
        physique = 10
        mental = 20
        perception = 18
    }
    classe = "MAGE"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method Post -Headers $headers -Body $newAventurier
    $merlinId = $response.id
    Write-Host "✓ Succès - Merlin créé avec ID: $merlinId" -ForegroundColor Green
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""
Start-Sleep -Seconds 1

# Test 3: GET by ID
Write-Host "Test 3: GET by ID - Récupérer Aragorn" -ForegroundColor Yellow
$aragornId = "550e8400-e29b-41d4-a716-446655440001"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/$aragornId" -Method Get
    Write-Host "✓ Succès - $($response.nom) trouvé" -ForegroundColor Green
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""
Start-Sleep -Seconds 1

# Test 4: PATCH
Write-Host "Test 4: PATCH - Monter Legolas au niveau 4" -ForegroundColor Yellow
$legolasId = "550e8400-e29b-41d4-a716-446655440002"
$patchLegolas = @{ niveau = 4 } | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/$legolasId" -Method Patch -Headers $headers -Body $patchLegolas
    Write-Host "✓ Succès - Legolas niveau $($response.niveau)" -ForegroundColor Green
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""
Start-Sleep -Seconds 1

# Test 5: Erreur 404
Write-Host "Test 5: Test erreur 404 - Aventurier inexistant" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/00000000-0000-0000-0000-000000000000" -Method Get
    Write-Host "✗ Devrait avoir échoué" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "✓ Succès - Erreur 404 correctement levée (loggée en WARN)" -ForegroundColor Green
    } else {
        Write-Host "✗ Échec: Mauvais code d'erreur" -ForegroundColor Red
    }
}
Write-Host ""
Start-Sleep -Seconds 1

# Test 6: Erreur 422
Write-Host "Test 6: Test erreur 422 - Monter de 2 niveaux" -ForegroundColor Yellow
$invalidPatch = @{ niveau = 6 } | ConvertTo-Json
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/$legolasId" -Method Patch -Headers $headers -Body $invalidPatch
    Write-Host "✗ Devrait avoir échoué" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 422) {
        Write-Host "✓ Succès - Erreur 422 correctement levée (loggée en WARN)" -ForegroundColor Green
    } else {
        Write-Host "✗ Échec: Mauvais code d'erreur" -ForegroundColor Red
    }
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests terminés!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Vérifications dans H2 Console:" -ForegroundColor Yellow
Write-Host "1. Ouvrir: http://localhost:8080/h2-console" -ForegroundColor White
Write-Host "   - JDBC URL: jdbc:h2:mem:testdb" -ForegroundColor Gray
Write-Host "   - Username: sa (pas de password)" -ForegroundColor Gray
Write-Host ""
Write-Host "2. Vérifier les aventuriers:" -ForegroundColor White
Write-Host "   SELECT * FROM aventuriers;" -ForegroundColor Gray
Write-Host ""
Write-Host "3. Vérifier les logs d'API:" -ForegroundColor White
Write-Host "   SELECT * FROM api_logs ORDER BY timestamp DESC LIMIT 20;" -ForegroundColor Gray
Write-Host ""
Write-Host "4. Statistiques des logs:" -ForegroundColor White
Write-Host "   SELECT log_level, COUNT(*) FROM api_logs GROUP BY log_level;" -ForegroundColor Gray
Write-Host ""
Write-Host "5. Logs d'erreurs:" -ForegroundColor White
Write-Host "   SELECT * FROM api_logs WHERE success = false;" -ForegroundColor Gray
Write-Host ""
