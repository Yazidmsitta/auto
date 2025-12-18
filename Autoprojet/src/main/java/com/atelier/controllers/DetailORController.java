package com.atelier.controllers;

import com.atelier.dao.OrdreReparationDAO;
import com.atelier.entities.LignePrestation;
import com.atelier.entities.OrdreReparation;
import com.atelier.entities.Piece;
import com.atelier.services.AtelierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class DetailORController {
    @FXML
    private ComboBox<OrdreReparation> orCombo;
    
    @FXML
    private Label labelId;
    
    @FXML
    private Label labelDate;
    
    @FXML
    private Label labelVehicule;
    
    @FXML
    private Label labelClient;
    
    @FXML
    private Label labelEtat;
    
    @FXML
    private Label labelTotal;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private ComboBox<String> typeCombo;
    
    @FXML
    private ComboBox<Piece> pieceCombo;
    
    @FXML
    private TextField quantiteField;
    
    @FXML
    private TextField prixField;
    
    @FXML
    private TableView<LignePrestation> tablePrestations;
    
    @FXML
    private Button btnSupprimer;
    
    private AtelierService atelierService;
    private OrdreReparation ordreReparation;
    private ObservableList<LignePrestation> prestationsList = FXCollections.observableArrayList();
    
    public void setAtelierService(AtelierService service) {
        this.atelierService = service;
        initialiserComboBox();
        initialiserTable();
        chargerPieces();
        chargerOrdresReparation();
    }
    
    private void initialiserComboBox() {
        typeCombo.setItems(FXCollections.observableArrayList("PIECE", "MAIN_D_OEUVRE"));
        
        // Configurer le ComboBox pour afficher les OR
        orCombo.setCellFactory(param -> new ListCell<OrdreReparation>() {
            @Override
            protected void updateItem(OrdreReparation or, boolean empty) {
                super.updateItem(or, empty);
                if (empty || or == null) {
                    setText(null);
                } else {
                    setText("OR #" + or.getId() + " - " + or.getVehicule().getImmatriculation() + " (" + or.getEtat() + ")");
                }
            }
        });
        
        orCombo.setButtonCell(new ListCell<OrdreReparation>() {
            @Override
            protected void updateItem(OrdreReparation or, boolean empty) {
                super.updateItem(or, empty);
                if (empty || or == null) {
                    setText(null);
                } else {
                    setText("OR #" + or.getId() + " - " + or.getVehicule().getImmatriculation());
                }
            }
        });
    }
    
    private void chargerOrdresReparation() {
        List<OrdreReparation> ordres = new java.util.ArrayList<>();
        ordres.addAll(atelierService.getOrdresReparationEnCours());
        ordres.addAll(atelierService.getOrdresReparationParEtat(OrdreReparation.EtatReparation.TERMINE));
        orCombo.setItems(FXCollections.observableArrayList(ordres));
    }
    
    // Méthode publique pour actualiser le ComboBox depuis d'autres contrôleurs
    public void actualiserListeOR() {
        chargerOrdresReparation();
    }
    
    @FXML
    private void actualiser() {
        chargerOrdresReparation();
        chargerPieces();
        if (ordreReparation != null) {
            // Recharger l'OR pour avoir les données à jour
            OrdreReparationDAO dao = new OrdreReparationDAO();
            ordreReparation = dao.findById(ordreReparation.getId());
            afficherDetails();
            chargerPrestations();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void initialiserTable() {
        TableColumn<LignePrestation, ?> colDesc = tablePrestations.getColumns().get(0);
        ((TableColumn<LignePrestation, String>) colDesc).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        
        TableColumn<LignePrestation, ?> colType = tablePrestations.getColumns().get(1);
        ((TableColumn<LignePrestation, String>) colType).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType().toString()));
        
        TableColumn<LignePrestation, ?> colQte = tablePrestations.getColumns().get(2);
        ((TableColumn<LignePrestation, Integer>) colQte).setCellValueFactory(
            data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getQuantite()));
        
        TableColumn<LignePrestation, ?> colPrix = tablePrestations.getColumns().get(3);
        ((TableColumn<LignePrestation, String>) colPrix).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", data.getValue().getPrix()) + "€"));
        
        TableColumn<LignePrestation, ?> colSousTotal = tablePrestations.getColumns().get(4);
        ((TableColumn<LignePrestation, String>) colSousTotal).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", data.getValue().getSousTotal()) + "€"));
    }
    
    private void chargerPieces() {
        List<Piece> pieces = atelierService.getToutesLesPieces();
        pieceCombo.setItems(FXCollections.observableArrayList(pieces));
    }
    
    @FXML
    private void chargerOR() {
        ordreReparation = orCombo.getSelectionModel().getSelectedItem();
        
        if (ordreReparation == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un ordre de réparation!");
            alert.showAndWait();
            return;
        }
        
        // Recharger l'OR depuis la base pour avoir les données à jour
        OrdreReparationDAO dao = new OrdreReparationDAO();
        ordreReparation = dao.findById(ordreReparation.getId());
        
        afficherDetails();
        chargerPrestations();
    }
    
    private void afficherDetails() {
        labelId.setText(String.valueOf(ordreReparation.getId()));
        labelDate.setText(ordreReparation.getDate().toString());
        labelVehicule.setText(ordreReparation.getVehicule().toString());
        labelClient.setText(ordreReparation.getVehicule().getClient().toString());
        labelEtat.setText(ordreReparation.getEtat().toString());
        labelTotal.setText(String.format("%.2f", ordreReparation.getTotal()) + "€");
    }
    
    private void chargerPrestations() {
        prestationsList.clear();
        prestationsList.addAll(ordreReparation.getLignesPrestation());
        tablePrestations.setItems(prestationsList);
    }
    
    @FXML
    private void ajouterPrestation() {
        if (ordreReparation == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez d'abord charger un ordre de réparation!");
            alert.showAndWait();
            return;
        }
        
        try {
            String description = descriptionField.getText();
            if (description.isEmpty()) {
                throw new IllegalArgumentException("La description est requise");
            }
            
            LignePrestation.TypePrestation type = 
                LignePrestation.TypePrestation.valueOf(typeCombo.getValue());
            Integer quantite = Integer.parseInt(quantiteField.getText());
            Double prix = Double.parseDouble(prixField.getText());
            
            Long pieceId = null;
            if (type == LignePrestation.TypePrestation.PIECE) {
                Piece piece = pieceCombo.getValue();
                if (piece == null) {
                    throw new IllegalArgumentException("Veuillez sélectionner une pièce");
                }
                pieceId = piece.getId();
            }
            
            atelierService.ajouterLignePrestation(
                ordreReparation.getId(), description, type, quantite, prix, pieceId);
            
            // Recharger l'OR pour avoir les données à jour
            OrdreReparationDAO dao = new OrdreReparationDAO();
            ordreReparation = dao.findById(ordreReparation.getId());
            
            afficherDetails();
            chargerPrestations();
            
            // Réinitialiser le formulaire
            descriptionField.clear();
            quantiteField.clear();
            prixField.clear();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Prestation ajoutée avec succès!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void supprimerPrestation() {
        LignePrestation ligne = tablePrestations.getSelectionModel().getSelectedItem();
        if (ligne == null) {
            return;
        }
        
        try {
            atelierService.supprimerLignePrestation(ligne.getId());
            
            // Recharger l'OR
            OrdreReparationDAO dao = new OrdreReparationDAO();
            ordreReparation = dao.findById(ordreReparation.getId());
            
            afficherDetails();
            chargerPrestations();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Prestation supprimée avec succès!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

