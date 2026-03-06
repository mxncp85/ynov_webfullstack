# 🚀 Guide de Test avec Postman - API des Aventuriers

Comment tester complètement l'API avec Postman, étape par étape.

---

## 📋 Table des matières

1. [Installation de Postman](#installation)
2. [Importer le contrat OpenAPI](#importer-openapi)
3. [Tester les endpoints](#tester-endpoints)
4. [Créer une collection personnalisée](#collection)
5. [Variables d'environnement](#variables)
6. [Cas de test avancés](#avances)

---

## 📥 Installation

### Option 1 : Application Desktop
1. Aller sur [postman.com](https://www.postman.com/downloads/)
2. Télécharger l'application pour Windows
3. Installer et créer un compte (gratuit)

### Option 2 : Web Version
- Aller sur [web.postman.co](https://web.postman.co)
- Se connecter avec un compte gratuit

---

## 🔗 Importer le Contrat OpenAPI

### Méthode 1 : Importer depuis fichier

1. **Ouvrir Postman**
2. **Cliquer sur "Import"** (bouton en haut à gauche)
3. **Choisir l'onglet "Upload Files"**
4. **Sélectionner le fichier `openapi.yaml`**
   - Chemin : `c:\Users\ASUS\Downloads\adventures\adventures\openapi.yaml`
5. **Cliquer sur "Import"**

### Résultat
Postman créera automatiquement :
- ✅ Une collection avec tous les endpoints
- ✅ Les schémas de requête/réponse
- ✅ Les exemples
- ✅ Les paramètres

---

## 🧪 Tester les Endpoints

### Prérequis
1. **L'API doit être en cours d'exécution**
   ```bash
   mvnw.cmd spring-boot:run
   ```
2. **Base URL:** `http://localhost:8080`

### Endpoints à Tester

#### 1️⃣ GET - Lister tous les aventuriers

```
GET http://localhost:8080/api/v1/aventuriers
```

**Paramètres (optionnels) :**
| Nom | Valeur | Type |
|-----|--------|------|
| page | 1 | Query |
| limit | 20 | Query |
| classe | GUERRIER | Query |
| niveau_min | 1 | Query |
| niveau_max | 10 | Query |

**Étapes dans Postman:**
1. Créer nouvelle requête GET
2. URL: `http://localhost:8080/api/v1/aventuriers`
3. Onglet "Params", ajouter les paramètres optionnels
4. Cliquer "Send"

**Réponse attendue (200):**
```json
{
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "nom": "Aragorn",
      "description": "Rôdeur des terres sauvages, héritier du trône du Gondor",
      "caracteristiques": {
        "physique": 17,
        "mental": 14,
        "perception": 16
      },
      "niveau": 5,
      "classe": "RODEUR",
      "createdAt": "2026-03-05T10:15:00",
      "updatedAt": "2026-03-05T10:15:00"
    }
    // ... autres aventuriers
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 7,
    "totalPages": 1
  }
}
```

---

#### 2️⃣ POST - Créer un aventurier

```
POST http://localhost:8080/api/v1/aventuriers
Content-Type: application/json
```

**Étapes:**
1. Créer nouvelle requête POST
2. URL: `http://localhost:8080/api/v1/aventuriers`
3. Onglet "Headers":
   - Key: `Content-Type`
   - Value: `application/json`
4. Onglet "Body" → Raw → JSON
5. Coller le JSON ci-dessous
6. Cliquer "Send"

**Body (JSON):**
```json
{
  "nom": "Legolas",
  "description": "Elfe archer de la Forêt Noire",
  "caracteristiques": {
    "physique": 14,
    "mental": 13,
    "perception": 18
  },
  "niveau": 1,
  "classe": "RODEUR"
}
```

**Réponse attendue (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440099",
  "nom": "Legolas",
  "description": "Elfe archer de la Forêt Noire",
  "caracteristiques": {
    "physique": 14,
    "mental": 13,
    "perception": 18
  },
  "niveau": 1,
  "classe": "RODEUR",
  "createdAt": "2026-03-05T10:20:00",
  "updatedAt": "2026-03-05T10:20:00"
}
```

✅ **Notez l'ID pour les tests suivants**

---

#### 3️⃣ GET - Récupérer par ID

```
GET http://localhost:8080/api/v1/aventuriers/{id}
```

**Étapes:**
1. Créer nouvelle requête GET
2. URL: `http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440099`
   - Remplacez l'ID par celui reçu précédemment
3. Cliquer "Send"

**Réponse attendue (200):**
Même format que la création

---

#### 4️⃣ PUT - Mise à jour complète

```
PUT http://localhost:8080/api/v1/aventuriers/{id}
Content-Type: application/json
```

**Étapes:**
1. Créer nouvelle requête PUT
2. URL: `http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440099`
3. Headers: `Content-Type: application/json`
4. Body (JSON):

```json
{
  "nom": "Legolas le Vaillant",
  "description": "Elfe archer légendaire",
  "caracteristiques": {
    "physique": 15,
    "mental": 14,
    "perception": 19
  },
  "niveau": 2,
  "classe": "RODEUR"
}
```

5. Cliquer "Send"

**Réponse attendue (200):** Aventurier mis à jour

---

#### 5️⃣ PATCH - Mise à jour partielle

```
PATCH http://localhost:8080/api/v1/aventuriers/{id}
Content-Type: application/json
```

**Cas 1: Augmenter le niveau**
```json
{
  "niveau": 3
}
```

**Cas 2: Modifier le nom uniquement**
```json
{
  "nom": "Legolas Vertefeuille"
}
```

**Cas 3: Modifier une caractéristique**
```json
{
  "caracteristiques": {
    "perception": 20
  }
}
```

---

#### 6️⃣ DELETE - Supprimer

```
DELETE http://localhost:8080/api/v1/aventuriers/{id}
```

**Étapes:**
1. Créer nouvelle requête DELETE
2. URL: `http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440099`
3. Cliquer "Send"

**Réponse attendue (204):** Pas de contenu (normal)

---

## 📁 Créer une Collection Personnalisée

### Étapes

1. **Cliquer sur "Collections"** (gauche)
2. **Cliquer le "+"**
3. **Nommer:** "API Aventuriers"
4. **Cliquer "Create"**

### Créer des Requêtes dans la Collection

#### Dossier "Read"
- GET /aventuriers
- GET /aventuriers/{id}

#### Dossier "Create"
- POST /aventuriers

#### Dossier "Update"
- PUT /aventuriers/{id}
- PATCH /aventuriers/{id}

#### Dossier "Delete"
- DELETE /aventuriers/{id}

### Structure
```
API Aventuriers
├── Read
│   ├── List all
│   └── Get by ID
├── Create
│   └── Create new
├── Update
│   ├── Full update (PUT)
│   └── Partial update (PATCH)
└── Delete
    └── Delete by ID
```

---

## 🔧 Variables d'Environnement

### Créer un Environnement

1. **Cliquer sur l'icône "Environnements"** (gauche)
2. **Cliquer le "+"**
3. **Nommer:** "Development"

### Ajouter des Variables

| Variable | Valeur | Type |
|----------|--------|------|
| base_url | http://localhost:8080 | string |
| api_path | /api/v1 | string |
| aventurier_id | (à remplir) | string |

### Utiliser les Variables

Au lieu de:
```
http://localhost:8080/api/v1/aventuriers
```

Utiliser:
```
{{base_url}}{{api_path}}/aventuriers
```

Et pour l'ID:
```
{{base_url}}{{api_path}}/aventuriers/{{aventurier_id}}
```

---

## 🧪 Cas de Test Avancés

### Test 1 : Flux Complet (Happy Path)

1. **POST** - Créer un aventurier au niveau 1
   - Réponse: 201 ✅
   
2. **GET** - Récupérer l'aventurier
   - Réponse: 200 ✅
   
3. **PATCH** - Augmenter niveau à 2
   - Réponse: 200 ✅
   
4. **GET** - Vérifier le niveau
   - Réponse: 200, niveau = 2 ✅
   
5. **DELETE** - Supprimer
   - Réponse: 204 ✅

### Test 2 : Erreurs Métier

#### ❌ Créer avec niveau ≠ 1
```json
{
  "nom": "Test",
  "caracteristiques": {"physique": 10, "mental": 10, "perception": 10},
  "niveau": 5,
  "classe": "GUERRIER"
}
```
**Résultat attendu:** 422 Unprocessable Entity
```json
{
  "type": "about:blank",
  "title": "Erreur de validation métier",
  "status": 422,
  "detail": "Un aventurier nouvellement créé doit commencer au niveau 1"
}
```

#### ❌ Baisser le niveau
**Créer un aventurier, puis:**
```json
{
  "nom": "Test",
  "caracteristiques": {"physique": 10, "mental": 10, "perception": 10},
  "niveau": 1,
  "classe": "GUERRIER"
}
```
**Puis PATCH avec niveau 0:**
```json
{
  "niveau": 0
}
```
**Résultat attendu:** 422

#### ❌ Augmenter de plus de 1
```json
{
  "niveau": 5
}
```
**Résultat attendu:** 422

### Test 3 : Erreurs de Validation

#### ❌ Nom trop court
```json
{
  "nom": "A",
  "caracteristiques": {"physique": 10, "mental": 10, "perception": 10},
  "niveau": 1,
  "classe": "GUERRIER"
}
```
**Résultat attendu:** 400 Bad Request
```json
{
  "type": "about:blank",
  "title": "Erreur de validation",
  "status": 400,
  "detail": "Les données fournies sont invalides",
  "fieldErrors": {
    "nom": "Le nom doit faire entre 2 et 100 caractères"
  }
}
```

#### ❌ Caractéristique invalide
```json
{
  "nom": "Test",
  "caracteristiques": {"physique": 25, "mental": 10, "perception": 10},
  "niveau": 1,
  "classe": "GUERRIER"
}
```
**Résultat attendu:** 400 Bad Request avec erreur `caracteristiques.physique`

### Test 4 : 404 Not Found
```
GET http://localhost:8080/api/v1/aventuriers/00000000-0000-0000-0000-000000000000
```
**Résultat attendu:** 404 Not Found

---

## 📊 Checklist de Test

Cochez les tests réussis :

### CRUD Basiques
- [ ] GET - Lister tous les aventuriers (200)
- [ ] POST - Créer un aventurier (201)
- [ ] GET - Récupérer par ID (200)
- [ ] PUT - Mise à jour complète (200)
- [ ] PATCH - Mise à jour partielle (200)
- [ ] DELETE - Supprimer (204)

### Règles Métier
- [ ] Créer au niveau 1 (201)
- [ ] Créer avec niveau ≠ 1 (422)
- [ ] Baisser le niveau (422)
- [ ] Augmenter > 1 niveau (422)
- [ ] Augmenter de 1 niveau (200)

### Validations
- [ ] Nom trop court (400)
- [ ] Caractéristique invalide (400)
- [ ] Champ obligatoire manquant (400)
- [ ] Description trop longue (400)

### Erreurs
- [ ] GET ID inexistant (404)
- [ ] DELETE ID inexistant (404)
- [ ] PUT/PATCH ID inexistant (404)

### Filtrage & Pagination
- [ ] GET avec page=1&limit=5 (200)
- [ ] GET avec classe=GUERRIER (200)
- [ ] GET avec niveau_min=1&niveau_max=5 (200)

---

## 💾 Exporter la Collection

Une fois vos requêtes créées :

1. **Clic droit sur "API Aventuriers"**
2. **"Export"**
3. **Choisir format "Collection v2.1"**
4. **Cliquer "Export"**

**Fichier généré:** `API Aventuriers.postman_collection.json`

Vous pouvez le partager avec d'autres développeurs !

---

## 🔗 Scripts de Test (Tests Automatisés)

### Dans Postman, onglet "Tests"

**Ajouter après chaque requête :**

#### Pour GET liste
```javascript
pm.test("Status is 200", function() {
    pm.response.to.have.status(200);
});

pm.test("Response has pagination", function() {
    var jsonData = pm.response.json();
    pm.expect(jsonData.pagination).to.exist;
    pm.expect(jsonData.data).to.be.an('array');
});
```

#### Pour POST création
```javascript
pm.test("Status is 201", function() {
    pm.response.to.have.status(201);
});

pm.test("Response has ID", function() {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.exist;
    pm.environment.set("aventurier_id", jsonData.id);
});

pm.test("Niveau is 1", function() {
    var jsonData = pm.response.json();
    pm.expect(jsonData.niveau).to.equal(1);
});
```

#### Pour erreur 422
```javascript
pm.test("Status is 422", function() {
    pm.response.to.have.status(422);
});

pm.test("Error has title", function() {
    var jsonData = pm.response.json();
    pm.expect(jsonData.title).to.exist;
    pm.expect(jsonData.status).to.equal(422);
});
```

---

## 🚀 Workflow Complet

1. ✅ **Importer le contrat OpenAPI**
2. ✅ **Créer un environnement avec variables**
3. ✅ **Créer une collection organisée**
4. ✅ **Tester chaque endpoint**
5. ✅ **Ajouter des tests de validation**
6. ✅ **Exécuter la collection entière**
7. ✅ **Exporter pour partage**

---

## 📚 Raccourcis Clavier Postman

| Action | Windows |
|--------|---------|
| Nouvelle requête | Ctrl+Alt+N |
| Nouvelle collection | Ctrl+Alt+C |
| Envoyer requête | Ctrl+Entrée |
| Chercher | Ctrl+F |
| Duplicate requête | Ctrl+D |

---

## 💡 Astuces

1. **Sauvegarde automatique** : Postman sauvegarde tout automatiquement
2. **Historique** : Cliquer sur "History" pour voir les requêtes précédentes
3. **Scripts pré-requête** : Onglet "Pre-request Script" pour setup avant requête
4. **Cookies** : Gérer automatiquement avec Postman
5. **Certificats SSL** : Settings → General → SSL → désactiver (dev)

---

**Prêt à tester ! 🧪 Lancez Postman et importez le contrat OpenAPI !**
