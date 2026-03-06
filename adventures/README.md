# 🏰 API des Aventuriers - Système de Gestion CRUD

Une API REST complète et professionnelle pour la gestion des aventuriers dans un univers de fantasy, construite avec Spring Boot 4.0.3 et Java 25.

## 🎯 Aperçu

Cette implémentation fournit :

- ✅ **API REST CRUD complète** avec 6 endpoints
- ✅ **Contrat OpenAPI 3.0.3** détaillé et complet
- ✅ **Règles métier appliquées** au niveau service
- ✅ **Validation des données** à plusieurs niveaux
- ✅ **Gestion des erreurs** avec ProblemDetail
- ✅ **Pagination et filtrage** des résultats
- ✅ **Tests unitaires** couvrant les cas critiques
- ✅ **Documentation complète** avec exemples

---

## 📋 Règles Métier

### 1. Création
Un aventurier **doit** commencer au **niveau 1**
```bash
# ✅ Valide
POST /api/v1/aventuriers
{ "nom": "Aragorn", "niveau": 1, ... }
→ 201 Created

# ❌ Invalid - niveau ≠ 1
POST /api/v1/aventuriers
{ "nom": "Gandalf", "niveau": 5, ... }
→ 422 Unprocessable Entity
```

### 2. Progression de Niveau
- Le niveau **ne peut jamais descendre**
- Le niveau **ne peut monter de plus de 1 à la fois**

```bash
# ✅ Valide (niveau 3 → 4)
PATCH /api/v1/aventuriers/{id}
{ "niveau": 4 }
→ 200 OK

# ❌ Invalid (niveau 3 → 2)
{ "niveau": 2 }
→ 422 Unprocessable Entity

# ❌ Invalid (niveau 3 → 6)
{ "niveau": 6 }
→ 422 Unprocessable Entity
```

---

## 🚀 Démarrage Rapide

### 1. Compiler
```bash
mvn clean install
```

### 2. Démarrer
```bash
mvn spring-boot:run
```

### 3. Tester
```bash
curl http://localhost:8080/api/v1/aventuriers
```

**Voir [QUICKSTART.md](QUICKSTART.md) pour plus de détails.**

---

## 📚 Documentation

| Document | Contenu |
|----------|---------|
| **[QUICKSTART.md](QUICKSTART.md)** | 🚀 Guide de démarrage rapide |
| **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** | 📖 Documentation complète de l'API |
| **[CURL_EXAMPLES.md](CURL_EXAMPLES.md)** | 💡 Exemples de requêtes pratiques |
| **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** | 🏗️ Vue d'ensemble technique |
| **[CHECKLIST.md](CHECKLIST.md)** | ✅ Vérification de l'implémentation |
| **[openapi.yaml](openapi.yaml)** | 📄 Contrat OpenAPI complet |

---

## 🏗️ Architecture

```
Controller (REST Endpoints)
    ↓
Service (Logique Métier)
    ↓ MapperService
    ↓
Entity (JPA/Hibernate)
    ↓
Repository (Data Access)
    ↓
H2 Database
```

### Couches

| Couche | Responsabilités |
|--------|------------------|
| **Controller** | Endpoints HTTP, validation input, codes HTTP |
| **Service** | Logique métier, règles de progression, transactions |
| **Repository** | Accès aux données, requêtes |
| **Entity** | Modèle de données, persistance |
| **Exception Handler** | Gestion centralisée des erreurs avec ProblemDetail |

---

## 📊 Endpoints

### GET - Lister les aventuriers
```
GET /api/v1/aventuriers?page=1&limit=20&classe=GUERRIER&niveau_min=1&niveau_max=10
```
**Paramètres :**
- `page` (défaut: 1)
- `limit` (défaut: 20, max: 100)
- `classe` (optionnel)
- `niveau_min` (optionnel)
- `niveau_max` (optionnel)

**Réponse :** `200 OK` avec liste paginée

---

### POST - Créer un aventurier
```
POST /api/v1/aventuriers
Content-Type: application/json

{
  "nom": "Légolas",
  "description": "Elfe archer",
  "caracteristiques": {
    "physique": 14,
    "mental": 13,
    "perception": 18
  },
  "niveau": 1,
  "classe": "RODEUR"
}
```

**Réponse :** `201 Created` avec aventurier créé

---

### GET - Récupérer par ID
```
GET /api/v1/aventuriers/{id}
```

**Réponse :** `200 OK` | `404 Not Found`

---

### PUT - Mise à jour complète
```
PUT /api/v1/aventuriers/{id}
Content-Type: application/json

{ /* même format que POST */ }
```

**Réponse :** `200 OK` | `404 Not Found` | `422 Unprocessable Entity`

---

### PATCH - Mise à jour partielle
```
PATCH /api/v1/aventuriers/{id}
Content-Type: application/json

{
  "niveau": 5,
  "description": "Nouvelle description"
}
```

**Réponse :** `200 OK` | `404 Not Found` | `422 Unprocessable Entity`

---

### DELETE - Suppression
```
DELETE /api/v1/aventuriers/{id}
```

**Réponse :** `204 No Content` | `404 Not Found`

---

## 🗂️ Structure du Projet

