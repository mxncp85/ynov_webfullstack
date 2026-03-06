# API des Aventuriers

API REST pour la gestion des aventuriers dans un univers de fantasy.

## Description

Cette API implémente un système CRUD complet pour la gestion des aventuriers avec les règles métier suivantes :

### Règles Métier

1. **Création d'un aventurier** : Un aventurier nouvellement créé commence toujours au niveau 1
2. **Progression du niveau** : Le niveau ne peut jamais descendre
3. **Limitation de progression** : Le niveau ne peut pas monter de plus de 1 à la fois

### Propriétés d'un Aventurier

- **ID** : Identifiant unique (UUID) généré automatiquement
- **Nom** : String (2-100 caractères, obligatoire)
- **Description** : String (jusqu'à 1000 caractères, optionnel)
- **Caractéristiques** : Objet composé de 3 attributs (1-20 chacun, obligatoires)
  - **Physique** : Force, endurance et résistance physique
  - **Mental** : Intelligence, sagesse et force de volonté
  - **Perception** : Acuité sensorielle et intuition
- **Niveau** : Integer (1-20, obligatoire)
- **Classe** : Énumération parmi 10 types (obligatoire)
  - Guerrier
  - Mage
  - Rôdeur
  - Voleur
  - Clerc
  - Barde
  - Paladin
  - Druide
  - Barbare
  - Sorcier
- **CreatedAt** : Date/heure de création (généré automatiquement, lecture seule)
- **UpdatedAt** : Date/heure de dernière mise à jour (généré automatiquement, lecture seule)

## Endpoints

### Liste des Aventuriers

```
GET /api/v1/aventuriers
```

**Paramètres de requête (optionnels):**
- `page` : Numéro de la page (défaut: 1)
- `limit` : Éléments par page (défaut: 20, max: 100)
- `classe` : Filtrer par classe (ex: Guerrier)
- `niveau_min` : Niveau minimum (ex: 5)
- `niveau_max` : Niveau maximum (ex: 10)

**Exemple:**
```bash
curl "http://localhost:8080/api/v1/aventuriers?page=1&limit=20&classe=Guerrier&niveau_min=5&niveau_max=10"
```

**Réponse (200 OK):**
```json
{
  "data": [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "nom": "Aragorn",
      "description": "Rôdeur des terres sauvages",
      "caracteristiques": {
        "physique": 15,
        "mental": 14,
        "perception": 16
      },
      "niveau": 1,
      "classe": "RODEUR",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 42,
    "totalPages": 3
  }
}
```

### Créer un Aventurier

```
POST /api/v1/aventuriers
```

**Corps de la requête:**
```json
{
  "nom": "Légolas",
  "description": "Elfe de la Forêt Noire, archer d'exception.",
  "caracteristiques": {
    "physique": 14,
    "mental": 13,
    "perception": 18
  },
  "niveau": 1,
  "classe": "RODEUR"
}
```

**Réponse (201 Created):**
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "nom": "Légolas",
  "description": "Elfe de la Forêt Noire, archer d'exception.",
  "caracteristiques": {
    "physique": 14,
    "mental": 13,
    "perception": 18
  },
  "niveau": 1,
  "classe": "RODEUR",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Récupérer un Aventurier

```
GET /api/v1/aventuriers/{id}
```

**Exemple:**
```bash
curl "http://localhost:8080/api/v1/aventuriers/3fa85f64-5717-4562-b3fc-2c963f66afa6"
```

**Réponse (200 OK):** Même format que la création

**Erreur (404 Not Found):**
```json
{
  "type": "about:blank",
  "title": "Aventurier non trouvé",
  "status": 404,
  "detail": "Aventurier non trouvé avec l'ID : 3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```

### Mettre à jour un Aventurier (PUT)

```
PUT /api/v1/aventuriers/{id}
```

Effectue une mise à jour complète (tous les champs doivent être fournis).

**Corps de la requête:** Identique à la création

**Réponse (200 OK):** Aventurier mis à jour

**Erreur (422 Unprocessable Entity):** Si violation des règles métier

```json
{
  "type": "about:blank",
  "title": "Erreur de validation métier",
  "status": 422,
  "detail": "Le niveau ne peut jamais descendre. Niveau actuel : 5, Niveau demandé : 3"
}
```

### Mettre à jour partiellement un Aventurier (PATCH)

```
PATCH /api/v1/aventuriers/{id}
```

Effectue une mise à jour partielle (seuls les champs fournis sont mis à jour).

**Corps de la requête (tous les champs optionnels):**
```json
{
  "nom": "Aragorn le Grand",
  "niveau": 6,
  "caracteristiques": {
    "physique": 16
  }
}
```

**Réponse (200 OK):** Aventurier mis à jour

### Supprimer un Aventurier

```
DELETE /api/v1/aventuriers/{id}
```

**Réponse (204 No Content):** Aucun contenu

**Erreur (404 Not Found):** Aventurier inexistant

## Gestion des Erreurs

Toutes les erreurs sont retournées au format **ProblemDetail** de Spring :

- **400 Bad Request** : Requête malformée ou données invalides
- **404 Not Found** : Ressource inexistante
- **422 Unprocessable Entity** : Erreurs métier (ex: violation des règles de progression)
- **500 Internal Server Error** : Erreur serveur interne

### Exemple d'erreur de validation

```json
{
  "type": "about:blank",
  "title": "Erreur de validation",
  "status": 400,
  "detail": "Les données fournies sont invalides",
  "fieldErrors": {
    "nom": "Le nom doit faire entre 2 et 100 caractères",
    "caracteristiques.physique": "La valeur doit être comprise entre 1 et 20"
  }
}
```

## Technologies

- **Framework** : Spring Boot 4.0.3
- **Language** : Java 25
- **Base de données** : H2 (en mémoire pour le développement)
- **JPA** : Hibernate
- **Validation** : Jakarta Validation
- **Mapping** : Service personnalisé (MapperService)
- **Documentation** : OpenAPI 3.0.3

## Démarrage

### Prérequis

- Java 25+
- Maven 3.8+

### Commandes

```bash
# Compiler le projet
mvn clean install

# Démarrer l'application
mvn spring-boot:run

# Accéder à la console H2
http://localhost:8080/h2-console
```

L'API sera accessible à: `http://localhost:8080/api/v1/aventuriers`

## Documentation OpenAPI

Le fichier `openapi.yaml` à la racine du projet contient la documentation complète de l'API.

Vous pouvez le visualiser avec des outils comme:
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [ReDoc](https://redoc.ly/)
- Des plugins IntelliJ IDEA

## Tests

Les tests unitaires couvrent les aspects critiques :

```bash
# Exécuter les tests
mvn test
```

Tests inclus:
- Création d'aventuriers
- Validation des règles métier de progression
- Gestion des erreurs 404
- Validation des données d'entrée
