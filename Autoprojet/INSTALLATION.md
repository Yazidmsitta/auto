# Guide d'Installation - Application de Gestion d'Atelier Automobile

## Problème : "mvn is not recognized"

Si vous voyez cette erreur, cela signifie que Maven n'est pas installé ou n'est pas dans votre PATH. Voici plusieurs solutions :

## Solution 1 : Utiliser le Wrapper Maven (Le plus simple)

Le projet inclut un wrapper Maven qui fonctionne sans installation :

### Windows (PowerShell ou CMD)
```bash
.\mvnw.cmd clean compile
.\mvnw.cmd javafx:run
```

Le wrapper téléchargera automatiquement Maven la première fois.

## Solution 2 : Installer Maven sur Windows

### Étape 1 : Télécharger Maven
1. Aller sur https://maven.apache.org/download.cgi
2. Télécharger `apache-maven-3.9.x-bin.zip` (version binaire)

### Étape 2 : Extraire Maven
1. Extraire le fichier ZIP dans un dossier, par exemple :
   - `C:\Program Files\Apache\Maven`
   - Ou `C:\Tools\apache-maven-3.9.x`

### Étape 3 : Configurer les Variables d'Environnement

#### Méthode 1 : Via l'Interface Graphique
1. Ouvrir **Paramètres** → **Système** → **À propos de** → **Paramètres système avancés**
2. Cliquer sur **Variables d'environnement**
3. Dans **Variables système**, cliquer sur **Nouveau**
4. Nom : `MAVEN_HOME`
5. Valeur : `C:\Program Files\Apache\Maven` (ou votre chemin)
6. Trouver la variable `Path` dans **Variables système**
7. Cliquer sur **Modifier** → **Nouveau**
8. Ajouter : `%MAVEN_HOME%\bin`
9. Cliquer **OK** sur toutes les fenêtres

#### Méthode 2 : Via PowerShell (Administrateur)
```powershell
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\Program Files\Apache\Maven", "Machine")
$path = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
[System.Environment]::SetEnvironmentVariable("Path", "$path;C:\Program Files\Apache\Maven\bin", "Machine")
```

### Étape 4 : Vérifier l'Installation
1. **Fermer tous les terminaux ouverts**
2. Ouvrir un **nouveau terminal** (PowerShell ou CMD)
3. Taper :
```bash
mvn -version
```

Vous devriez voir quelque chose comme :
```
Apache Maven 3.9.x
Maven home: C:\Program Files\Apache\Maven
Java version: 11.x.x
```

## Solution 3 : Utiliser un IDE

### IntelliJ IDEA
1. Télécharger IntelliJ IDEA Community (gratuit) : https://www.jetbrains.com/idea/
2. Installer et ouvrir IntelliJ
3. **File** → **Open** → Sélectionner le dossier du projet
4. IntelliJ détectera automatiquement Maven
5. Si Maven n'est pas installé, IntelliJ proposera de télécharger un wrapper
6. Pour exécuter : Clic droit sur `App.java` → **Run 'App.main()'**

### Eclipse
1. Télécharger Eclipse IDE for Java Developers : https://www.eclipse.org/downloads/
2. Installer et ouvrir Eclipse
3. **File** → **Import** → **Maven** → **Existing Maven Projects**
4. Sélectionner le dossier du projet
5. Eclipse gérera Maven automatiquement
6. Pour exécuter : Clic droit sur `App.java` → **Run As** → **Java Application**

### NetBeans
1. Télécharger NetBeans : https://netbeans.apache.org/download/
2. Installer et ouvrir NetBeans
3. **File** → **Open Project** → Sélectionner le dossier du projet
4. NetBeans gérera Maven automatiquement
5. Pour exécuter : Clic droit sur le projet → **Run**

## Solution 4 : Vérifier Java

Maven nécessite Java. Vérifiez que Java est installé :

```bash
java -version
```

Si Java n'est pas installé :
1. Télécharger JDK 11 ou supérieur : https://adoptium.net/ ou https://www.oracle.com/java/technologies/downloads/
2. Installer le JDK
3. Configurer `JAVA_HOME` :
   - Variable : `JAVA_HOME`
   - Valeur : `C:\Program Files\Java\jdk-11` (ou votre chemin JDK)

## Commandes Utiles

### Compiler le projet
```bash
# Avec wrapper
.\mvnw.cmd clean compile

# Avec Maven installé
mvn clean compile
```

### Exécuter l'application
```bash
# Avec wrapper
.\mvnw.cmd javafx:run

# Avec Maven installé
mvn javafx:run
```

### Créer un JAR exécutable
```bash
.\mvnw.cmd clean package
java -jar target/atelier-automobile-1.0-SNAPSHOT.jar
```

## Dépannage

### Erreur : "JAVA_HOME not found"
- Vérifier que `JAVA_HOME` est défini et pointe vers le JDK (pas le JRE)
- Redémarrer le terminal après modification

### Erreur : "mvnw.cmd not found"
- Vérifier que vous êtes dans le répertoire du projet
- Vérifier que le fichier `mvnw.cmd` existe

### Erreur : "Permission denied" (Linux/Mac)
- Rendre le script exécutable : `chmod +x mvnw`

### Maven ne se télécharge pas automatiquement
- Vérifier votre connexion Internet
- Vérifier que PowerShell peut exécuter des scripts (si nécessaire : `Set-ExecutionPolicy RemoteSigned`)

## Besoin d'Aide ?

Si vous rencontrez toujours des problèmes :
1. Vérifier que Java est installé : `java -version`
2. Vérifier que vous êtes dans le bon répertoire
3. Essayer avec un IDE (IntelliJ, Eclipse, NetBeans)
4. Vérifier les logs d'erreur pour plus de détails



