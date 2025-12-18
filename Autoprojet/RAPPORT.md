# Rapport - Application de Gestion d'Atelier Automobile

## 1. Description du Projet

Cette application permet de gérer un atelier automobile en suivant le cycle complet : **Devis → Ordre de Réparation → Facture**. Elle offre une interface JavaFX pour gérer les clients, véhicules, ordres de réparation, pièces et factures.

## 2. Architecture Technique

### 2.1 Technologies Utilisées
- **Java 11** : Langage de programmation
- **JavaFX 17** : Interface graphique
- **Hibernate 5.6** : ORM pour la persistance des données
- **H2 Database** : Base de données embarquée
- **Maven** : Gestion des dépendances

### 2.2 Structure du Projet
```
src/
├── main/
│   ├── java/com/atelier/
│   │   ├── entities/          # Entités JPA/Hibernate
│   │   ├── dao/               # Data Access Objects
│   │   ├── services/          # Services métier
│   │   ├── controllers/       # Contrôleurs JavaFX
│   │   ├── util/              # Utilitaires
│   │   └── App.java           # Point d'entrée
│   └── resources/
│       ├── fxml/              # Fichiers FXML pour les vues
│       └── hibernate.cfg.xml  # Configuration Hibernate
```

## 3. Modèle de Données

### 3.1 Entités Principales

#### Client
- `id` : Identifiant unique
- `nom` : Nom du client
- `email` : Adresse email (unique)
- `telephone` : Numéro de téléphone
- Relation : Un client peut avoir plusieurs véhicules

#### Vehicule
- `id` : Identifiant unique
- `client_id` : Référence au client propriétaire
- `immatriculation` : Numéro d'immatriculation (unique, format validé)
- `marque` : Marque du véhicule
- `modele` : Modèle du véhicule
- Relation : Un véhicule appartient à un client, peut avoir plusieurs OR

#### OrdreReparation
- `id` : Identifiant unique
- `vehicule_id` : Référence au véhicule
- `date` : Date de création
- `etat` : État (DIAGNOSTIC, ATELIER, TERMINE)
- Relation : Un OR appartient à un véhicule, peut avoir plusieurs lignes de prestation, une facture

#### LignePrestation
- `id` : Identifiant unique
- `or_id` : Référence à l'ordre de réparation
- `description` : Description de la prestation
- `type` : Type (PIECE ou MAIN_D_OEUVRE)
- `quantite` : Quantité
- `prix` : Prix unitaire
- `piece_id` : Référence à la pièce (si type = PIECE)
- Relation : Une ligne appartient à un OR, peut référencer une pièce

#### Piece
- `id` : Identifiant unique
- `ref` : Référence unique de la pièce
- `nom` : Nom de la pièce
- `stock` : Quantité en stock (non négatif)
- `prix` : Prix unitaire
- Relation : Une pièce peut être utilisée dans plusieurs lignes de prestation

#### Facture
- `id` : Identifiant unique
- `or_id` : Référence à l'ordre de réparation (unique)
- `total` : Montant total
- `date` : Date de facturation
- Relation : Une facture correspond à un OR

## 4. Cycle Ordre de Réparation

### 4.1 Processus Complet

#### Étape 1 : Création de l'Ordre de Réparation
1. Un véhicule est pris en charge
2. Un nouvel Ordre de Réparation (OR) est créé avec l'état **DIAGNOSTIC**
3. L'OR est associé au véhicule et à son client

#### Étape 2 : Diagnostic et Ajout de Prestations
1. Le mécanicien effectue le diagnostic
2. Des lignes de prestation sont ajoutées à l'OR :
   - **Pièces** : Consommation automatique du stock (vérification du stock disponible)
   - **Main d'œuvre** : Prestations de réparation
3. Chaque ligne contient : description, type, quantité, prix unitaire

