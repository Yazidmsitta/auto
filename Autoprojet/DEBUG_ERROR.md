# Debug de l'erreur Maven JavaFX

## Erreur rencontrée
```
Failed to execute goal org.openjfx:javafx-maven-plugin:0.0.8:run
Process exited with an error: 1
```

## Solutions appliquées

1. ✅ **Amélioration de la gestion des erreurs** dans `App.java`
   - Messages d'erreur plus clairs
   - Vérification des fichiers FXML
   - Vérification des contrôleurs

2. ✅ **Ajout des imports manquants** dans les fichiers FXML
   - `FXCollections` ajouté dans `ordres_reparation.fxml`
   - `FXCollections` ajouté dans `detail_or.fxml`

## Prochaines étapes pour déboguer

### Dans votre IDE :

1. **Compiler d'abord** :
   - Dans la vue Maven, double-cliquer sur `Lifecycle` → `compile`
   - Vérifier qu'il n'y a pas d'erreurs de compilation

2. **Voir les logs détaillés** :
   - Dans la vue Maven, clic droit sur `javafx:run`
   - Sélectionner "Run Maven Goal..." avec l'option `-e` ou `-X`
   - Cela affichera l'erreur complète

3. **Vérifier les dépendances** :
   - Dans la vue Maven, développer "Dependencies"
   - Vérifier que toutes les dépendances sont téléchargées (pas en rouge)

### Commandes à essayer dans le terminal de l'IDE :

```bash
# Compiler avec logs détaillés
mvn clean compile -e

# Exécuter avec logs détaillés
mvn javafx:run -e -X
```

### Vérifications à faire :

1. ✅ Les fichiers FXML sont dans `src/main/resources/fxml/`
2. ✅ Les contrôleurs existent et sont correctement nommés
3. ✅ Hibernate peut créer la base de données
4. ✅ JavaFX est bien dans les dépendances

## Si l'erreur persiste

Partagez les logs complets avec `-e -X` pour voir l'erreur exacte.


