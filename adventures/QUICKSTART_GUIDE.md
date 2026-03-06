# 🚀 Guide de Démarrage Rapide

## ✅ Résumé des Changements

Votre projet a été configuré avec:
- ✅ **Spring Boot 3.4.3** (au lieu de 4.0.3 qui n'existe pas)
- ✅ **Java 21** (au lieu de 25 qui n'est pas stable)
- ✅ **H2 Database (SQL)** - Fonctionnel et configuré
- ✅ **MongoDB (NoSQL)** - Optionnel, l'application fonctionne sans
- ✅ **Logging AOP** - Intercepte tous les appels API
- ✅ **Données de test** - 7 aventuriers pré-chargés

## 🏗️ Build du Projet

### Option 1: Build complet avec tests
```cmd
mvnw.cmd clean install
```

### Option 2: Build sans tests (plus rapide)
```cmd
mvnw.cmd clean install -DskipTests
```

### Option 3: Juste compiler
```cmd
mvnw.cmd clean compile
```

## 🎯 Démarrage de l'Application

### Sans MongoDB (Mode Simple)
```cmd
mvnw.cmd spring-boot:run
```

L'application démarrera et loggera dans la console uniquement.

### Avec MongoDB (Mode Complet)

**Étape 1: Démarrer MongoDB avec Docker**
```cmd
start_mongodb.cmd
```

**Étape 2: Démarrer l'application**
```cmd
mvnw.cmd spring-boot:run
```

L'application loggera dans la console ET dans MongoDB.

## 🧪 Tests

### Tests automatiques complets
```powershell
.\test_sql_nosql.ps1
```

### Tests manuels avec curl

**Liste des aventuriers:**
```cmd
curl http://localhost:8080/api/v1/aventuriers
```

**Créer un aventurier:**
```cmd
curl -X POST http://localhost:8080/api/v1/aventuriers ^
-H "Content-Type: application/json" ^
-d "{\"nom\":\"Test\",\"description\":\"Test\",\"caracteristiques\":{\"physique\":10,\"mental\":10,\"perception\":10},\"classe\":\"MAGE\"}"
```

## 🔍 Vérifications

### 1. Base de Données H2 (SQL)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (vide)
- Requête: `SELECT * FROM aventuriers;`

### 2. MongoDB (NoSQL) - Si démarré
```javascript
mongosh
use adventures_logs
db.api_logs.find().pretty()
db.api_logs.count()
```

### 3. Logs Console
Les logs apparaissent dans la console:
```
[INFO] GET /api/v1/aventuriers - Status: 200 - Time: 45ms - Success: true
```

## 📊 Endpoints Disponibles

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/aventuriers` | Liste tous les aventuriers (pagination + filtres) |
| POST | `/api/v1/aventuriers` | Créer un aventurier |
| GET | `/api/v1/aventuriers/{id}` | Obtenir un aventurier par ID |
| PUT | `/api/v1/aventuriers/{id}` | Mise à jour complète |
| PATCH | `/api/v1/aventuriers/{id}` | Mise à jour partielle |
| DELETE | `/api/v1/aventuriers/{id}` | Supprimer un aventurier |

## 🐛 Résolution de Problèmes

### "Cannot resolve symbol 'mongodb'" dans IntelliJ
1. Clic droit sur le projet → Maven → Reload Project
2. Ou: Maven tool window → Reload (icône refresh)
3. Ou: File → Invalidate Caches / Restart

### Application ne démarre pas (MongoDB error)
**Cause:** MongoDB n'est pas démarré et l'application essaie de s'y connecter.

**Solution 1 - Démarrer MongoDB:**
```cmd
start_mongodb.cmd
```

**Solution 2 - Désactiver MongoDB:**
Commentez dans `application.yaml`:
```yaml
#  data:
#    mongodb:
#      host: localhost
#      port: 27017
#      database: adventures_logs
```

L'application fonctionnera et loggera seulement dans la console.

### Mauvaise version Java
**Erreur:** `UnsupportedClassVersionError` ou version incompatible

**Solution:**
1. Vérifier votre version Java:
```cmd
java -version
```

2. Si différent de Java 21, modifier dans `pom.xml`:
```xml
<properties>
    <java.version>17</java.version>  <!-- ou votre version -->
</properties>
```

3. Rebuild:
```cmd
mvnw.cmd clean install -DskipTests
```

## 📁 Structure du Projet

```
adventures/
├── 📄 pom.xml                          ✅ Spring Boot 3.4.3, Java 21
├── 🔧 start_mongodb.cmd                Démarrer MongoDB
├── 🧪 test_sql_nosql.ps1               Tests automatiques
│
├── 📚 Documentation/
│   ├── FIXES_APPLIED.md                Ce qui a été corrigé
│   ├── MONGODB_SETUP.md                Guide MongoDB
│   ├── SQL_NOSQL_TESTS.md              Tests détaillés
│   └── IMPLEMENTATION_SQL_NOSQL.md     Implémentation complète
│
└── src/main/
    ├── java/com/ynov/adventures/
    │   ├── aspect/
    │   │   └── ApiLoggingAspect.java   🔍 Intercepte tous les appels
    │   ├── config/
    │   │   └── AsyncConfig.java         ⚙️ Configuration async
    │   ├── controller/
    │   │   └── AventurierController.java 🎮 Endpoints REST
    │   ├── document/
    │   │   └── ApiLog.java              📝 Document MongoDB
    │   ├── domain/
    │   │   ├── Aventurier.java          💾 Entité JPA
    │   │   ├── Caracteristiques.java    
    │   │   └── Classe.java              
    │   ├── repository/
    │   │   ├── AventurierRepository.java 🗄️ Repository SQL
    │   │   └── ApiLogRepository.java     🗄️ Repository MongoDB
    │   └── service/
    │       ├── AventurierService.java   📦 Logique métier
    │       └── ApiLogService.java       📊 Service logging
    │
    └── resources/
        ├── application.yaml              ⚙️ Configuration
        └── data.sql                      📊 Données de test
```

## ✨ Fonctionnalités Implémentées

### SQL (H2)
- ✅ Base de données en mémoire
- ✅ Entité Aventurier avec JPA
- ✅ Repository avec méthodes de requête custom
- ✅ 7 aventuriers pré-chargés au démarrage
- ✅ CRUD complet
- ✅ Filtres par classe et niveau
- ✅ Pagination
- ✅ Console H2 accessible

### NoSQL (MongoDB)
- ✅ Document ApiLog
- ✅ Repository MongoDB
- ✅ Logging automatique de tous les appels API
- ✅ Capture des paramètres d'entrée/sortie
- ✅ Mesure du temps d'exécution
- ✅ Niveaux de log appropriés (INFO/WARN/ERROR)
- ✅ Mode optionnel (fonctionne sans MongoDB)

### Logging
- ✅ Aspect AOP qui intercepte tous les controllers
- ✅ Log dans MongoDB ET console
- ✅ INFO: Succès (2xx, 3xx)
- ✅ WARN: Erreurs client (400, 404, 422)
- ✅ ERROR: Erreurs serveur (5xx)
- ✅ Asynchrone (ne bloque pas les requêtes)

## 🎓 Commandes Maven Utiles

```cmd
# Nettoyer
mvnw.cmd clean

# Compiler
mvnw.cmd compile

# Tester
mvnw.cmd test

# Packager
mvnw.cmd package

# Installer
mvnw.cmd install

# Démarrer
mvnw.cmd spring-boot:run

# Tout en une fois (clean + compile + test + package)
mvnw.cmd clean install

# Sans tests
mvnw.cmd clean install -DskipTests

# Avec logs détaillés
mvnw.cmd clean install -X
```

## 🎉 Vous êtes prêt!

Lancez simplement:
```cmd
mvnw.cmd clean install -DskipTests
mvnw.cmd spring-boot:run
```

Puis testez:
```cmd
curl http://localhost:8080/api/v1/aventuriers
```

Ou ouvrez dans votre navigateur:
```
http://localhost:8080/h2-console
```

---

**Pour plus de détails, consultez:**
- `FIXES_APPLIED.md` - Corrections apportées
- `MONGODB_SETUP.md` - Configuration MongoDB
- `SQL_NOSQL_TESTS.md` - Tests détaillés
