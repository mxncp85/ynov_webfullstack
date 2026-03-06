# 🔧 Résolution du Problème Java 21 vs Java 17

## ⚠️ Problème
IntelliJ IDEA est configuré pour Java 21, mais vous avez Java 17 d'installé.

## ✅ Solution en 5 Étapes

### Étape 1: Fermer IntelliJ IDEA
Fermez complètement IntelliJ IDEA (pas juste le projet, l'application entière).

### Étape 2: Exécuter le script de nettoyage
```cmd
cleanup_idea.cmd
```
Ce script va:
- Supprimer les caches d'IntelliJ
- Supprimer les répertoires de build
- Réinitialiser les configurations

### Étape 3: Attendre 10 secondes
Laissez le système traiter les suppressions.

### Étape 4: Rouvrir IntelliJ IDEA
Relancez IntelliJ IDEA.

Attendez que l'indexation soit complète (barre de progress en bas).

### Étape 5: Recharger Maven
- Menu: **Maven** → **Reload All Maven Projects**
- Ou clic droit sur le projet root → **Maven** → **Reload Projects**

---

## 📋 Alternative: Configuration Manuelle IntelliJ

Si le script ne suffit pas, faites ceci manuellement:

### 1. File → Project Structure
- Sélectionner l'onglet **Project**
- **Project SDK**: Vérifier que c'est **17** (pas 25)
- **Project language level**: Vérifier que c'est **17**
- Cliquer **OK**

### 2. File → Project Structure → Modules
- Sélectionner le module **adventures**
- **Module SDK**: Vérifier que c'est **17**
- **Language level**: Vérifier que c'est **17**
- Cliquer **OK**

### 3. File → Invalidate Caches
- Cocher **Invalidate and start the IDE**
- Cliquer **Invalidate and Restart**

### 4. Maven → Reload All Maven Projects

---

## ✅ Vérifications Finales

Après avoir suivi les étapes:

### Vérification 1: Check pom.xml
```xml
<java.version>17</java.version>
```

### Vérification 2: Check misc.xml
```xml
languageLevel="JDK_17"
```

### Vérification 3: Essayer le build
- Menu: **Build** → **Build Project**
- Ou: **mvnw.cmd clean install -DskipTests**

---

## 🚀 Si Tout Est Correct

Vous devriez voir:
```
[INFO] BUILD SUCCESS
```

Ensuite:
```cmd
mvnw.cmd spring-boot:run
```

Et accéder à: http://localhost:8080/h2-console

---

## 🐛 Troubleshooting

### Erreur: "Cannot find JDK 17"
- **File** → **Project Structure** → **Platform Settings** → **SDKs**
- Ajouter un JDK manuellement si absent

### Les caches ne disparaissent pas
- Ouvrir le gestionnaire de tâches
- Tuer le processus IntelliJ
- Attendre 10 secondes
- Relancer IntelliJ

### Les erreurs persistent
- Supprimer manuellement: `C:\Users\ASUS\.IntelliJIdea*\system\caches`
- Relancer IntelliJ

---

## 📞 Support

Si ça ne marche pas:
1. Partagez le contenu de **Project Structure → Project** 
2. Partagez le contenu de **Project Structure → Modules**
3. Je corrigerai les fichiers XML manuellement

---

**Une fois réglé, vous aurez:**
- ✅ Java 17 configuré partout
- ✅ pom.xml synchronized
- ✅ Build sans erreurs
