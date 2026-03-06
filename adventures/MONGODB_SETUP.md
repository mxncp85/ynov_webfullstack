# Configuration MongoDB pour le Logging

## Prérequis

Pour que les logs fonctionnent correctement, vous devez avoir MongoDB installé et en cours d'exécution.

### Installation de MongoDB (Windows)

1. **Télécharger MongoDB Community Server:**
   - Allez sur https://www.mongodb.com/try/download/community
   - Téléchargez la version Windows MSI
   - Installez avec les options par défaut

2. **Démarrer MongoDB comme service:**
   MongoDB devrait démarrer automatiquement après l'installation. Si ce n'est pas le cas:
   ```cmd
   net start MongoDB
   ```

### Démarrage Manuel de MongoDB (si non installé comme service)

```cmd
cd C:\Program Files\MongoDB\Server\7.0\bin
mongod --dbpath C:\data\db
```

### Installation avec Docker (Alternative)

Si vous préférez utiliser Docker:

```cmd
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

## Configuration de l'Application

La configuration MongoDB est définie dans `src/main/resources/application.yaml`:

```yaml
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: adventures_logs
```

## Démarrage en Mode sans MongoDB

Si vous souhaitez exécuter l'application sans MongoDB (les logs seront seulement dans la console):

1. Commentez la dépendance MongoDB dans `pom.xml`:
```xml
<!-- 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
-->
```

2. Ou ajoutez cette propriété dans `application.yaml`:
```yaml
spring:
  autoconfigure:
    exclude: org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
```

## Visualisation des Logs

### Avec MongoDB Compass (GUI)

1. Téléchargez MongoDB Compass: https://www.mongodb.com/try/download/compass
2. Connectez-vous à `mongodb://localhost:27017`
3. Accédez à la base de données `adventures_logs`
4. Consultez la collection `api_logs`

### Avec MongoDB Shell

```bash
mongosh
use adventures_logs
db.api_logs.find().pretty()
```

## Structure des Logs

Chaque appel d'API est enregistré avec les informations suivantes:

- `timestamp`: Date et heure de l'appel
- `method`: Méthode HTTP (GET, POST, PUT, PATCH, DELETE)
- `endpoint`: Pattern de l'endpoint
- `uri`: URI complète de la requête
- `pathVariables`: Variables de chemin extraites
- `requestParams`: Paramètres de requête
- `requestBody`: Corps de la requête (pour POST, PUT, PATCH)
- `responseBody`: Corps de la réponse
- `statusCode`: Code de statut HTTP
- `logLevel`: Niveau de log (INFO, WARN, ERROR)
- `errorMessage`: Message d'erreur (si applicable)
- `executionTimeMs`: Temps d'exécution en millisecondes
- `success`: Booléen indiquant le succès de l'opération

## Niveaux de Log

- **INFO**: Requêtes réussies (status 2xx, 3xx)
- **WARN**: Erreurs client (status 4xx)
- **ERROR**: Erreurs serveur (status 5xx)
