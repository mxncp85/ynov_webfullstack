    # Configuration de Protection de Branche Master

## ✅ Pour configurer le merge automatique bloqué:

### Étape 1: Aller sur GitHub
1. Allez sur votre repository
2. Cliquez sur **Settings**
3. Allez dans **Branches** (left sidebar)
4. Sous "Branch protection rules", cliquez **Add rule**

### Étape 2: Configurer la règle
1. **Branch name pattern**: `master` (ou `main`)
2. ✅ Cochez: **Require a pull request before merging**
3. ✅ Cochez: **Require status checks to pass before merging**
4. Cherchez et sélectionnez: **build** (c'est le job du workflow)
5. ✅ Cochez: **Require branches to be up to date before merging**
6. Cliquez **Create** ou **Save changes**

### Étape 3: Votre workflow

```
develop (votre branche de travail)
   ↓ (push régulièrement ici)
   ↓
[Créer une Pull Request vers master]
   ↓
[GitHub Action se déclenche automatiquement] ✅
   ├─ Compile le code
   ├─ Lance les tests
   ├─ Valide tout
   └─ ✅ PASS ou ❌ FAIL
       ├─ SI PASS → Bouton "Merge" est actif ✅
       └─ SI FAIL → Bouton "Merge" est bloqué 🚫

```

## 🔄 Votre workflow Git

```bash
# 1. Créer la branche develop (une seule fois)
git checkout -b develop

# 2. Travailler sur develop
git add .
git commit -m "feature: ma nouvelle fonctionnalité"
git push origin develop

# 3. Quand vous êtes prêt à merger
# Créez une Pull Request sur GitHub de develop vers master

# 4. GitHub Action vérifie automatiquement:
#    - Build compile-t-il?
#    - Tests passent-ils?

# 5. Si tout passe → Cliquez "Merge pull request" ✅
#    Si ça échoue → Corrigez et repoussez sur develop 🔄

```

## 📋 Résumé

| Action | Résultat | Merge autorisé? |
|--------|----------|-----------------|
| Compilation réussie ✅ | Status: PASS | ✅ OUI |
| Tests réussis ✅ | Status: PASS | ✅ OUI |
| Compilation échouée ❌ | Status: FAIL | 🚫 NON |
| Tests échoués ❌ | Status: FAIL | 🚫 NON |

## 🎯 Avantages

- ✅ Jamais de code cassé sur `master`
- ✅ Validation automatique
- ✅ Traçabilité complète avec PR
- ✅ Code review possible avant merge
- ✅ Branche stable pour production