#### Étape 3 : Passage en Atelier
1. L'état de l'OR passe à **ATELIER**
2. Les réparations sont effectuées
3. Les pièces sont consommées du stock (déjà fait lors de l'ajout)

#### Étape 4 : Clôture et Facturation
1. L'état de l'OR passe à **TERMINE**
2. **Règle métier** : La clôture déclenche automatiquement la génération d'une facture
3. La facture est créée avec :
   - Le total calculé à partir des lignes de prestation
   - La date de facturation
   - L'association à l'OR

### 4.2 Exemple de Cycle Complet

**Scénario** : Remplacement des plaquettes de frein

1. **Création OR** : OR #1 créé pour véhicule "AB-123-CD" (état: DIAGNOSTIC)
2. **Ajout prestations** :
   - Main d'œuvre : "Remplacement plaquettes avant" - 1h × 80€ = 80€
   - Pièce : "Plaquette de frein avant" - 2 × 45.50€ = 91€
   - Stock de la pièce passe de 20 à 18
3. **Passage en atelier** : État → ATELIER
4. **Clôture** : État → TERMINE
5. **Facturation automatique** : Facture #1 générée avec total = 171€

## 5. Règles de Gestion Implémentées

### 5.1 Validation des Données
- **Immatriculation** : Format validé (AB-123-CD ou 1234-AB-56)
- **Stock de pièces** : Ne peut pas être négatif
- **Quantités et prix** : Doivent être positifs
- **Email client** : Unique dans la base

### 5.2 Gestion du Stock
- Lors de l'ajout d'une ligne de prestation de type PIECE :
  - Vérification du stock disponible
  - Consommation automatique du stock
  - Exception si stock insuffisant

### 5.3 Facturation
- **Règle automatique** : Un OR clôturé (état TERMINE) génère automatiquement une facture
- Une facture ne peut être créée qu'une seule fois par OR
- Le total est calculé automatiquement à partir des lignes de prestation

## 6. Interface Utilisateur

### 6.1 Vues Principales

#### Onglet "Ordres de Réparation en Cours"
- Liste des OR avec filtrage par état (TOUS, DIAGNOSTIC, ATELIER, TERMINE)
- Affichage : ID, Date, Véhicule, Client, État, Total
- Actions : Créer un nouvel OR, Voir détails, Changer l'état

#### Onglet "Détail Ordre de Réparation"
- Chargement d'un OR par son ID
- Affichage des informations : ID, Date, Véhicule, Client, État, Total
- Liste des prestations avec détails
- Ajout de nouvelles prestations (pièces ou main d'œuvre)
- Suppression de prestations

#### Onglet "Stock Pièces"
- Liste de toutes les pièces avec : ID, Référence, Nom, Stock, Prix
- Actions : Ajouter une pièce, Modifier le stock, Modifier le prix

#### Onglet "Facturation"
- Liste de toutes les factures générées
- Affichage : ID, Date, OR ID, Véhicule, Total
- Génération manuelle de facture pour un OR terminé

## 7. Fonctionnalités Développées

✅ Cycle complet Devis → OR → Facture  
✅ Association de pièces et main d'œuvre à une réparation  
✅ Mise à jour automatique du stock de pièces  
✅ Consultation de l'historique par véhicule  
✅ Filtre par état de réparation (diagnostic, atelier, terminé)  
✅ Validation des formats d'immatriculation  
✅ Contrôle des stocks non négatifs  
✅ Génération automatique de facture lors de la clôture  

## 8. Jeu de Données

Le jeu de données initialisé comprend :
- **5 clients** : Dupont, Martin, Bernard, Dubois, Moreau
- **6 véhicules** : Répartis entre les clients
- **8 pièces** : Plaquettes, disques, filtres, bougies, batterie, pneus, amortisseurs, radiateur
- **4 ordres de réparation** : Avec différents états et prestations
- **1 facture** : Générée automatiquement pour l'OR terminé

## 9. Utilisation

### 9.1 Lancement de l'Application
```bash
mvn clean javafx:run
```

### 9.2 Initialisation des Données
1. Menu "Données" → "Initialiser les données"
2. Les données de test sont créées automatiquement

### 9.3 Création d'un Nouvel OR
1. Onglet "Ordres de Réparation en Cours"
2. Cliquer sur "Nouvel OR"
3. Entrer l'ID du véhicule
4. L'OR est créé avec l'état DIAGNOSTIC

### 9.4 Ajout de Prestations
1. Onglet "Détail Ordre de Réparation"
2. Entrer l'ID de l'OR et cliquer "Charger"
3. Remplir le formulaire d'ajout de prestation
4. Sélectionner le type (PIECE ou MAIN_D_OEUVRE)
5. Si PIECE, sélectionner la pièce dans la liste
6. Cliquer "Ajouter"

### 9.5 Clôture et Facturation
1. Onglet "Ordres de Réparation en Cours"
2. Sélectionner un OR
3. Cliquer "Changer état"
4. Choisir "TERMINE"
5. La facture est générée automatiquement

## 10. Conclusion

L'application répond à tous les besoins spécifiés :
- Gestion complète du cycle OR
- Validation des règles métier
- Interface utilisateur intuitive
- Persistance des données avec Hibernate
- Jeu de données de test fourni

L'architecture modulaire permet une maintenance et une évolution faciles du système.