```
adventures/
├── src/main/java/com/ynov/adventures/
│   ├── controller/
│   │   └── AventurierController.java
│   ├── domain/
│   │   ├── Aventurier.java
│   │   ├── Caracteristiques.java
│   │   └── Classe.java
│   ├── dto/
│   │   ├── AventurierDTO.java
│   │   ├── AventurierCreateDTO.java
│   │   ├── AventurierPatchDTO.java
│   │   ├── CaracteristiquesDTO.java
│   │   ├── CaracteristiquesPatchDTO.java
│   │   ├── AventurierListResponseDTO.java
│   │   └── PaginationDTO.java
│   ├── exception/
│   │   ├── AventurierNotFoundException.java
│   │   ├── AventurierBusinessException.java
│   │   └── GlobalExceptionHandler.java
│   ├── repository/
│   │   └── AventurierRepository.java
│   ├── service/
│   │   ├── AventurierService.java
│   │   └── MapperService.java
│   └── AdventuresApplication.java
├── src/main/resources/
│   ├── application.yaml
│   └── data.sql
├── src/test/java/com/ynov/adventures/
│   ├── controller/
│   │   └── AventurierControllerTest.java
│   └── service/
│       └── AventurierServiceTest.java
├── openapi.yaml
├── pom.xml
├── QUICKSTART.md
├── API_DOCUMENTATION.md
├── CURL_EXAMPLES.md
├── IMPLEMENTATION_SUMMARY.md
└── README.md (ce fichier)
```

---

## 🧪 Tests

### Exécuter tous les tests
```bash
mvn test
```

### Couverts
- ✅ Création d'aventuriers
- ✅ Validation des règles métier
- ✅ Gestion des erreurs 404
- ✅ Gestion des erreurs 422
- ✅ Validation des données
- ✅ Endpoints HTTP

---

## 🛠️ Technologies

| Technologie | Version | Rôle |
|-------------|---------|------|
| Java | 25 | Langage principal |
| Spring Boot | 4.0.3 | Framework principal |
| Spring Data JPA | - | ORM |
| Hibernate | - | Persistance |
| H2 Database | - | Base de données (dev) |
| Jakarta Validation | - | Validation des données |
| Lombok | - | Réduction du boilerplate |
| JUnit 5 | - | Tests unitaires |
| Mockito | - | Mocking |
| Maven | 3.8+ | Gestion de dépendances |

---

## ✨ Fonctionnalités Clés

### Validation Multi-Niveaux
1. **Jakarta Validation** sur les DTOs (annotations)
2. **Validations métier** au niveau service
3. **Gestion des erreurs** avec ProblemDetail

### Pagination Flexible
- Page et limite configurables
- Défauts intelligents
- Métadonnées complètes

### Filtrage Puissant
- Par classe
- Par niveau (min/max)
- Combinaisons possibles

### Gestion d'Erreurs Robuste
- Codes HTTP appropriés (200, 201, 204, 400, 404, 422, 500)
- ProblemDetail standardisé (RFC 7807)
- Messages détaillés avec erreurs par champ

### Timestamps Automatiques
- `createdAt` : Défini à la création
- `updatedAt` : Mis à jour à chaque modification

---

## 🔐 Sécurité

- ✅ Validations des données d'entrée
- ✅ Injection SQL impossible (JPA paramétrisé)
- ✅ Erreurs génériques sans détails sensibles
- ⚠️ Note : JWT non implémenté (à faire selon besoins)

---

## 📈 Prochaines Étapes

Optionnel, selon les besoins :

- [ ] Authentification JWT
- [ ] Rôles et permissions (RBAC)
- [ ] Audit des modifications
- [ ] Cache Redis
- [ ] Base de données PostgreSQL
- [ ] Tests d'intégration avancés
- [ ] Swagger UI / ReDoc
- [ ] CI/CD avec GitHub Actions

---

## 📝 Exemples Rapides

### Créer un aventurier
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Aragorn",
    "description": "Rôdeur des terres sauvages",
    "caracteristiques": {"physique": 17, "mental": 14, "perception": 16},
    "niveau": 1,
    "classe": "RODEUR"
  }'
```

### Augmenter le niveau
```bash
curl -X PATCH http://localhost:8080/api/v1/aventuriers/{id} \
  -H "Content-Type: application/json" \
  -d '{"niveau": 2}'
```

### Lister les guerriers niveau 1-5
```bash
curl "http://localhost:8080/api/v1/aventuriers?classe=GUERRIER&niveau_min=1&niveau_max=5"
```

**Pour plus d'exemples, voir [CURL_EXAMPLES.md](CURL_EXAMPLES.md)**

---

## 🆘 Aide

- **Erreur de port** : Voir [QUICKSTART.md#Dépannage](QUICKSTART.md#dépannage)
- **Tests échouent** : Exécuter `mvn clean test`
- **Questions API** : Consulter [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- **Détails technique** : Consulter [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)

---

## 📄 Licence

Ce projet est fourni à titre d'exemple éducatif.

---

## 👨‍💻 À Propos

Une implémentation complète et professionnelle d'une API REST CRUD en Java/Spring Boot, démontrant :
- Bonnes pratiques architecturales
- Gestion robuste des erreurs
- Validation complète des données
- Documentation exhaustive
- Tests unitaires significatifs
- Respect des normes OpenAPI/REST

**Commencez par [QUICKSTART.md](QUICKSTART.md) pour démarrer ! 🚀**
