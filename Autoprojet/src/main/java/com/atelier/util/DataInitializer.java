package com.atelier.util;

import com.atelier.dao.ClientDAO;
import com.atelier.dao.PieceDAO;
import com.atelier.dao.VehiculeDAO;
import com.atelier.entities.*;
import com.atelier.services.AtelierService;

import java.util.List;

public class DataInitializer {
    
    public static void initialiser(AtelierService service) {
        // Créer des clients
        service.creerClient("Dupont", "dupont@email.com", "0123456789");
        service.creerClient("Martin", "martin@email.com", "0234567890");
        service.creerClient("Bernard", "bernard@email.com", "0345678901");
        service.creerClient("Dubois", "dubois@email.com", "0456789012");
        service.creerClient("Moreau", "moreau@email.com", "0567890123");
        
        // Récupérer les clients créés
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = clientDAO.findAll();
        
        // Créer des véhicules
        service.creerVehicule(clients.get(0).getId(), "AB-123-CD", "Renault", "Clio");
        service.creerVehicule(clients.get(0).getId(), "EF-456-GH", "Peugeot", "208");
        service.creerVehicule(clients.get(1).getId(), "IJ-789-KL", "Citroën", "C3");
        service.creerVehicule(clients.get(2).getId(), "MN-012-OP", "Volkswagen", "Golf");
        service.creerVehicule(clients.get(3).getId(), "QR-345-ST", "Ford", "Fiesta");
        service.creerVehicule(clients.get(4).getId(), "UV-678-WX", "Opel", "Corsa");
        
        // Récupérer les véhicules créés
        VehiculeDAO vehiculeDAO = new VehiculeDAO();
        List<Vehicule> vehicules = vehiculeDAO.findAll();
        
        // Créer des pièces
        service.creerPiece("P001", "Plaquette de frein avant", 20, 45.50);
        service.creerPiece("P002", "Disque de frein", 15, 65.00);
        service.creerPiece("P003", "Filtre à huile", 50, 12.00);
        service.creerPiece("P004", "Bougie d'allumage", 30, 8.50);
        service.creerPiece("P005", "Batterie 12V", 10, 95.00);
        service.creerPiece("P006", "Pneu 195/65 R15", 25, 75.00);
        service.creerPiece("P007", "Amortisseur avant", 8, 120.00);
        service.creerPiece("P008", "Radiateur", 5, 180.00);
        
        // Récupérer les pièces créées
        PieceDAO pieceDAO = new PieceDAO();
        Piece piece1 = pieceDAO.findByRef("P001");
        Piece piece3 = pieceDAO.findByRef("P003");
        Piece piece5 = pieceDAO.findByRef("P005");
        Piece piece6 = pieceDAO.findByRef("P006");
        
        // Créer des ordres de réparation
        OrdreReparation or1 = service.creerOrdreReparation(vehicules.get(0).getId());
        service.ajouterLignePrestation(or1.getId(), "Remplacement plaquettes avant", 
            LignePrestation.TypePrestation.MAIN_D_OEUVRE, 1, 80.00, null);
        service.ajouterLignePrestation(or1.getId(), "Plaquette de frein avant", 
            LignePrestation.TypePrestation.PIECE, 2, 45.50, piece1.getId());
        
        OrdreReparation or2 = service.creerOrdreReparation(vehicules.get(1).getId());
        service.ajouterLignePrestation(or2.getId(), "Vidange moteur", 
            LignePrestation.TypePrestation.MAIN_D_OEUVRE, 1, 50.00, null);
        service.ajouterLignePrestation(or2.getId(), "Filtre à huile", 
            LignePrestation.TypePrestation.PIECE, 1, 12.00, piece3.getId());
        
        OrdreReparation or3 = service.creerOrdreReparation(vehicules.get(2).getId());
        service.changerEtatOR(or3.getId(), OrdreReparation.EtatReparation.ATELIER);
        service.ajouterLignePrestation(or3.getId(), "Remplacement batterie", 
            LignePrestation.TypePrestation.MAIN_D_OEUVRE, 1, 40.00, null);
        service.ajouterLignePrestation(or3.getId(), "Batterie 12V", 
            LignePrestation.TypePrestation.PIECE, 1, 95.00, piece5.getId());
        
        OrdreReparation or4 = service.creerOrdreReparation(vehicules.get(3).getId());
        service.changerEtatOR(or4.getId(), OrdreReparation.EtatReparation.TERMINE);
        service.ajouterLignePrestation(or4.getId(), "Remplacement pneus", 
            LignePrestation.TypePrestation.MAIN_D_OEUVRE, 1, 60.00, null);
        service.ajouterLignePrestation(or4.getId(), "Pneu 195/65 R15", 
            LignePrestation.TypePrestation.PIECE, 4, 75.00, piece6.getId());
    }
}

