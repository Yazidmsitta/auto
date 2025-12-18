package com.atelier.controllers;

import com.atelier.dao.PieceDAO;
import com.atelier.entities.Piece;
import com.atelier.services.AtelierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class StockPiecesController {
    @FXML
    private TableView<Piece> tablePieces;
    
    @FXML
    private Button btnModifierStock;
    
    @FXML
    private Button btnModifierPrix;
    
    @FXML
    private VBox formAjout;
    
    @FXML
    private TextField refField;
    
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField stockField;
    
    @FXML
    private TextField prixField;
    
    private AtelierService atelierService;
    private ObservableList<Piece> piecesList = FXCollections.observableArrayList();
    
    public void setAtelierService(AtelierService service) {
        this.atelierService = service;
        initialiserTable();
        actualiser();
    }
    
    @SuppressWarnings("unchecked")
    private void initialiserTable() {
        TableColumn<Piece, ?> colId = tablePieces.getColumns().get(0);
        ((TableColumn<Piece, Long>) colId).setCellValueFactory(
            data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        
        TableColumn<Piece, ?> colRef = tablePieces.getColumns().get(1);
        ((TableColumn<Piece, String>) colRef).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRef()));
        
        TableColumn<Piece, ?> colNom = tablePieces.getColumns().get(2);
        ((TableColumn<Piece, String>) colNom).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        
        TableColumn<Piece, ?> colStock = tablePieces.getColumns().get(3);
        ((TableColumn<Piece, Integer>) colStock).setCellValueFactory(
            data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getStock()));
        
        TableColumn<Piece, ?> colPrix = tablePieces.getColumns().get(4);
        ((TableColumn<Piece, String>) colPrix).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", data.getValue().getPrix()) + "€"));
    }
    
    @FXML
    private void actualiser() {
        List<Piece> pieces = atelierService.getToutesLesPieces();
        piecesList.clear();
        piecesList.addAll(pieces);
        tablePieces.setItems(piecesList);
    }
    
    @FXML
    private void nouvellePiece() {
        formAjout.setVisible(true);
        refField.clear();
        nomField.clear();
        stockField.clear();
        prixField.clear();
    }
    
    @FXML
    private void enregistrerPiece() {
        try {
            String ref = refField.getText();
            String nom = nomField.getText();
            Integer stock = Integer.parseInt(stockField.getText());
            Double prix = Double.parseDouble(prixField.getText());
            
            atelierService.creerPiece(ref, nom, stock, prix);
            
            formAjout.setVisible(false);
            actualiser();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Pièce créée avec succès!");
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
    private void annulerAjout() {
        formAjout.setVisible(false);
    }
    
    @FXML
    private void modifierStock() {
        Piece piece = tablePieces.getSelectionModel().getSelectedItem();
        if (piece == null) {
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog(String.valueOf(piece.getStock()));
        dialog.setTitle("Modifier le stock");
        dialog.setHeaderText(null);
        dialog.setContentText("Nouveau stock:");
        
        dialog.showAndWait().ifPresent(stockStr -> {
            try {
                Integer nouveauStock = Integer.parseInt(stockStr);
                atelierService.mettreAJourStock(piece.getId(), nouveauStock);
                actualiser();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Stock modifié avec succès!");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Erreur: " + e.getMessage());
                alert.showAndWait();
            }
        });
    }
    
    @FXML
    private void modifierPrix() {
        Piece piece = tablePieces.getSelectionModel().getSelectedItem();
        if (piece == null) {
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog(String.valueOf(piece.getPrix()));
        dialog.setTitle("Modifier le prix");
        dialog.setHeaderText(null);
        dialog.setContentText("Nouveau prix:");
        
        dialog.showAndWait().ifPresent(prixStr -> {
            try {
                Double nouveauPrix = Double.parseDouble(prixStr);
                piece.setPrix(nouveauPrix);
                PieceDAO dao = new PieceDAO();
                dao.update(piece);
                actualiser();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Prix modifié avec succès!");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Erreur: " + e.getMessage());
                alert.showAndWait();
            }
        });
    }
}

