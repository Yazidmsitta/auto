# Comment Exécuter l'Application dans l'IDE

## ⚡ Solution la PLUS RAPIDE

### Dans IntelliJ IDEA :

1. **Ouvrir le terminal intégré** (en bas de l'écran, onglet "Terminal")

2. **Taper cette commande** :
   ```bash
   mvn clean javafx:run
   ```

3. ✅ **C'est tout !** L'application devrait démarrer.

---

## 🔧 Si vous voulez utiliser le bouton "Run" vert

### IntelliJ IDEA :

1. **Run** → **Edit Configurations...** (ou clic droit sur `App.java` → **Modify Run Configuration...**)

2. Dans la section **VM options**, ajouter exactement ceci :
   ```
   --add-modules javafx.controls,javafx.fxml
   ```

3. Cliquer **OK**

4. Cliquer sur le bouton **Run** vert ▶️

### Eclipse :

1. **Clic droit sur `App.java`** → **Run As** → **Run Configurations...**

2. Créer une nouvelle configuration **Java Application**

3. Dans **VM arguments**, ajouter :
   ```
   --add-modules javafx.controls,javafx.fxml
   ```

4. **Run**

---

## ❓ Pourquoi cette erreur ?

JavaFX 11+ nécessite des modules Java spécifiques. L'IDE doit être configuré pour les charger.

---

## ✅ Vérification

Si ça ne fonctionne toujours pas :
1. Vérifiez que Maven a téléchargé les dépendances (dans `.m2/repository/org/openjfx/`)
2. Utilisez la commande `mvn javafx:run` qui fonctionne toujours


