package com.atelier.services;

import com.atelier.dao.*;
import com.atelier.entities.*;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class AtelierService {
    private ClientDAO clientDAO = new ClientDAO();
    private VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private OrdreReparationDAO ordreReparationDAO = new OrdreReparationDAO();
    private PieceDAO pieceDAO = new PieceDAO();
    private FactureDAO factureDAO = new FactureDAO();
    private LignePrestationDAO lignePrestationDAO = new LignePrestationDAO();
    
    // Format d'immatriculation: AB-123-CD ou 1234-AB-56
    private static final Pattern IMMATRICULATION_PATTERN = 
        Pattern.compile("^[A-Z]{2}-\\d{3}-[A-Z]{2}$|^\\d{4}-[A-Z]{2}-\\d{2}$");
    
    // Client operations
    public void creerClient(String nom, String email, String telephone) {
        Client client = new Client(nom, email, telephone);
        clientDAO.save(client);
    }
    
    public List<Client> getTousLesClients() {
        return clientDAO.findAll();
    }
    
    // Vehicule operations
    public void creerVehicule(Long clientId, String immatriculation, String marque, String modele) {
        if (!validerImmatriculation(immatriculation)) {
            throw new IllegalArgumentException("Format d'immatriculation invalide. Format attendu: AB-123-CD ou 1234-AB-56");
        }
        
        Client client = clientDAO.findById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client introuvable");
        }
        
        Vehicule vehicule = new Vehicule(client, immatriculation, marque, modele);
        vehiculeDAO.save(vehicule);
    }
    
    public boolean validerImmatriculation(String immatriculation) {
        return immatriculation != null && IMMATRICULATION_PATTERN.matcher(immatriculation).matches();
    }
    
    public List<Vehicule> getTousLesVehicules() {
        return vehiculeDAO.findAll();
    }
    
    public List<Vehicule> getVehiculesParClient(Long clientId) {
        return vehiculeDAO.findByClientId(clientId);
    }
    
    // Ordre de réparation operations
    public OrdreReparation creerOrdreReparation(Long vehiculeId) {
        Vehicule vehicule = vehiculeDAO.findById(vehiculeId);
        if (vehicule == null) {
            throw new IllegalArgumentException("Véhicule introuvable");
        }
        
        OrdreReparation or = new OrdreReparation();
        or.setVehicule(vehicule);
        or.setDate(LocalDate.now());
        or.setEtat(OrdreReparation.EtatReparation.DIAGNOSTIC);
        
        ordreReparationDAO.save(or);
        return or;
    }
    
    public void changerEtatOR(Long orId, OrdreReparation.EtatReparation nouvelEtat) {
        OrdreReparation or = ordreReparationDAO.findById(orId);
        if (or == null) {
            throw new IllegalArgumentException("Ordre de réparation introuvable");
        }
        
        or.setEtat(nouvelEtat);
        
        // Règle: OR clôturé implique génération d'une facture
        if (nouvelEtat == OrdreReparation.EtatReparation.TERMINE && or.getFacture() == null) {
            genererFacture(orId);
        }
        
        ordreReparationDAO.update(or);
    }
    
    public List<OrdreReparation> getOrdresReparationEnCours() {
        return ordreReparationDAO.findEnCours();
    }
    
    public List<OrdreReparation> getOrdresReparationParEtat(OrdreReparation.EtatReparation etat) {
        return ordreReparationDAO.findByEtat(etat);
    }
    
    public List<OrdreReparation> getOrdresReparationParVehicule(Long vehiculeId) {
        return ordreReparationDAO.findByVehiculeId(vehiculeId);
    }
    
    // Ligne prestation operations
    public void ajouterLignePrestation(Long orId, String description, 
                                      LignePrestation.TypePrestation type, 
                                      Integer quantite, Double prix, Long pieceId) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        if (prix <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif");
        }
        
        OrdreReparation or = ordreReparationDAO.findById(orId);
        if (or == null) {
            throw new IllegalArgumentException("Ordre de réparation introuvable");
        }
        
        LignePrestation ligne = new LignePrestation();
        ligne.setOrdreReparation(or);
        ligne.setDescription(description);
        ligne.setType(type);
        ligne.setQuantite(quantite);
        ligne.setPrix(prix);
        
        // Si c'est une pièce, associer la pièce et consommer le stock
        if (type == LignePrestation.TypePrestation.PIECE && pieceId != null) {
            Piece piece = pieceDAO.findById(pieceId);
            if (piece == null) {
                throw new IllegalArgumentException("Pièce introuvable");
            }
            
            // Vérifier et consommer le stock
            if (piece.getStock() < quantite) {
                throw new IllegalArgumentException(
                    "Stock insuffisant. Stock disponible: " + piece.getStock() + ", demandé: " + quantite);
            }
            
            piece.consommer(quantite);
            pieceDAO.update(piece);
            
            ligne.setPiece(piece);
        }
        
        lignePrestationDAO.save(ligne);
    }
    
    public void supprimerLignePrestation(Long ligneId) {
        LignePrestation ligne = new LignePrestation();
        ligne.setId(ligneId);
        lignePrestationDAO.delete(ligne);
    }
    
    // Piece operations
    public void creerPiece(String ref, String nom, Integer stock, Double prix) {
        if (stock < 0) {
            throw new IllegalArgumentException("Le stock ne peut pas être négatif");
        }
        if (prix <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif");
        }
        
        Piece piece = new Piece(ref, nom, stock, prix);
        pieceDAO.save(piece);
    }
    
    public void mettreAJourStock(Long pieceId, Integer nouveauStock) {
        if (nouveauStock < 0) {
            throw new IllegalArgumentException("Le stock ne peut pas être négatif");
        }
        
        Piece piece = pieceDAO.findById(pieceId);
        if (piece == null) {
            throw new IllegalArgumentException("Pièce introuvable");
        }
        
        piece.setStock(nouveauStock);
        pieceDAO.update(piece);
    }
    
    public List<Piece> getToutesLesPieces() {
        return pieceDAO.findAll();
    }
    
    // Facture operations
    public Facture genererFacture(Long orId) {
        OrdreReparation or = ordreReparationDAO.findById(orId);
        if (or == null) {
            throw new IllegalArgumentException("Ordre de réparation introuvable");
        }
        
        if (or.getFacture() != null) {
            throw new IllegalArgumentException("Une facture existe déjà pour cet ordre de réparation");
        }
        
        double total = or.getTotal();
        Facture facture = new Facture(or, total, LocalDate.now());
        factureDAO.save(facture);
        
        or.setFacture(facture);
        ordreReparationDAO.update(or);
        
        return facture;
    }
    
    public List<Facture> getToutesLesFactures() {
        return factureDAO.findAll();
    }
}



