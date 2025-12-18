package com.atelier.controllers;

import com.atelier.entities.OrdreReparation;
import com.atelier.entities.Vehicule;
import com.atelier.services.AtelierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class OrdresReparationController {
    @FXML
    private TableView<OrdreReparation> tableOrdres;
    
    @FXML
    private ComboBox<String> filtreEtat;
    
    @FXML
    private Button btnVoirDetails;
    
    @FXML
    private Button btnChangerEtat;
    
    private AtelierService atelierService;
    private DetailORController detailORController;
    private ObservableList<OrdreReparation> ordresList = FXCollections.observableArrayList();
    
    public void setDetailORController(DetailORController controller) {
        this.detailORController = controller;
    }
    
    public void setAtelierService(AtelierService service) {
        this.atelierService = service;
        initialiserComboBox();
        filtreEtat.setValue("TOUS");
        initialiserTable();
        actualiser();
    }
    
    private void initialiserComboBox() {
        filtreEtat.setItems(FXCollections.observableArrayList("TOUS", "DIAGNOSTIC", "ATELIER", "TERMINE"));
    }
    
    @SuppressWarnings("unchecked")
    private void initialiserTable() {
        TableColumn<OrdreReparation, ?> colId = tableOrdres.getColumns().get(0);
        ((TableColumn<OrdreReparation, Long>) colId).setCellValueFactory(
            data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        
        TableColumn<OrdreReparation, ?> colDate = tableOrdres.getColumns().get(1);
        ((TableColumn<OrdreReparation, String>) colDate).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate().toString()));
        
        TableColumn<OrdreReparation, ?> colVehicule = tableOrdres.getColumns().get(2);
        ((TableColumn<OrdreReparation, String>) colVehicule).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getVehicule().toString()));
        
        TableColumn<OrdreReparation, ?> colClient = tableOrdres.getColumns().get(3);
        ((TableColumn<OrdreReparation, String>) colClient).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getVehicule().getClient().toString()));
        
        TableColumn<OrdreReparation, ?> colEtat = tableOrdres.getColumns().get(4);
        ((TableColumn<OrdreReparation, String>) colEtat).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEtat().toString()));
        
        TableColumn<OrdreReparation, ?> colTotal = tableOrdres.getColumns().get(5);
        ((TableColumn<OrdreReparation, String>) colTotal).setCellValueFactory(
            data -> {
                try {
                    double total = data.getValue().getTotal();
                    return new javafx.beans.property.SimpleStringProperty(String.format("%.2f", total) + "€");
                } catch (Exception e) {
                    return new javafx.beans.property.SimpleStringProperty("0.00€");
                }
            });
    }
    
    @FXML
    private void actualiser() {
        List<OrdreReparation> ordres = new java.util.ArrayList<>();
        String etat = filtreEtat.getValue();
        
        if (etat == null || "TOUS".equals(etat)) {
            // Inclure tous les OR (en cours + terminés)
            ordres.addAll(atelierService.getOrdresReparationEnCours());
            ordres.addAll(atelierService.getOrdresReparationParEtat(OrdreReparation.EtatReparation.TERMINE));
        } else {
            ordres = atelierService.getOrdresReparationParEtat(
                OrdreReparation.EtatReparation.valueOf(etat));
        }
        
        ordresList.clear();
        ordresList.addAll(ordres);
        tableOrdres.setItems(ordresList);
    }
    
    @FXML
    private void filtrerParEtat() {
        actualiser();
    }
    
    @FXML
    private void nouvelOR() {
        List<Vehicule> vehicules = atelierService.getTousLesVehicules();
        if (vehicules.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Aucun véhicule disponible. Veuillez d'abord créer un véhicule.");
            alert.showAndWait();
            return;
        }
        
        // Créer une boîte de dialogue personnalisée
        Dialog<Vehicule> dialog = new Dialog<>();
        dialog.setTitle("Nouvel Ordre de Réparation");
        dialog.setHeaderText("Sélectionner un véhicule");
        
        ComboBox<Vehicule> comboBox = new ComboBox<>(FXCollections.observableArrayList(vehicules));
        comboBox.setCellFactory(param -> new ListCell<Vehicule>() {
            @Override
            protected void updateItem(Vehicule vehicule, boolean empty) {
                super.updateItem(vehicule, empty);
                if (empty || vehicule == null) {
                    setText(null);
                } else {
                    setText(vehicule.getImmatriculation() + " - " + vehicule.getMarque() + " " + vehicule.getModele() + " (" + vehicule.getClient().getNom() + ")");
                }
            }
        });
        comboBox.setButtonCell(new ListCell<Vehicule>() {
            @Override
            protected void updateItem(Vehicule vehicule, boolean empty) {
                super.updateItem(vehicule, empty);
                if (empty || vehicule == null) {
                    setText(null);
                } else {
                    setText(vehicule.getImmatriculation() + " - " + vehicule.getMarque() + " " + vehicule.getModele());
                }
            }
        });
        comboBox.setPrefWidth(400);
        comboBox.getSelectionModel().selectFirst();
        
        dialog.getDialogPane().setContent(comboBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return comboBox.getSelectionModel().getSelectedItem();
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(vehicule -> {
            try {
                OrdreReparation nouvelOR = atelierService.creerOrdreReparation(vehicule.getId());
                
                // Forcer le filtre à "TOUS" pour voir tous les OR
                filtreEtat.setValue("TOUS");
                
                // Actualiser la liste
                actualiser();
                
                // Actualiser le ComboBox dans DetailORController
                if (detailORController != null) {
                    detailORController.actualiserListeOR();
                }
                
                // Sélectionner le nouvel OR dans la table
                ordresList.stream()
                    .filter(or -> or.getId().equals(nouvelOR.getId()))
                    .findFirst()
                    .ifPresent(or -> {
                        tableOrdres.getSelectionModel().select(or);
                        tableOrdres.scrollTo(or);
                    });
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Ordre de réparation créé avec succès!\nOR #" + nouvelOR.getId() + " créé.");
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
    private void voirDetails() {
        OrdreReparation or = tableOrdres.getSelectionModel().getSelectedItem();
        if (or != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Détails OR");
            alert.setHeaderText("Ordre de Réparation #" + or.getId());
            alert.setContentText(
                "Date: " + or.getDate() + "\n" +
                "Véhicule: " + or.getVehicule() + "\n" +
                "Client: " + or.getVehicule().getClient() + "\n" +
                "État: " + or.getEtat() + "\n" +
                "Total: " + String.format("%.2f", or.getTotal()) + "€"
            );
            alert.showAndWait();
        }
    }
    
    @FXML
    private void changerEtat() {
        OrdreReparation or = tableOrdres.getSelectionModel().getSelectedItem();
        if (or == null) {
            return;
        }
        
        ChoiceDialog<OrdreReparation.EtatReparation> dialog = new ChoiceDialog<>(
            or.getEtat(),
            OrdreReparation.EtatReparation.values()
        );
        dialog.setTitle("Changer l'état");
        dialog.setHeaderText(null);
        dialog.setContentText("Nouvel état:");
        
        dialog.showAndWait().ifPresent(nouvelEtat -> {
            try {
                atelierService.changerEtatOR(or.getId(), nouvelEtat);
                actualiser();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("État modifié avec succès!");
                if (nouvelEtat == OrdreReparation.EtatReparation.TERMINE) {
                    alert.setContentText("État modifié et facture générée automatiquement!");
                }
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

