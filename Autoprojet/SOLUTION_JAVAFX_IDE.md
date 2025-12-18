# Solution Rapide : Erreur JavaFX dans l'IDE

## 🚀 Solution la PLUS SIMPLE (IntelliJ IDEA)

### Option 1 : Utiliser Maven depuis IntelliJ (RECOMMANDÉ)

1. Dans IntelliJ, ouvrir le terminal intégré (en bas)
2. Taper :
   ```bash
   mvn clean javafx:run
   ```
3. L'application devrait démarrer !

### Option 2 : Configurer la Run Configuration

1. **Run** → **Edit Configurations...** (ou clic droit sur `App.java` → **Modify Run Configuration...**)

2. Si aucune configuration n'existe, créer une nouvelle **Application** configuration :
   - **Name** : `App`
   - **Main class** : `com.atelier.App`
   - **Module** : `atelier-automobile`

3. Dans **VM options**, ajouter :
   ```
   --add-modules javafx.controls,javafx.fxml
   ```

4. **OU** (si l'option ci-dessus ne fonctionne pas) :
   ```
   --module-path "${USER_HOME}/.m2/repository/org/openjfx/javafx-controls/17.0.2/javafx-controls-17.0.2.jar;${USER_HOME}/.m2/repository/org/openjfx/javafx-fxml/17.0.2/javafx-fxml-17.0.2.jar" --add-modules javafx.controls,javafx.fxml
   ```

5. Cliquer **OK** et exécuter

## 🔧 Solution pour Eclipse

1. **Clic droit sur `App.java`** → **Run As** → **Run Configurations...**

2. Créer une nouvelle **Java Application** configuration

3. Dans l'onglet **Arguments**, section **VM arguments** :
   ```
   --add-modules javafx.controls,javafx.fxml
   ```

4. **OU** utiliser le chemin complet :
   ```
   --module-path "C:\Users\VotreNom\.m2\repository\org\openjfx\javafx-controls\17.0.2\javafx-controls-17.0.2.jar;C:\Users\VotreNom\.m2\repository\org\openjfx\javafx-fxml\17.0.2\javafx-fxml-17.0.2.jar" --add-modules javafx.controls,javafx.fxml
   ```

5. **Run**

## ✅ Vérification

Si vous voyez toujours l'erreur, vérifiez que :
1. Les dépendances Maven sont bien téléchargées (dans `.m2/repository/org/openjfx/`)
2. Le projet est bien reconnu comme projet Maven
3. Les dépendances sont dans le classpath

## 🎯 Solution Alternative : Utiliser JavaFX 11+ avec module path automatique

Si rien ne fonctionne, je peux modifier le code pour détecter automatiquement les modules JavaFX.


