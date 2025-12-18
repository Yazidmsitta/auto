# Guide de Compilation - Solutions

## Problème : Maven n'est pas reconnu

Plusieurs solutions sont disponibles :

## Solution 1 : Utiliser un IDE (RECOMMANDÉ - Le plus simple)

### IntelliJ IDEA (Gratuit)
1. Télécharger : https://www.jetbrains.com/idea/download/
2. Installer et ouvrir IntelliJ
3. **File** → **Open** → Sélectionner le dossier `Autoprojet`
4. IntelliJ détectera automatiquement Maven
5. Pour compiler : **Build** → **Build Project** (ou `Ctrl+F9`)
6. Pour exécuter : Clic droit sur `App.java` → **Run 'App.main()'**

### Eclipse
1. Télécharger : https://www.eclipse.org/downloads/
2. Installer et ouvrir Eclipse
3. **File** → **Import** → **Maven** → **Existing Maven Projects**
4. Sélectionner le dossier `Autoprojet`
5. Pour compiler : **Project** → **Build Project**
6. Pour exécuter : Clic droit sur `App.java` → **Run As** → **Java Application**

## Solution 2 : Installer Maven

### Étapes d'installation

1. **Télécharger Maven**
   - Aller sur : https://maven.apache.org/download.cgi
   - Télécharger `apache-maven-3.9.x-bin.zip`

2. **Extraire**
   - Extraire dans `C:\Tools\apache-maven-3.9.x` (ou autre emplacement)

3. **Configurer les variables d'environnement**
   - Ouvrir **Paramètres** → **Système** → **Variables d'environnement**
   - Créer `MAVEN_HOME` = `C:\Tools\apache-maven-3.9.x`
   - Ajouter à `Path` : `%MAVEN_HOME%\bin`

4. **Vérifier**
   - Ouvrir un **nouveau** PowerShell
   - Taper : `mvn -version`

5. **Compiler**
   ```powershell
   mvn clean compile
   mvn javafx:run
   ```

## Solution 3 : Utiliser le Wrapper Maven (Si installé)

Si Maven est installé mais pas dans le PATH, vous pouvez utiliser le wrapper :

```powershell
# Définir JAVA_HOME pour cette session
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Compiler
.\mvnw.cmd clean compile

# Exécuter
.\mvnw.cmd javafx:run
```

## Solution 4 : Compiler manuellement avec Java

Si vous avez des problèmes avec Maven, vous pouvez compiler manuellement :

```powershell
# Définir JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Créer le répertoire de sortie
New-Item -ItemType Directory -Force -Path "target\classes" | Out-Null

# Compiler (nécessite d'avoir les dépendances dans le classpath)
# Cette méthode est complexe, mieux vaut utiliser un IDE
```

## Recommandation

**Utilisez IntelliJ IDEA Community Edition** - C'est gratuit, facile à utiliser, et gère Maven automatiquement. C'est la solution la plus simple pour ce projet.

## Commandes utiles une fois Maven installé

```powershell
# Compiler
mvn clean compile

# Exécuter l'application
mvn javafx:run

# Créer un JAR
mvn clean package

# Nettoyer
mvn clean
```


