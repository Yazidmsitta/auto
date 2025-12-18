# Solution : Erreur JavaFX runtime components are missing

## Solution pour IntelliJ IDEA

### Méthode 1 : Configuration de Run (Recommandé)

1. **Clic droit sur `App.java`** → **Modify Run Configuration...** (ou **Edit Configurations...**)

2. Dans la section **VM options**, ajouter :
   ```
   --module-path "${PATH_TO_FX}" --add-modules javafx.controls,javafx.fxml
   ```

3. **OU** utiliser cette configuration complète :
   ```
   --module-path "${PATH_TO_FX}" --add-modules javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.runtime=ALL-UNNAMED
   ```

4. **Définir la variable d'environnement `PATH_TO_FX`** :
   - File → Settings → Build, Execution, Deployment → Build Tools → Maven → Runner
   - Ou : Run → Edit Configurations → Environment variables
   - Ajouter : `PATH_TO_FX` = chemin vers les modules JavaFX
   
   **OU plus simple** : Utiliser directement le chemin dans VM options :
   ```
   --module-path "C:\Users\VotreNom\.m2\repository\org\openjfx\javafx-controls\17.0.2\javafx-controls-17.0.2.jar;C:\Users\VotreNom\.m2\repository\org\openjfx\javafx-fxml\17.0.2\javafx-fxml-17.0.2.jar" --add-modules javafx.controls,javafx.fxml
   ```

### Méthode 2 : Utiliser le plugin JavaFX Maven (Plus simple)

Le projet utilise déjà le plugin JavaFX Maven. Utilisez cette commande dans le terminal intégré de IntelliJ :

```bash
mvn clean javafx:run
```

IntelliJ devrait avoir Maven intégré. Si non, utilisez la méthode 1.

### Méthode 3 : Configuration automatique (Le plus simple)

1. **File** → **Project Structure** (Ctrl+Alt+Shift+S)
2. **Project Settings** → **Libraries**
3. Vérifier que les dépendances JavaFX sont présentes
4. **Run** → **Edit Configurations**
5. Sélectionner votre configuration de run
6. Dans **VM options**, ajouter :
   ```
   --add-modules javafx.controls,javafx.fxml
   ```

## Solution pour Eclipse

### Méthode 1 : Run Configuration

1. **Clic droit sur `App.java`** → **Run As** → **Run Configurations...**

2. Créer une nouvelle configuration Java Application

3. Dans l'onglet **Arguments**, section **VM arguments**, ajouter :
   ```
   --module-path "${env_var:PATH_TO_FX}" --add-modules javafx.controls,javafx.fxml
   ```

4. **OU** trouver le chemin des JAR JavaFX dans `.m2/repository` et utiliser :
   ```
   --module-path "C:\Users\VotreNom\.m2\repository\org\openjfx\javafx-controls\17.0.2\javafx-controls-17.0.2.jar;C:\Users\VotreNom\.m2\repository\org\openjfx\javafx-fxml\17.0.2\javafx-fxml-17.0.2.jar" --add-modules javafx.controls,javafx.fxml
   ```

### Méthode 2 : Utiliser e(fx)clipse Plugin

1. **Help** → **Eclipse Marketplace**
2. Rechercher "e(fx)clipse"
3. Installer le plugin
4. Redémarrer Eclipse
5. Le plugin configurera JavaFX automatiquement

## Solution Universelle : Modifier le main() pour JavaFX 11+

Si les méthodes ci-dessus ne fonctionnent pas, on peut modifier le code pour détecter automatiquement les modules.

## Vérification

Après configuration, l'application devrait démarrer sans erreur JavaFX.


