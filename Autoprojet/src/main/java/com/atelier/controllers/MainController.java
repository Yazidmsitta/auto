package com.atelier.controllers;

import com.atelier.services.AtelierService;
import com.atelier.util.DataInitializer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private TabPane tabPane;
    
    @FXML
    private MenuItem menuQuitter;
    
    @FXML
    private MenuItem menuInitDonnees;
    
    private AtelierService atelierService;
    
    public void setAtelierService(AtelierService service) {
        this.atelierService = service;
    }
    
    @FXML
    private void quitter() {
        Stage stage = (Stage) tabPane.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void initialiserDonnees() {
        try {
            DataInitializer.initialiser(atelierService);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Initialisation");
            alert.setHeaderText(null);
            alert.setContentText("Données initialisées avec succès!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'initialisation: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

