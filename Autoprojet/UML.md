# Diagramme UML - Application de Gestion d'Atelier Automobile

## Diagramme de Classes

```
┌─────────────────────────────────────────────────────────────────┐
│                          CLIENT                                  │
├─────────────────────────────────────────────────────────────────┤
│ - id: Long                                                       │
│ - nom: String                                                    │
│ - email: String                                                 │
│ - telephone: String                                              │
│ - vehicules: List<Vehicule>                                     │
├─────────────────────────────────────────────────────────────────┤
│ + getId(): Long                                                 │
│ + getNom(): String                                              │
│ + getEmail(): String                                            │
│ + getTelephone(): String                                        │
│ + getVehicules(): List<Vehicule>                                │
└─────────────────────────────────────────────────────────────────┘
                            │
                            │ 1..*
                            │
┌───────────────────────────▼──────────────────────────────────────┐
│                        VEHICULE                                  │
├─────────────────────────────────────────────────────────────────┤
│ - id: Long                                                       │
│ - immatriculation: String                                       │
│ - marque: String                                                │
│ - modele: String                                                │
│ - client: Client                                                │
│ - ordresReparation: List<OrdreReparation>                       │
├─────────────────────────────────────────────────────────────────┤
│ + getId(): Long                                                 │
│ + getImmatriculation(): String                                  │
│ + getMarque(): String                                           │
│ + getModele(): String                                           │
│ + getClient(): Client                                           │
│ + getOrdresReparation(): List<OrdreReparation>                  │
└─────────────────────────────────────────────────────────────────┘
                            │
                            │ 1..*
                            │
┌───────────────────────────▼──────────────────────────────────────┐
│                    ORDRE_REPARATION                              │
├─────────────────────────────────────────────────────────────────┤
│ - id: Long                                                       │
│ - date: LocalDate                                               │
│ - etat: EtatReparation (DIAGNOSTIC, ATELIER, TERMINE)          │
│ - vehicule: Vehicule                                            │
│ - lignesPrestation: List<LignePrestation>                       │
│ - facture: Facture                                               │
├─────────────────────────────────────────────────────────────────┤
│ + getId(): Long                                                 │
│ + getDate(): LocalDate                                          │
│ + getEtat(): EtatReparation                                     │
│ + getVehicule(): Vehicule                                       │
│ + getLignesPrestation(): List<LignePrestation>                  │
│ + getFacture(): Facture                                         │
│ + getTotal(): double                                            │
└─────────────────────────────────────────────────────────────────┘
         │                                    │
         │ 1..*                               │ 1
         │                                    │
┌────────▼──────────────┐        ┌───────────▼──────────────┐
│  LIGNE_PRESTATION     │        │       FACTURE            │
├───────────────────────┤        ├──────────────────────────┤
│ - id: Long            │        │ - id: Long              │
│ - description: String │        │ - total: Double          │
│ - type: TypePrestation│        │ - date: LocalDate        │
│   (PIECE, MAIN_D_OEUVRE)       │ - ordreReparation:       │
│ - quantite: Integer   │        │   OrdreReparation       │
│ - prix: Double        │        ├──────────────────────────┤
│ - ordreReparation:    │        │ + getId(): Long          │
│   OrdreReparation     │        │ + getTotal(): Double     │
│ - piece: Piece        │        │ + getDate(): LocalDate   │
├───────────────────────┤        │ + getOrdreReparation():  │
│ + getId(): Long       │        │   OrdreReparation       │
│ + getDescription():   │        └──────────────────────────┘
│   String              │
│ + getType():          │
│   TypePrestation      │
│ + getQuantite():      │
│   Integer             │
│ + getPrix(): Double   │
│ + getSousTotal():     │
│   double              │
└───────────────────────┘
         │
         │ 0..1
         │
┌────────▼──────────────┐
│        PIECE          │
├───────────────────────┤
│ - id: Long            │
│ - ref: String         │
│ - nom: String         │
│ - stock: Integer      │
│ - prix: Double        │
│ - lignesPrestation:   │
│   List<LignePrestation>│
├───────────────────────┤
│ + getId(): Long       │
│ + getRef(): String    │
│ + getNom(): String    │
│ + getStock(): Integer │
│ + getPrix(): Double   │
│ + consommer(int): void│
│ + ajouterStock(int):  │
│   void                │
└───────────────────────┘
```

## Relations

1. **Client ↔ Vehicule** : 1..* (Un client peut avoir plusieurs véhicules)
2. **Vehicule ↔ OrdreReparation** : 1..* (Un véhicule peut avoir plusieurs OR)
3. **OrdreReparation ↔ LignePrestation** : 1..* (Un OR contient plusieurs lignes)
4. **OrdreReparation ↔ Facture** : 1..1 (Un OR génère une facture)
5. **LignePrestation ↔ Piece** : 0..1 (Une ligne peut référencer une pièce)
6. **Piece ↔ LignePrestation** : 1..* (Une pièce peut être utilisée plusieurs fois)

## Diagramme de Séquence - Cycle OR Complet

