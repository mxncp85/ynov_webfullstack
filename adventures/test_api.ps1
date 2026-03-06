# Script de test de l'API des Aventuriers - Version PowerShell
# Exécution : powershell -ExecutionPolicy Bypass -File test_api.ps1

$BaseUrl = "http://localhost:8080/api/v1/aventuriers"
$CreatedId = ""

Write-Host "═══════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "API DES AVENTURIERS - SCRIPT DE TEST" -ForegroundColor Cyan
Write-Host "═══════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

function Test-Result {
    param(
        [int]$ActualCode,
        [int]$ExpectedCode
    )
    if ($ActualCode -eq $ExpectedCode) {
        Write-Host "✅ SUCCÈS - Code: $ActualCode (attendu: $ExpectedCode)" -ForegroundColor Green
    } else {
        Write-Host "❌ ERREUR - Code: $ActualCode (attendu: $ExpectedCode)" -ForegroundColor Red
    }
    Write-Host ""
}

# Test 1 : GET Liste
Write-Host "[TEST 1]" -ForegroundColor Yellow " Récupérer la liste des aventuriers"
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl`?page=1&limit=20" -Method Get -ErrorAction SilentlyContinue
    Write-Host "Réponse: $($response.Content)"
    Test-Result $response.StatusCode 200
} catch {
    Write-Host "Erreur lors de la requête: $_" -ForegroundColor Red
}

# Test 2 : POST Créer un aventurier
Write-Host "[TEST 2]" -ForegroundColor Yellow " Créer un nouvel aventurier"
$body = @{
    nom = "Aragorn Test"
    description = "Rôdeur des terres sauvages"
    caracteristiques = @{
        physique = 17
        mental = 14
        perception = 16
    }
    niveau = 1
    classe = "RODEUR"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri $BaseUrl -Method Post `
        -ContentType "application/json" `
        -Body $body `
        -ErrorAction SilentlyContinue
    Write-Host "Réponse: $($response.Content)"
    Test-Result $response.StatusCode 201
    
    # Extraire l'ID
    $responseObj = $response.Content | ConvertFrom-Json
    $CreatedId = $responseObj.id
    Write-Host "ID créé pour les tests suivants: $CreatedId" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "Erreur lors de la requête: $_" -ForegroundColor Red
}

# Test 3 : GET par ID
if ($CreatedId) {
    Write-Host "[TEST 3]" -ForegroundColor Yellow " Récupérer l'aventurier par ID"
    try {
        $response = Invoke-WebRequest -Uri "$BaseUrl/$CreatedId" -Method Get -ErrorAction SilentlyContinue
        Write-Host "Réponse: $($response.Content)"
        Test-Result $response.StatusCode 200
    } catch {
        Write-Host "Erreur lors de la requête: $_" -ForegroundColor Red
    }
}

# Test 4 : POST Créer avec niveau != 1 (ERREUR 422)
Write-Host "[TEST 4]" -ForegroundColor Yellow " Essayer de créer avec niveau != 1 (doit échouer)"
$body = @{
    nom = "Gandalf"
    caracteristiques = @{
        physique = 10
        mental = 18
        perception = 15
    }
    niveau = 5
    classe = "MAGE"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri $BaseUrl -Method Post `
        -ContentType "application/json" `
        -Body $body `
        -ErrorAction Continue
    Write-Host "Réponse: $($response.Content)"
    Test-Result $response.StatusCode 422
} catch {
    $statusCode = $_.Exception.Response.StatusCode.Value__
    Write-Host "Réponse: $($_.Exception.Response.StatusCode)"
    Test-Result $statusCode 422
}

# Test 5 : POST Créer avec données invalides (ERREUR 400)
Write-Host "[TEST 5]" -ForegroundColor Yellow " Créer avec données invalides (nom trop court)"
$body = @{
    nom = "A"
    caracteristiques = @{
        physique = 10
        mental = 10
        perception = 10
    }
    niveau = 1
    classe = "GUERRIER"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri $BaseUrl -Method Post `
        -ContentType "application/json" `
        -Body $body `
        -ErrorAction Continue
    Write-Host "Réponse: $($response.Content)"
    Test-Result $response.StatusCode 400
} catch {
    $statusCode = $_.Exception.Response.StatusCode.Value__
    Write-Host "Réponse: $($_.Exception.Response.StatusCode)"
    Test-Result $statusCode 400
}

# Test 6 : PUT Augmenter le niveau de 1 (succès)
if ($CreatedId) {
    Write-Host "[TEST 6]" -ForegroundColor Yellow " Augmenter le niveau de 1"
    $body = @{
        nom = "Aragorn Test"
        description = "Rôdeur des terres sauvages"
        caracteristiques = @{
            physique = 17
            mental = 14
            perception = 16
        }
        niveau = 2
        classe = "RODEUR"
    } | ConvertTo-Json

    try {
        $response = Invoke-WebRequest -Uri "$BaseUrl/$CreatedId" -Method Put `
            -ContentType "application/json" `
            -Body $body `
            -ErrorAction SilentlyContinue
        Write-Host "Réponse: $($response.Content)"
        Test-Result $response.StatusCode 200
    } catch {
        Write-Host "Erreur lors de la requête: $_" -ForegroundColor Red
    }
}

# Test 7 : PUT Baisser le niveau (ERREUR 422)
if ($CreatedId) {
    Write-Host "[TEST 7]" -ForegroundColor Yellow " Essayer de baisser le niveau (doit échouer)"
    $body = @{
        nom = "Aragorn Test"
        caracteristiques = @{
            physique = 17
            mental = 14
            perception = 16
        }
        niveau = 1
        classe = "RODEUR"
    } | ConvertTo-Json

    try {
        $response = Invoke-WebRequest -Uri "$BaseUrl/$CreatedId" -Method Put `
            -ContentType "application/json" `
            -Body $body `
            -ErrorAction Continue
        Write-Host "Réponse: $($response.Content)"
        Test-Result $response.StatusCode 422
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.Value__
        Write-Host "Réponse: $($_.Exception.Response.StatusCode)"
        Test-Result $statusCode 422
    }
}

