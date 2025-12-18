# Application de Gestion d'Atelier Automobile

Application JavaFX pour la gestion complète d'un atelier automobile : clients, véhicules, ordres de réparation, pièces et factures.

## Fonctionnalités

- ✅ Cycle complet : Devis → Ordre de Réparation → Facture
- ✅ Gestion des clients et véhicules
- ✅ Association de pièces et main d'œuvre aux réparations
- ✅ Gestion du stock de pièces avec contrôle automatique
- ✅ Filtrage des ordres de réparation par état (DIAGNOSTIC, ATELIER, TERMINE)
- ✅ Génération automatique de factures lors de la clôture d'un OR
- ✅ Validation des formats d'immatriculation
- ✅ Consultation de l'historique par véhicule

## Prérequis

- Java 11 ou supérieur (JDK)
- Maven 3.6 ou supérieur (optionnel - le wrapper Maven est inclus)

## Installation

### Option 1 : Utiliser le Wrapper Maven (Recommandé - Pas d'installation nécessaire)

Le projet inclut un wrapper Maven (`mvnw.cmd` pour Windows). Il téléchargera automatiquement Maven si nécessaire.

1. Cloner ou télécharger le projet
2. Ouvrir un terminal dans le répertoire du projet
3. Compiler le projet :
```bash
.\mvnw.cmd clean compile
```

### Option 2 : Installer Maven (Si vous préférez utiliser `mvn` directement)

1. **Télécharger Maven** : https://maven.apache.org/download.cgi
2. **Extraire** l'archive dans un dossier (ex: `C:\Program Files\Apache\Maven`)
3. **Configurer les variables d'environnement** :
   - Créer `MAVEN_HOME` = `C:\Program Files\Apache\Maven`
   - Ajouter `%MAVEN_HOME%\bin` à la variable `Path`
4. **Vérifier** : Ouvrir un nouveau terminal et taper `mvn -version`

### Option 3 : Utiliser un IDE (IntelliJ IDEA, Eclipse, NetBeans)

Les IDE modernes peuvent gérer Maven automatiquement :
- **IntelliJ IDEA** : Ouvrir le projet, Maven sera détecté automatiquement
- **Eclipse** : Importer comme projet Maven existant
- **NetBeans** : Ouvrir le projet, NetBeans gère Maven automatiquement

## Exécution

### Méthode la plus simple (Windows) :
Double-cliquer sur `run.bat` ou exécuter dans PowerShell :
```powershell
.\run.ps1
```

### Avec le Wrapper Maven :
```bash
.\mvnw.cmd javafx:run
```

### Avec Maven installé :
```bash
mvn javafx:run
```

### Avec un IDE :
- **IntelliJ IDEA** : Clic droit sur `App.java` → Run 'App.main()'
- **Eclipse** : Clic droit sur `App.java` → Run As → Java Application
- **NetBeans** : Clic droit sur le projet → Run

> **Note** : Si vous voyez "mvn is not recognized", utilisez `.\mvnw.cmd` à la place de `mvn`, ou consultez `INSTALLATION.md` pour installer Maven.

## Initialisation des Données

Au premier lancement, utiliser le menu :
- **Données** → **Initialiser les données**

Cela créera :
- 5 clients
- 6 véhicules
- 8 pièces
- 4 ordres de réparation avec différents états
- 1 facture (générée automatiquement)

## Structure du Projet

```
src/main/java/com/atelier/
├── entities/          # Entités JPA/Hibernate
├── dao/               # Data Access Objects
├── services/          # Services métier
├── controllers/       # Contrôleurs JavaFX
├── util/              # Utilitaires
└── App.java          # Point d'entrée

src/main/resources/
├── fxml/              # Fichiers FXML pour les vues
└── hibernate.cfg.xml  # Configuration Hibernate
```

## Utilisation

### Créer un Ordre de Réparation

1. Aller dans l'onglet "Ordres de Réparation en Cours"
2. Cliquer sur "Nouvel OR"
3. Entrer l'ID du véhicule
4. L'OR est créé avec l'état DIAGNOSTIC

### Ajouter des Prestations

1. Aller dans l'onglet "Détail Ordre de Réparation"
2. Entrer l'ID de l'OR et cliquer "Charger"
3. Remplir le formulaire :
   - Description
   - Type (PIECE ou MAIN_D_OEUVRE)
   - Si PIECE : sélectionner la pièce
   - Quantité et prix
4. Cliquer "Ajouter"

### Gérer le Stock

1. Aller dans l'onglet "Stock Pièces"
2. Voir la liste de toutes les pièces
3. Utiliser "Nouvelle pièce" pour ajouter
4. Utiliser "Modifier stock" ou "Modifier prix" pour mettre à jour

### Clôturer un OR et Générer une Facture

1. Aller dans l'onglet "Ordres de Réparation en Cours"
2. Sélectionner un OR
3. Cliquer "Changer état"
4. Choisir "TERMINE"
5. La facture est générée automatiquement

## Règles de Gestion

- **Stock non négatif** : Le stock de pièces ne peut pas être négatif
- **Format d'immatriculation** : Validé (AB-123-CD ou 1234-AB-56)
- **Quantités et prix positifs** : Tous les montants et quantités doivent être positifs
- **Facturation automatique** : Un OR clôturé génère automatiquement une facture
- **Consommation de stock** : L'ajout d'une pièce consomme automatiquement le stock

## Base de Données

L'application utilise H2 Database (base de données embarquée).
Les données sont stockées dans le fichier `./data/atelier.mv.db`

## Documentation

- **RAPPORT.md** : Rapport détaillé du projet
- **UML.md** : Diagrammes UML (classes, séquences, cas d'utilisation)

## Technologies

- Java 11
- JavaFX 17
- Hibernate 5.6
- H2 Database 2.1
- Maven

## Auteur

Projet réalisé dans le cadre du Projet 16 - Application de gestion d'un atelier automobile