```
Client          Interface        Service          DAO            Base de données
  │                 │               │              │                    │
  │  Créer OR       │               │              │                    │
  ├─────────────────>│               │              │                    │
  │                 ├──────────────>│              │                    │
  │                 │               ├─────────────>│                    │
  │                 │               │              ├───────────────────>│
  │                 │               │              │<───────────────────┤
  │                 │               │<─────────────┤                    │
  │                 │<──────────────┤              │                    │
  │<─────────────────┤               │              │                    │
  │                 │               │              │                    │
  │  Ajouter        │               │              │                    │
  │  Prestation     │               │              │                    │
  ├─────────────────>│               │              │                    │
  │                 ├──────────────>│              │                    │
  │                 │               ├─────────────>│                    │
  │                 │               │              ├───────────────────>│
  │                 │               │              │<───────────────────┤
  │                 │               │              ├───────────────────>│ (Mise à jour stock)
  │                 │               │              │<───────────────────┤
  │                 │               │<─────────────┤                    │
  │                 │<──────────────┤              │                    │
  │<─────────────────┤               │              │                    │
  │                 │               │              │                    │
  │  Changer état   │               │              │                    │
  │  → TERMINE      │               │              │                    │
  ├─────────────────>│               │              │                    │
  │                 ├──────────────>│              │                    │
  │                 │               ├─────────────>│                    │
  │                 │               │              ├───────────────────>│
  │                 │               │              │<───────────────────┤
  │                 │               │  Générer     │                    │
  │                 │               │  Facture     │                    │
  │                 │               ├─────────────>│                    │
  │                 │               │              ├───────────────────>│
  │                 │               │              │<───────────────────┤
  │                 │               │<─────────────┤                    │
  │                 │<──────────────┤              │                    │
  │<─────────────────┤               │              │                    │
```

## Diagramme de Cas d'Utilisation

```
┌─────────────────────────────────────────────────────────────┐
│                    Gestionnaire Atelier                      │
└─────────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│ Gérer        │   │ Gérer        │   │ Gérer        │
│ Clients      │   │ Véhicules    │   │ OR           │
└──────────────┘   └──────────────┘   └──────────────┘
        │                   │                   │
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│ Créer Client│   │ Créer        │   │ Créer OR     │
│ Modifier     │   │ Véhicule     │   │ Ajouter      │
│              │   │              │   │ Prestation   │
│              │   │              │   │ Changer État │
└──────────────┘   └──────────────┘   └──────────────┘
                            │
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│ Gérer        │   │ Consulter    │   │ Générer      │
│ Stock        │   │ Historique   │   │ Facture      │
└──────────────┘   └──────────────┘   └──────────────┘
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│ Ajouter      │   │ Par Véhicule │   │ Automatique  │
│ Pièce        │   │ Par Client   │   │ Manuel       │
│ Modifier     │   │              │   │              │
│ Stock        │   │              │   │              │
└──────────────┘   └──────────────┘   └──────────────┘
```

## Diagramme d'État - Ordre de Réparation

```
     [Création]
         │
         ▼
    ┌─────────┐
    │DIAGNOSTIC│
    └────┬────┘
         │
         │ Changer état
         ▼
    ┌─────────┐
    │ ATELIER │
    └────┬────┘
         │
         │ Changer état
         ▼
    ┌─────────┐
    │ TERMINE │ ──────► [Génération Facture]
    └─────────┘
```

## Architecture en Couches

```
┌─────────────────────────────────────────────────────────┐
│                    COUCHE PRÉSENTATION                    │
│  (JavaFX Controllers + FXML)                             │
│  - MainController                                        │
│  - OrdresReparationController                            │
│  - DetailORController                                    │
│  - StockPiecesController                                 │
│  - FacturationController                                 │
└───────────────────────┬───────────────────────────────────┘
                        │
┌───────────────────────▼───────────────────────────────────┐
│                    COUCHE SERVICE                         │
│  (Logique Métier)                                         │
│  - AtelierService                                         │
│    • Validation des règles métier                         │
│    • Gestion du cycle OR                                  │
│    • Contrôle des stocks                                  │
│    • Génération de factures                              │
└───────────────────────┬───────────────────────────────────┘
                        │
┌───────────────────────▼───────────────────────────────────┐
│                    COUCHE ACCÈS DONNÉES                  │
│  (DAO Pattern)                                            │
│  - ClientDAO                                              │
│  - VehiculeDAO                                            │
│  - OrdreReparationDAO                                     │
│  - PieceDAO                                               │
│  - FactureDAO                                             │
│  - LignePrestationDAO                                     │
└───────────────────────┬───────────────────────────────────┘
                        │
┌───────────────────────▼───────────────────────────────────┐
│                    COUCHE PERSISTANCE                     │
│  (Hibernate ORM)                                          │
│  - HibernateUtil                                          │
│  - Configuration Hibernate                                │
└───────────────────────┬───────────────────────────────────┘
                        │
┌───────────────────────▼───────────────────────────────────┐
│                    BASE DE DONNÉES                        │
│  (H2 Database)                                            │
│  - Tables: clients, vehicules, ordres_reparation,          │
│    lignes_prestation, pieces, factures                    │
└───────────────────────────────────────────────────────────┘
```

