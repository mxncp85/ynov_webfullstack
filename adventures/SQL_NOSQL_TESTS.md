# Tests de Validation - SQL et NoSQL

## Configuration H2 et MongoDB

### Base de Données H2 (SQL)
- **URL**: `jdbc:h2:mem:testdb`
- **Console H2**: http://localhost:8080/h2-console
- **Username**: `sa`
- **Password**: (vide)

### MongoDB (NoSQL)
- **Host**: `localhost`
- **Port**: `27017`
- **Database**: `adventures_logs`
- **Collection**: `api_logs`

## Tests des Endpoints avec Persistance

### 1. Test GET - Liste tous les aventuriers

**Commande:**
```cmd
curl -X GET "http://localhost:8080/api/v1/aventuriers?page=1&limit=10"
```

**Vérification SQL:**
- Ouvrir http://localhost:8080/h2-console
- Exécuter: `SELECT * FROM aventuriers;`
- Devrait montrer 7 aventuriers pré-chargés

**Vérification MongoDB:**
```javascript
db.api_logs.find({ method: "GET", endpoint: "/api/v1/aventuriers" }).pretty()
```

---

### 2. Test POST - Créer un aventurier

**Commande:**
```cmd
curl -X POST http://localhost:8080/api/v1/aventuriers ^
-H "Content-Type: application/json" ^
-d "{\"nom\":\"Merlin\",\"description\":\"Enchanteur légendaire\",\"caracteristiques\":{\"physique\":10,\"mental\":20,\"perception\":18},\"classe\":\"MAGE\"}"
```

**Vérification SQL:**
```sql
SELECT * FROM aventuriers WHERE nom = 'Merlin';
```

**Vérification MongoDB:**
```javascript
db.api_logs.find({ method: "POST", endpoint: "/api/v1/aventuriers" }).sort({timestamp:-1}).limit(1).pretty()
```

**Attendu:**
- `statusCode`: 201
- `success`: true
- `logLevel`: "INFO"
- `requestBody`: devrait contenir les données de Merlin
- `responseBody`: devrait contenir l'aventurier créé avec un ID UUID

---

### 3. Test GET by ID - Récupérer un aventurier

**Commande:**
```cmd
curl -X GET http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440001
```

**Vérification MongoDB:**
```javascript
db.api_logs.find({ 
  method: "GET", 
  "pathVariables.id": "550e8400-e29b-41d4-a716-446655440001" 
}).pretty()
```

**Attendu:**
- `statusCode`: 200
- `success`: true
- `responseBody`: devrait contenir Aragorn

---

### 4. Test PUT - Mise à jour complète

**Commande:**
```cmd
curl -X PUT http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440005 ^
-H "Content-Type: application/json" ^
-d "{\"nom\":\"Frodo Baggins\",\"description\":\"Porteur de l'Anneau\",\"caracteristiques\":{\"physique\":9,\"mental\":12,\"perception\":14},\"niveau\":3,\"classe\":\"VOLEUR\"}"
```

**Vérification SQL:**
```sql
SELECT * FROM aventuriers WHERE id = '550e8400-e29b-41d4-a716-446655440005';
```

**Attendu en SQL:**
- Frodo devrait maintenant être niveau 3
- Description mise à jour

**Vérification MongoDB:**
```javascript
db.api_logs.find({ 
  method: "PUT", 
  "pathVariables.id": "550e8400-e29b-41d4-a716-446655440005" 
}).sort({timestamp:-1}).limit(1).pretty()
```

---

### 5. Test PATCH - Mise à jour partielle

**Commande:**
```cmd
curl -X PATCH http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440002 ^
-H "Content-Type: application/json" ^
-d "{\"niveau\":4}"
```

**Vérification SQL:**
```sql
SELECT nom, niveau FROM aventuriers WHERE id = '550e8400-e29b-41d4-a716-446655440002';
```

**Attendu:**
- Legolas devrait être passé au niveau 4

---

### 6. Test DELETE - Suppression

**Commande:**
```cmd
curl -X DELETE http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440006
```

**Vérification SQL:**
```sql
SELECT * FROM aventuriers WHERE id = '550e8400-e29b-41d4-a716-446655440006';
```

**Attendu:**
- Aucun résultat (Boromir supprimé)

