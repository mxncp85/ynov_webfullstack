#!/bin/bash

# Script de test de l'API des Aventuriers
# Exécution : bash test_api.sh

BASE_URL="http://localhost:8080/api/v1/aventuriers"
CREATED_ID=""

echo "═══════════════════════════════════════════════════════════════════"
echo "API DES AVENTURIERS - SCRIPT DE TEST"
echo "═══════════════════════════════════════════════════════════════════"
echo ""

# Couleurs pour les messages
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Fonction pour afficher les résultats
test_result() {
    if [ $1 -eq $2 ]; then
        echo -e "${GREEN}✅ SUCCÈS${NC} - Code: $1 (attendu: $2)"
    else
        echo -e "${RED}❌ ERREUR${NC} - Code: $1 (attendu: $2)"
    fi
    echo ""
}

# Test 1 : GET Liste (sans données)
echo -e "${YELLOW}[TEST 1]${NC} Récupérer la liste des aventuriers"
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL?page=1&limit=20")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)
echo "Réponse: $BODY"
test_result $HTTP_CODE 200

# Test 2 : POST Créer un aventurier
echo -e "${YELLOW}[TEST 2]${NC} Créer un nouvel aventurier"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Aragorn Test",
    "description": "Rôdeur des terres sauvages",
    "caracteristiques": {
      "physique": 17,
      "mental": 14,
      "perception": 16
    },
    "niveau": 1,
    "classe": "RODEUR"
  }')
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)
echo "Réponse: $BODY"
test_result $HTTP_CODE 201

# Extraire l'ID pour les tests suivants
CREATED_ID=$(echo "$BODY" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "ID créé pour les tests suivants: ${GREEN}$CREATED_ID${NC}"
echo ""

# Test 3 : GET par ID
if [ -n "$CREATED_ID" ]; then
    echo -e "${YELLOW}[TEST 3]${NC} Récupérer l'aventurier par ID"
    RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/$CREATED_ID")
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | head -n-1)
    echo "Réponse: $BODY"
    test_result $HTTP_CODE 200
fi

# Test 4 : POST Créer avec niveau != 1 (ERREUR 422)
echo -e "${YELLOW}[TEST 4]${NC} Essayer de créer avec niveau != 1 (doit échouer)"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Gandalf",
    "caracteristiques": {
      "physique": 10,
      "mental": 18,
      "perception": 15
    },
    "niveau": 5,
    "classe": "MAGE"
  }')
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)
echo "Réponse: $BODY"
test_result $HTTP_CODE 422

# Test 5 : POST Créer avec données invalides (ERREUR 400)
echo -e "${YELLOW}[TEST 5]${NC} Créer avec données invalides (nom trop court)"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "A",
    "caracteristiques": {
      "physique": 10,
      "mental": 10,
      "perception": 10
    },
    "niveau": 1,
    "classe": "GUERRIER"
  }')
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)
echo "Réponse: $BODY"
test_result $HTTP_CODE 400

# Test 6 : PUT Augmenter le niveau de 1 (succès)
if [ -n "$CREATED_ID" ]; then
    echo -e "${YELLOW}[TEST 6]${NC} Augmenter le niveau de 1"
    RESPONSE=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/$CREATED_ID" \
      -H "Content-Type: application/json" \
      -d '{
        "nom": "Aragorn Test",
        "description": "Rôdeur des terres sauvages",
        "caracteristiques": {
          "physique": 17,
          "mental": 14,
          "perception": 16
        },
        "niveau": 2,
        "classe": "RODEUR"
      }')
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | head -n-1)
    echo "Réponse: $BODY"
    test_result $HTTP_CODE 200
fi

# Test 7 : PUT Baisser le niveau (ERREUR 422)
if [ -n "$CREATED_ID" ]; then
    echo -e "${YELLOW}[TEST 7]${NC} Essayer de baisser le niveau (doit échouer)"
    RESPONSE=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/$CREATED_ID" \
      -H "Content-Type: application/json" \
      -d '{
        "nom": "Aragorn Test",
        "caracteristiques": {
          "physique": 17,
          "mental": 14,
          "perception": 16
        },
        "niveau": 1,
        "classe": "RODEUR"
      }')
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | head -n-1)
    echo "Réponse: $BODY"
    test_result $HTTP_CODE 422
fi

# Test 8 : PUT Augmenter de plus de 1 (ERREUR 422)
if [ -n "$CREATED_ID" ]; then
    echo -e "${YELLOW}[TEST 8]${NC} Essayer d'augmenter de plus de 1 niveau (doit échouer)"
    RESPONSE=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/$CREATED_ID" \
      -H "Content-Type: application/json" \
      -d '{
        "nom": "Aragorn Test",
        "caracteristiques": {
          "physique": 17,
          "mental": 14,
          "perception": 16
        },
        "niveau": 5,
        "classe": "RODEUR"
      }')
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | head -n-1)
    echo "Réponse: $BODY"
    test_result $HTTP_CODE 422
fi

# Test 9 : PATCH Augmenter le niveau de 1
if [ -n "$CREATED_ID" ]; then
    echo -e "${YELLOW}[TEST 9]${NC} PATCH - Augmenter le niveau de 1"
    RESPONSE=$(curl -s -w "\n%{http_code}" -X PATCH "$BASE_URL/$CREATED_ID" \
      -H "Content-Type: application/json" \
      -d '{
        "niveau": 3
      }')
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | head -n-1)
    echo "Réponse: $BODY"
    test_result $HTTP_CODE 200
fi

# Test 10 : PATCH Modifier d'autres champs
if [ -n "$CREATED_ID" ]; then
    echo -e "${YELLOW}[TEST 10]${NC} PATCH - Modifier nom et description"
    RESPONSE=$(curl -s -w "\n%{http_code}" -X PATCH "$BASE_URL/$CREATED_ID" \
      -H "Content-Type: application/json" \
      -d '{
        "nom": "Aragorn le Roi",
        "description": "Le Roi qui a réuni les royaumes"
      }')
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | head -n-1)
    echo "Réponse: $BODY"
    test_result $HTTP_CODE 200
fi

# Test 11 : GET ID inexistant (ERREUR 404)
echo -e "${YELLOW}[TEST 11]${NC} Récupérer un aventurier inexistant (404)"
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/00000000-0000-0000-0000-000000000000")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)
echo "Réponse: $BODY"
test_result $HTTP_CODE 404

# Test 12 : DELETE
if [ -n "$CREATED_ID" ]; then
    echo -e "${YELLOW}[TEST 12]${NC} Supprimer l'aventurier"
    RESPONSE=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/$CREATED_ID")
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    test_result $HTTP_CODE 204
fi

# Test 13 : DELETE ID inexistant (ERREUR 404)
echo -e "${YELLOW}[TEST 13]${NC} Supprimer un aventurier inexistant (404)"
RESPONSE=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/00000000-0000-0000-0000-000000000000")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
test_result $HTTP_CODE 404

# Test 14 : GET Liste avec filtres
echo -e "${YELLOW}[TEST 14]${NC} Lister avec filtres (classe=GUERRIER, niveau 1-5)"
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL?classe=GUERRIER&niveau_min=1&niveau_max=5")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)
echo "Réponse: $BODY"
test_result $HTTP_CODE 200

echo -e "${GREEN}═══════════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}Tests terminés !${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════════════════${NC}"
