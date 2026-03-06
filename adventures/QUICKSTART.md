# 🚀 Guide de Démarrage Rapide - API des Aventuriers

## Prérequis

- **Java 25+** : [Télécharger](https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html)
- **Maven 3.8+** : [Télécharger](https://maven.apache.org/download.cgi)
- **Git** (optionnel) : Pour cloner le projet

Vérification :
```bash
java -version
mvn -version
```

## 📦 Installation et Démarrage

### 1. Compiler le projet

```bash
cd adventures
mvn clean install
```

Cela va :
- Télécharger toutes les dépendances
- Compiler le code source
- Exécuter les tests unitaires
- Créer un JAR exécutable

### 2. Démarrer l'application

```bash
mvn spring-boot:run
```

Ou directement avec le JAR généré :
```bash
java -jar target/adventures-0.0.1-SNAPSHOT.jar
```

**Vous devriez voir :**
```
Started AdventuresApplication in X.XXX seconds (JVM running for Y.YYY)
```

### 3. Vérifier que l'API est en ligne

```bash
curl http://localhost:8080/api/v1/aventuriers
```

Vous devriez recevoir une réponse JSON paginée avec 7 aventuriers pré-chargés.

---

## 🧪 Tester l'API

### Option 1 : Avec curl (Linux/Mac)

```bash
# Créer un aventurier
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Mon Aventurier",
    "caracteristiques": {
      "physique": 15,
      "mental": 12,
      "perception": 14
    },
    "niveau": 1,
    "classe": "GUERRIER"
  }'
```

### Option 2 : Avec PowerShell (Windows)

```powershell
$body = @{
    nom = "Mon Aventurier"
    caracteristiques = @{
        physique = 15
        mental = 12
        perception = 14
    }
    niveau = 1
    classe = "GUERRIER"
} | ConvertTo-Json

Invoke-WebRequest -Uri http://localhost:8080/api/v1/aventuriers `
  -Method Post `
  -ContentType "application/json" `
  -Body $body
```

### Option 3 : Avec le script de test complet

**Linux/Mac :**
```bash
chmod +x test_api.sh
./test_api.sh
```

**Windows (PowerShell) :**
```powershell
powershell -ExecutionPolicy Bypass -File test_api.ps1
```

### Option 4 : Avec Postman/Insomnia

Importez le fichier `openapi.yaml` dans votre client REST préféré.

---

## 📊 Accéder à la console H2

La base de données est accessible via une interface web :

```
http://localhost:8080/h2-console
```

**Paramètres de connexion :**
- **JDBC URL :** `jdbc:h2:mem:testdb`
- **User Name :** `sa`
- **Password :** (vide)

---

## 📚 Documentation

Consultez ces fichiers pour plus d'informations :

| Fichier | Description |
|---------|-------------|
| `API_DOCUMENTATION.md` | Guide complet de l'API |
| `CURL_EXAMPLES.md` | Exemples de requêtes curl |
| `IMPLEMENTATION_SUMMARY.md` | Vue d'ensemble technique |
| `CHECKLIST.md` | Vérification de l'implémentation |
| `openapi.yaml` | Contrat OpenAPI 3.0.3 |

---

## ✅ Tests incluant les règles métier

### Règle 1 : Créer au niveau 1

✅ **Succès :**
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{"nom":"Test","caracteristiques":{"physique":10,"mental":10,"perception":10},"niveau":1,"classe":"GUERRIER"}'
# Retour: 201 Created
```

❌ **Erreur (niveau ≠ 1) :**
```bash
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{"nom":"Test","caracteristiques":{"physique":10,"mental":10,"perception":10},"niveau":5,"classe":"GUERRIER"}'
# Retour: 422 Unprocessable Entity
```

### Règle 2 : Le niveau ne peut pas descendre

Supposons un aventurier au niveau 3. Tenter de le passer au niveau 2 :

```bash
curl -X PUT http://localhost:8080/api/v1/aventuriers/{id} \
  -H "Content-Type: application/json" \
  -d '{"nom":"Test","caracteristiques":{"physique":10,"mental":10,"perception":10},"niveau":2,"classe":"GUERRIER"}'
# Retour: 422 Unprocessable Entity
# Message: "Le niveau ne peut jamais descendre. Niveau actuel : 3, Niveau demandé : 2"
```

### Règle 3 : Le niveau monte de max 1

Supposons un aventurier au niveau 3 :

✅ **Succès (niveau 4) :**
```bash
curl -X PATCH http://localhost:8080/api/v1/aventuriers/{id} \
  -H "Content-Type: application/json" \
  -d '{"niveau":4}'
# Retour: 200 OK
```

❌ **Erreur (niveau 6) :**
```bash
curl -X PATCH http://localhost:8080/api/v1/aventuriers/{id} \
  -H "Content-Type: application/json" \
  -d '{"niveau":6}'
# Retour: 422 Unprocessable Entity
# Message: "Le niveau ne peut pas monter de plus de 1 à la fois. Niveau actuel : 3, Niveau demandé : 6"
```

---

## 🆘 Dépannage

### Le port 8080 est déjà utilisé

```bash
# Linux/Mac - Tuer le processus
lsof -ti:8080 | xargs kill -9

# Windows - PowerShell
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process
```

Ou démarrer sur un autre port :
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Les tests ne passent pas

```bash
# Nettoyer et reconstruire
mvn clean test
```

Si le problème persiste :
```bash
# Vérifier que Java 25+ est utilisé
java -version

# Réinstaller les dépendances
mvn clean install -U
```

### La base de données ne se remplit pas

Vérifiez que `data.sql` existe dans `src/main/resources/` et que la configuration dans `application.yaml` a :
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

### Erreur "Operation not supported" avec H2

C'est normal avec H2 en mémoire. La base de données est recréée à chaque redémarrage.

---

## 🔄 Cycle de développement courant

```bash
# 1. Démarrer l'app
mvn spring-boot:run

# 2. Dans un autre terminal, tester
curl http://localhost:8080/api/v1/aventuriers

# 3. Créer un aventurier
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{"nom":"Legolas","caracteristiques":{"physique":14,"mental":13,"perception":18},"niveau":1,"classe":"RODEUR"}'

# 4. Récupérer son ID de la réponse et continuer les tests
curl http://localhost:8080/api/v1/aventuriers/{id-reçu}

# 5. Arrêter l'app : Ctrl+C
```

---

## 📊 Structure de réponse

### Créer un aventurier (201)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nom": "Aragorn",
  "description": "Rôdeur des terres sauvages",
  "caracteristiques": {
    "physique": 17,
    "mental": 14,
    "perception": 16
  },
  "niveau": 1,
  "classe": "RODEUR",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Lister avec pagination (200)

```json
{
  "data": [
    {/* aventurier 1 */},
    {/* aventurier 2 */}
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 42,
    "totalPages": 3
  }
}
```

### Erreur (400/404/422/500)

```json
{
  "type": "about:blank",
  "title": "Aventurier non trouvé",
  "status": 404,
  "detail": "Aventurier non trouvé avec l'ID : 00000000-0000-0000-0000-000000000000"
}
```

---

## 💡 Conseils

- **Sauvegardez les IDs** lors du test pour les réutiliser dans d'autres requêtes
- **Utilisez les filtres** pour explorer les données : `?classe=GUERRIER&niveau_min=1&niveau_max=10`
- **Consultez les logs** (niveau DEBUG) pour voir les détails des opérations
- **Testez les erreurs** : les messages ProblemDetail sont informatifs

---

## 🎓 Exemples de workflow

### Créer et progresser un aventurier

```bash
# 1. Créer
curl -X POST http://localhost:8080/api/v1/aventuriers \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Nouveau Guerrier",
    "caracteristiques": {"physique": 16, "mental": 10, "perception": 12},
    "niveau": 1,
    "classe": "GUERRIER"
  }'
# Réponse contient un ID

# 2. Augmenter au niveau 2
curl -X PATCH http://localhost:8080/api/v1/aventuriers/{id} \
  -H "Content-Type: application/json" \
  -d '{"niveau": 2}'

# 3. Augmenter au niveau 3
curl -X PATCH http://localhost:8080/api/v1/aventuriers/{id} \
  -H "Content-Type: application/json" \
  -d '{"niveau": 3}'

# 4. Améliorer les stats
curl -X PATCH http://localhost:8080/api/v1/aventuriers/{id} \
  -H "Content-Type: application/json" \
  -d '{"caracteristiques": {"physique": 18}}'

# 5. Voir la progression
curl http://localhost:8080/api/v1/aventuriers/{id}
```

---

## ✨ Prochaines étapes

Une fois familiarisé avec l'API :

1. Explorez le contrat `openapi.yaml` complet
2. Lisez `IMPLEMENTATION_SUMMARY.md` pour comprendre l'architecture
3. Consultez le code source pour voir les implémentations
4. Modifiez et étendiez selon vos besoins

**L'API est prête pour le développement !** 🎉