**Vérification MongoDB:**
```javascript
db.api_logs.find({ 
  method: "DELETE", 
  "pathVariables.id": "550e8400-e29b-41d4-a716-446655440006" 
}).pretty()
```

**Attendu:**
- `statusCode`: 204
- `success`: true
- Pas de `responseBody`

---

## Tests des Erreurs

### 7. Test 404 - Aventurier non trouvé (Erreur Client)

**Commande:**
```cmd
curl -X GET http://localhost:8080/api/v1/aventuriers/00000000-0000-0000-0000-000000000000
```

**Vérification MongoDB:**
```javascript
db.api_logs.find({ 
  logLevel: "WARN",
  statusCode: 404 
}).sort({timestamp:-1}).limit(1).pretty()
```

**Attendu:**
- `statusCode`: 404
- `logLevel`: "WARN"
- `success`: false
- `errorMessage`: devrait contenir "Aventurier non trouvé"

---

### 8. Test 422 - Erreur de validation métier (Erreur Client)

**Commande (essayer de monter de 2 niveaux):**
```cmd
curl -X PATCH http://localhost:8080/api/v1/aventuriers/550e8400-e29b-41d4-a716-446655440005 ^
-H "Content-Type: application/json" ^
-d "{\"niveau\":5}"
```

**Vérification MongoDB:**
```javascript
db.api_logs.find({ 
  logLevel: "WARN",
  statusCode: 422 
}).sort({timestamp:-1}).limit(1).pretty()
```

**Attendu:**
- `statusCode`: 422
- `logLevel`: "WARN"
- `success`: false
- `errorMessage`: "Le niveau ne peut pas monter de plus de 1 à la fois"

---

### 9. Test 400 - Erreur de validation (Erreur Client)

**Commande:**
```cmd
curl -X POST http://localhost:8080/api/v1/aventuriers ^
-H "Content-Type: application/json" ^
-d "{\"description\":\"Test sans nom\"}"
```

**Vérification MongoDB:**
```javascript
db.api_logs.find({ 
  logLevel: "WARN",
  statusCode: 400 
}).sort({timestamp:-1}).limit(1).pretty()
```

**Attendu:**
- `statusCode`: 400
- `logLevel`: "WARN"
- `success`: false

---

## Tests de Filtrage

### 10. Test filtrage par classe

**Commande:**
```cmd
curl -X GET "http://localhost:8080/api/v1/aventuriers?classe=GUERRIER"
```

**Vérification:**
```javascript
db.api_logs.find({ 
  "requestParams.classe": "GUERRIER" 
}).sort({timestamp:-1}).limit(1).pretty()
```

---

### 11. Test filtrage par niveau

**Commande:**
```cmd
curl -X GET "http://localhost:8080/api/v1/aventuriers?niveau_min=5&niveau_max=8"
```

**Vérification MongoDB:**
```javascript
db.api_logs.find({ 
  "requestParams.niveau_min": "5",
  "requestParams.niveau_max": "8"
}).sort({timestamp:-1}).limit(1).pretty()
```

---

## Statistiques MongoDB

### Compter les logs par niveau

```javascript
db.api_logs.aggregate([
  { $group: { _id: "$logLevel", count: { $sum: 1 } } }
])
```

### Compter les succès vs échecs

```javascript
db.api_logs.aggregate([
  { $group: { _id: "$success", count: { $sum: 1 } } }
])
```

### Temps d'exécution moyen par endpoint

```javascript
db.api_logs.aggregate([
  { $group: { 
      _id: "$endpoint", 
      avgTime: { $avg: "$executionTimeMs" },
      count: { $sum: 1 }
    }
  },
  { $sort: { avgTime: -1 } }
])
```

### Logs des dernières 24h

```javascript
var yesterday = new Date(new Date().getTime() - (24 * 60 * 60 * 1000));
db.api_logs.find({ 
  timestamp: { $gte: yesterday } 
}).count()
```

---

## Vérification Complète

Pour vérifier que tout fonctionne:

1. **Démarrer l'application**
2. **Vérifier H2**: Ouvrir http://localhost:8080/h2-console et voir les données
3. **Faire quelques appels API** avec les commandes ci-dessus
4. **Vérifier MongoDB**: 
   ```javascript
   use adventures_logs
   db.api_logs.count()
   ```
5. **Vérifier les logs dans la console** de l'application

Tous les appels devraient être persistés dans H2 (données métier) ET loggés dans MongoDB (audit).