# Test 8 : PUT Augmenter de plus de 1 (ERREUR 422)
if ($CreatedId) {
    Write-Host "[TEST 8]" -ForegroundColor Yellow " Essayer d'augmenter de plus de 1 niveau (doit échouer)"
    $body = @{
        nom = "Aragorn Test"
        caracteristiques = @{
            physique = 17
            mental = 14
            perception = 16
        }
        niveau = 5
        classe = "RODEUR"
    } | ConvertTo-Json

    try {
        $response = Invoke-WebRequest -Uri "$BaseUrl/$CreatedId" -Method Put `
            -ContentType "application/json" `
            -Body $body `
            -ErrorAction Continue
        Write-Host "Réponse: $($response.Content)"
        Test-Result $response.StatusCode 422
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.Value__
        Write-Host "Réponse: $($_.Exception.Response.StatusCode)"
        Test-Result $statusCode 422
    }
}

# Test 9 : PATCH Augmenter le niveau de 1
if ($CreatedId) {
    Write-Host "[TEST 9]" -ForegroundColor Yellow " PATCH - Augmenter le niveau de 1"
    $body = @{
        niveau = 3
    } | ConvertTo-Json

    try {
        $response = Invoke-WebRequest -Uri "$BaseUrl/$CreatedId" -Method Patch `
            -ContentType "application/json" `
            -Body $body `
            -ErrorAction SilentlyContinue
        Write-Host "Réponse: $($response.Content)"
        Test-Result $response.StatusCode 200
    } catch {
        Write-Host "Erreur lors de la requête: $_" -ForegroundColor Red
    }
}

# Test 10 : PATCH Modifier d'autres champs
if ($CreatedId) {
    Write-Host "[TEST 10]" -ForegroundColor Yellow " PATCH - Modifier nom et description"
    $body = @{
        nom = "Aragorn le Roi"
        description = "Le Roi qui a réuni les royaumes"
    } | ConvertTo-Json

    try {
        $response = Invoke-WebRequest -Uri "$BaseUrl/$CreatedId" -Method Patch `
            -ContentType "application/json" `
            -Body $body `
            -ErrorAction SilentlyContinue
        Write-Host "Réponse: $($response.Content)"
        Test-Result $response.StatusCode 200
    } catch {
        Write-Host "Erreur lors de la requête: $_" -ForegroundColor Red
    }
}

# Test 11 : GET ID inexistant (ERREUR 404)
Write-Host "[TEST 11]" -ForegroundColor Yellow " Récupérer un aventurier inexistant (404)"
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/00000000-0000-0000-0000-000000000000" -Method Get -ErrorAction Continue
    Write-Host "Réponse: $($response.Content)"
    Test-Result $response.StatusCode 404
} catch {
    $statusCode = $_.Exception.Response.StatusCode.Value__
    Write-Host "Réponse: $($_.Exception.Response.StatusCode)"
    Test-Result $statusCode 404
}

# Test 12 : DELETE
if ($CreatedId) {
    Write-Host "[TEST 12]" -ForegroundColor Yellow " Supprimer l'aventurier"
    try {
        $response = Invoke-WebRequest -Uri "$BaseUrl/$CreatedId" -Method Delete -ErrorAction SilentlyContinue
        Test-Result $response.StatusCode 204
    } catch {
        Write-Host "Erreur lors de la requête: $_" -ForegroundColor Red
    }
}

# Test 13 : DELETE ID inexistant (ERREUR 404)
Write-Host "[TEST 13]" -ForegroundColor Yellow " Supprimer un aventurier inexistant (404)"
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/00000000-0000-0000-0000-000000000000" -Method Delete -ErrorAction Continue
    Test-Result $response.StatusCode 404
} catch {
    $statusCode = $_.Exception.Response.StatusCode.Value__
    Test-Result $statusCode 404
}

# Test 14 : GET Liste avec filtres
Write-Host "[TEST 14]" -ForegroundColor Yellow " Lister avec filtres (classe=GUERRIER, niveau 1-5)"
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl`?classe=GUERRIER&niveau_min=1&niveau_max=5" -Method Get -ErrorAction SilentlyContinue
    Write-Host "Réponse: $($response.Content)"
    Test-Result $response.StatusCode 200
} catch {
    Write-Host "Erreur lors de la requête: $_" -ForegroundColor Red
}

Write-Host "═══════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "Tests terminés !" -ForegroundColor Green
Write-Host "═══════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
