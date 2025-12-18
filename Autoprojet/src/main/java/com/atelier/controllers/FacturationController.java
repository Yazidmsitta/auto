package com.atelier.controllers;

import java.util.List;

import com.atelier.entities.Facture;
import com.atelier.services.AtelierService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.atelier.entities.OrdreReparation;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class FacturationController {
    @FXML
    private TableView<Facture> tableFactures;
    
    @FXML
    private VBox formGeneration;
    
    @FXML
    private ComboBox<OrdreReparation> orCombo;
    
    private AtelierService atelierService;
    private final ObservableList<Facture> facturesList = FXCollections.observableArrayList();
    
    public void setAtelierService(AtelierService service) {
        this.atelierService = service;
        initialiserTable();
        initialiserComboBox();
        chargerOrdresTerminesSansFacture();
        actualiser();
    }
    
    private void initialiserComboBox() {
        // Configurer le ComboBox pour afficher les OR terminés
        orCombo.setCellFactory(param -> new ListCell<OrdreReparation>() {
            @Override
            protected void updateItem(OrdreReparation or, boolean empty) {
                super.updateItem(or, empty);
                if (empty || or == null) {
                    setText(null);
                } else {
                    try {
                        String immatriculation = or.getVehicule() != null ? or.getVehicule().getImmatriculation() : "N/A";
                        double total = 0.0;
                        try {
                            total = or.getTotal();
                        } catch (Exception e) {
                            // Ignorer l'erreur, utiliser 0.0
                        }
                        setText("OR #" + or.getId() + " - " + immatriculation + " (" + String.format("%.2f", total) + "€)");
                    } catch (Exception e) {
                        setText("OR #" + or.getId());
                    }
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
                    try {
                        String immatriculation = or.getVehicule() != null ? or.getVehicule().getImmatriculation() : "N/A";
                        setText("OR #" + or.getId() + " - " + immatriculation);
                    } catch (Exception e) {
                        setText("OR #" + or.getId());
                    }
                }
            }
        });
        
        // S'assurer que le ComboBox est activé et configuré correctement
        orCombo.setDisable(false);
        orCombo.setEditable(false);
        orCombo.setVisible(true);
        
        // Ajouter un listener pour le débogage
        orCombo.setOnMouseClicked(e -> {
            System.out.println("ComboBox cliqué. Nombre d'items: " + (orCombo.getItems() != null ? orCombo.getItems().size() : 0));
        });
    }
    
    @SuppressWarnings("unchecked")
    private void initialiserTable() {
        TableColumn<Facture, ?> colId = tableFactures.getColumns().get(0);
        ((TableColumn<Facture, Long>) colId).setCellValueFactory(
            data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        
        TableColumn<Facture, ?> colDate = tableFactures.getColumns().get(1);
        ((TableColumn<Facture, String>) colDate).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate().toString()));
        
        TableColumn<Facture, ?> colOrId = tableFactures.getColumns().get(2);
        ((TableColumn<Facture, Long>) colOrId).setCellValueFactory(
            data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getOrdreReparation().getId()));
        
        TableColumn<Facture, ?> colVehicule = tableFactures.getColumns().get(3);
        ((TableColumn<Facture, String>) colVehicule).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getOrdreReparation().getVehicule().toString()));
        
        TableColumn<Facture, ?> colTotal = tableFactures.getColumns().get(4);
        ((TableColumn<Facture, String>) colTotal).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", data.getValue().getTotal()) + "€"));
    }
    
    @FXML
    private void actualiser() {
        List<Facture> factures = atelierService.getToutesLesFactures();
        facturesList.clear();
        facturesList.addAll(factures);
        tableFactures.setItems(facturesList);
        
        // Mettre à jour le ComboBox avec les OR terminés sans facture
        chargerOrdresTerminesSansFacture();
    }
    
    private void chargerOrdresTerminesSansFacture() {
        try {
            System.out.println("Chargement des OR terminés...");
            List<OrdreReparation> ordresTermines = atelierService.getOrdresReparationParEtat(
                OrdreReparation.EtatReparation.TERMINE);
            
            System.out.println("Nombre total d'OR terminés trouvés: " + ordresTermines.size());
            
            // Filtrer pour ne garder que ceux sans facture
            List<OrdreReparation> ordresSansFacture = new java.util.ArrayList<>();
            for (OrdreReparation or : ordresTermines) {
                try {
                    if (or.getFacture() == null) {
                        ordresSansFacture.add(or);
                        System.out.println("OR #" + or.getId() + " ajouté (sans facture)");
                    } else {
                        System.out.println("OR #" + or.getId() + " ignoré (a déjà une facture)");
                    }
                } catch (Exception e) {
                    // Si erreur lors de l'accès à la facture, considérer qu'il n'y a pas de facture
                    System.err.println("Erreur lors de la vérification de la facture pour OR #" + or.getId() + ": " + e.getMessage());
                    ordresSansFacture.add(or);
                }
            }
            
            ObservableList<OrdreReparation> items = FXCollections.observableArrayList(ordresSansFacture);
            orCombo.setItems(items);
            
            // Debug: afficher le nombre d'OR chargés
            System.out.println("Nombre d'OR terminés sans facture chargés dans le ComboBox: " + items.size());
            
            // S'assurer que le ComboBox est activé et visible
            orCombo.setDisable(false);
            orCombo.setVisible(true);
            
            // Si aucun OR disponible, afficher un message
            if (items.isEmpty()) {
                System.out.println("ATTENTION: Aucun OR terminé sans facture disponible!");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des OR terminés: " + e.getMessage());
            e.printStackTrace();
            orCombo.setItems(FXCollections.observableArrayList());
        }
    }
    
    @FXML
    private void genererFacture() {
        // Recharger les OR terminés sans facture avant d'afficher le formulaire
        chargerOrdresTerminesSansFacture();
        
        // Vérifier s'il y a des OR disponibles
        if (orCombo.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun OR disponible");
            alert.setHeaderText(null);
            alert.setContentText("Aucun ordre de réparation terminé sans facture n'est disponible.\n\nVeuillez d'abord terminer un ordre de réparation.");
            alert.showAndWait();
            return;
        }
        
        formGeneration.setVisible(true);
        orCombo.getSelectionModel().clearSelection();
        
        // S'assurer que le ComboBox est activé et visible
        orCombo.setDisable(false);
        orCombo.setVisible(true);
        
        // Forcer le ComboBox à se mettre à jour
        javafx.application.Platform.runLater(() -> {
            orCombo.requestFocus();
        });
    }
    
    @FXML
    private void genererFacturePourOR() {
        try {
            OrdreReparation or = orCombo.getSelectionModel().getSelectedItem();
            if (or == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un ordre de réparation terminé!");
                alert.showAndWait();
                return;
            }
            
            atelierService.genererFacture(or.getId());
            
            formGeneration.setVisible(false);
            actualiser();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Facture générée avec succès!");
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
    private void annulerGeneration() {
        formGeneration.setVisible(false);
    }
}

