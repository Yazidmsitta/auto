package com.atelier.controllers;

import com.atelier.entities.Client;
import com.atelier.entities.Vehicule;
import com.atelier.services.AtelierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class ClientsVehiculesController {
    @FXML
    private TableView<Client> tableClients;
    
    @FXML
    private TableView<Vehicule> tableVehicules;
    
    @FXML
    private VBox formClient;
    
    @FXML
    private TextField clientNomField;
    
    @FXML
    private TextField clientEmailField;
    
    @FXML
    private TextField clientTelephoneField;
    
    @FXML
    private VBox formVehicule;
    
    @FXML
    private ComboBox<Client> vehiculeClientCombo;
    
    @FXML
    private TextField vehiculeImmatField;
    
    @FXML
    private TextField vehiculeMarqueField;
    
    @FXML
    private TextField vehiculeModeleField;
    
    private AtelierService atelierService;
    private final ObservableList<Client> clientsList = FXCollections.observableArrayList();
    private final ObservableList<Vehicule> vehiculesList = FXCollections.observableArrayList();
    
    public void setAtelierService(AtelierService service) {
        this.atelierService = service;
        initialiserTables();
        initialiserComboBox();
        actualiser();
    }
    
    private void initialiserComboBox() {
        // Configurer le ComboBox pour afficher le nom du client
        vehiculeClientCombo.setCellFactory(param -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getNom() + " (" + client.getEmail() + ")");
                }
            }
        });
        
        vehiculeClientCombo.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getNom() + " (" + client.getEmail() + ")");
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    private void initialiserTables() {
        // Table Clients
        TableColumn<Client, ?> colId = tableClients.getColumns().get(0);
        ((TableColumn<Client, Long>) colId).setCellValueFactory(
            data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        
        TableColumn<Client, ?> colNom = tableClients.getColumns().get(1);
        ((TableColumn<Client, String>) colNom).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        
        TableColumn<Client, ?> colEmail = tableClients.getColumns().get(2);
        ((TableColumn<Client, String>) colEmail).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        
        TableColumn<Client, ?> colTel = tableClients.getColumns().get(3);
        ((TableColumn<Client, String>) colTel).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelephone()));
        
        // Table Véhicules
        TableColumn<Vehicule, ?> vColId = tableVehicules.getColumns().get(0);
        ((TableColumn<Vehicule, Long>) vColId).setCellValueFactory(
            data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        
        TableColumn<Vehicule, ?> vColImmat = tableVehicules.getColumns().get(1);
        ((TableColumn<Vehicule, String>) vColImmat).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getImmatriculation()));
        
        TableColumn<Vehicule, ?> vColMarque = tableVehicules.getColumns().get(2);
        ((TableColumn<Vehicule, String>) vColMarque).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMarque()));
        
        TableColumn<Vehicule, ?> vColModele = tableVehicules.getColumns().get(3);
        ((TableColumn<Vehicule, String>) vColModele).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getModele()));
        
        TableColumn<Vehicule, ?> vColClient = tableVehicules.getColumns().get(4);
        ((TableColumn<Vehicule, String>) vColClient).setCellValueFactory(
            data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getClient().getNom()));
    }
    
    @FXML
    private void actualiser() {
        List<Client> clients = atelierService.getTousLesClients();
        clientsList.clear();
        clientsList.addAll(clients);
        tableClients.setItems(clientsList);
        
        // Mettre à jour le ComboBox des clients
        vehiculeClientCombo.setItems(clientsList);
        
        List<Vehicule> vehicules = atelierService.getTousLesVehicules();
        vehiculesList.clear();
        vehiculesList.addAll(vehicules);
        tableVehicules.setItems(vehiculesList);
    }
    
    @FXML
    private void nouveauClient() {
        formClient.setVisible(true);
        formVehicule.setVisible(false);
        clientNomField.clear();
        clientEmailField.clear();
        clientTelephoneField.clear();
    }
    
    @FXML
    private void enregistrerClient() {
        try {
            String nom = clientNomField.getText();
            String email = clientEmailField.getText();
            String telephone = clientTelephoneField.getText();
            
            if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs sont requis");
            }
            
            atelierService.creerClient(nom, email, telephone);
            
            formClient.setVisible(false);
            actualiser();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Client créé avec succès!");
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
    private void annulerClient() {
        formClient.setVisible(false);
    }
    
    @FXML
    private void nouveauVehicule() {
        formVehicule.setVisible(true);
        formClient.setVisible(false);
        vehiculeClientCombo.getSelectionModel().clearSelection();
        vehiculeImmatField.clear();
        vehiculeMarqueField.clear();
        vehiculeModeleField.clear();
    }
    
    @FXML
    private void enregistrerVehicule() {
        try {
            Client client = vehiculeClientCombo.getSelectionModel().getSelectedItem();
            if (client == null) {
                throw new IllegalArgumentException("Veuillez sélectionner un client");
            }
            
            String immatriculation = vehiculeImmatField.getText();
            String marque = vehiculeMarqueField.getText();
            String modele = vehiculeModeleField.getText();
            
            if (immatriculation.isEmpty() || marque.isEmpty() || modele.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs sont requis");
            }
            
            atelierService.creerVehicule(client.getId(), immatriculation, marque, modele);
            
            formVehicule.setVisible(false);
            actualiser();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Véhicule créé avec succès!");
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
    private void annulerVehicule() {
        formVehicule.setVisible(false);
    }
}

