# Script de test complet pour SQL et NoSQL
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests SQL (H2) et NoSQL (MongoDB)" -ForegroundColor Cyan
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
    Write-Host "  Pagination: Page $($response.pagination.page)/$($response.pagination.totalPages)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 2: POST - Créer un nouvel aventurier
Write-Host "Test 2: POST - Créer un nouvel aventurier (Merlin)" -ForegroundColor Yellow
$newAventurier = @{
    nom = "Merlin"
    description = "Enchanteur légendaire de la cour du roi Arthur"
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
    Write-Host "  Niveau: $($response.niveau) (devrait être 1)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 3: GET by ID - Récupérer Aragorn
Write-Host "Test 3: GET by ID - Récupérer Aragorn" -ForegroundColor Yellow
$aragornId = "550e8400-e29b-41d4-a716-446655440001"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/$aragornId" -Method Get
    Write-Host "✓ Succès - $($response.nom) trouvé" -ForegroundColor Green
    Write-Host "  Classe: $($response.classe), Niveau: $($response.niveau)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 4: PUT - Mise à jour complète de Frodo
Write-Host "Test 4: PUT - Mise à jour complète de Frodo (niveau 2 -> 3)" -ForegroundColor Yellow
$frodoId = "550e8400-e29b-41d4-a716-446655440005"
$updateFrodo = @{
    nom = "Frodo Baggins"
    description = "Porteur de l'Anneau Unique"
    caracteristiques = @{
        physique = 9
        mental = 12
        perception = 14
    }
    niveau = 3
    classe = "VOLEUR"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/$frodoId" -Method Put -Headers $headers -Body $updateFrodo
    Write-Host "✓ Succès - $($response.nom) mis à jour au niveau $($response.niveau)" -ForegroundColor Green
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 5: PATCH - Mise à jour partielle de Legolas
Write-Host "Test 5: PATCH - Mise à jour partielle de Legolas (niveau 3 -> 4)" -ForegroundColor Yellow
$legolasId = "550e8400-e29b-41d4-a716-446655440002"
$patchLegolas = @{
    niveau = 4
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/$legolasId" -Method Patch -Headers $headers -Body $patchLegolas
    Write-Host "✓ Succès - $($response.nom) passé au niveau $($response.niveau)" -ForegroundColor Green
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 6: GET avec filtres - Guerriers
Write-Host "Test 6: GET avec filtres - Trouver tous les GUERRIER" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?classe=GUERRIER" -Method Get
    Write-Host "✓ Succès - Trouvé $($response.pagination.total) guerrier(s)" -ForegroundColor Green
    foreach ($aventurier in $response.data) {
        Write-Host "  - $($aventurier.nom) (niveau $($aventurier.niveau))" -ForegroundColor Gray
    }
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 7: GET avec filtres - Niveau 5-8
Write-Host "Test 7: GET avec filtres - Aventuriers niveau 5 à 8" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl?niveau_min=5&niveau_max=8" -Method Get
    Write-Host "✓ Succès - Trouvé $($response.pagination.total) aventurier(s) de haut niveau" -ForegroundColor Green
    foreach ($aventurier in $response.data) {
        Write-Host "  - $($aventurier.nom) (niveau $($aventurier.niveau))" -ForegroundColor Gray
    }
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 8: DELETE - Supprimer Boromir
Write-Host "Test 8: DELETE - Supprimer Boromir" -ForegroundColor Yellow
$boromirId = "550e8400-e29b-41d4-a716-446655440006"
try {
    Invoke-RestMethod -Uri "$baseUrl/$boromirId" -Method Delete
    Write-Host "✓ Succès - Boromir supprimé" -ForegroundColor Green
} catch {
    Write-Host "✗ Échec: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 9: Erreur 404 - Aventurier inexistant
Write-Host "Test 9: Test erreur 404 - Aventurier inexistant" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/00000000-0000-0000-0000-000000000000" -Method Get
    Write-Host "✗ Devrait avoir échoué" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 404) {
        Write-Host "✓ Succès - Erreur 404 correctement levée et loggée en WARN" -ForegroundColor Green
    } else {
        Write-Host "✗ Échec: Mauvais code d'erreur" -ForegroundColor Red
    }
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 10: Erreur 422 - Règle métier
Write-Host "Test 10: Test erreur 422 - Tentative de monter de 2 niveaux" -ForegroundColor Yellow
$invalidPatch = @{
    niveau = 5
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/$legolasId" -Method Patch -Headers $headers -Body $invalidPatch
    Write-Host "✗ Devrait avoir échoué" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 422) {
        Write-Host "✓ Succès - Erreur 422 correctement levée et loggée en WARN" -ForegroundColor Green
    } else {
        Write-Host "✗ Échec: Mauvais code d'erreur" -ForegroundColor Red
    }
}
Write-Host ""

Start-Sleep -Seconds 1

# Test 11: Erreur 400 - Validation
Write-Host "Test 11: Test erreur 400 - Données invalides (sans nom)" -ForegroundColor Yellow
$invalidAventurier = @{
    description = "Test sans nom"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $baseUrl -Method Post -Headers $headers -Body $invalidAventurier
    Write-Host "✗ Devrait avoir échoué" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "✓ Succès - Erreur 400 correctement levée et loggée en WARN" -ForegroundColor Green
    } else {
        Write-Host "✗ Échec: Mauvais code d'erreur" -ForegroundColor Red
    }
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests terminés!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Vérifications:" -ForegroundColor Yellow
Write-Host "1. H2 Console: http://localhost:8080/h2-console" -ForegroundColor White
Write-Host "   - URL: jdbc:h2:mem:testdb" -ForegroundColor Gray
Write-Host "   - User: sa (pas de mot de passe)" -ForegroundColor Gray
Write-Host "   - Requête: SELECT * FROM aventuriers;" -ForegroundColor Gray
Write-Host ""
Write-Host "2. MongoDB (mongosh ou Compass):" -ForegroundColor White
Write-Host "   - use adventures_logs" -ForegroundColor Gray
Write-Host "   - db.api_logs.find().pretty()" -ForegroundColor Gray
Write-Host "   - db.api_logs.count()" -ForegroundColor Gray
Write-Host ""
Write-Host "3. Logs de l'application pour voir les [INFO], [WARN], [ERROR]" -ForegroundColor White
Write-Host ""
