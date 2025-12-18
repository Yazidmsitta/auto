package com.atelier;

import com.atelier.controllers.ClientsVehiculesController;
import com.atelier.controllers.DetailORController;
import com.atelier.controllers.FacturationController;
import com.atelier.controllers.MainController;
import com.atelier.controllers.OrdresReparationController;
import com.atelier.controllers.StockPiecesController;
import com.atelier.services.AtelierService;
import com.atelier.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            AtelierService atelierService = new AtelierService();
            
            // Charger le fichier FXML principal
            java.net.URL fxmlUrl = getClass().getResource("/fxml/main.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Fichier FXML introuvable: /fxml/main.fxml");
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            
            MainController mainController = loader.getController();
            if (mainController == null) {
                throw new RuntimeException("Contrôleur principal introuvable dans main.fxml");
            }
            mainController.setAtelierService(atelierService);
            
            // Initialiser les contrôleurs des onglets
            TabPane tabPane = (TabPane) root.lookup("#tabPane");
            if (tabPane != null) {
                // Onglet 0: Clients et Véhicules
                Tab tab0 = tabPane.getTabs().get(0);
                java.net.URL fxml0Url = getClass().getResource("/fxml/clients_vehicules.fxml");
                if (fxml0Url != null) {
                    FXMLLoader loader0 = new FXMLLoader(fxml0Url);
                    tab0.setContent(loader0.load());
                    ClientsVehiculesController controller0 = loader0.getController();
                    if (controller0 != null) {
                        controller0.setAtelierService(atelierService);
                    }
                }
                
                // Onglet 1: Ordres de réparation
                Tab tab1 = tabPane.getTabs().get(1);
                java.net.URL fxml1Url = getClass().getResource("/fxml/ordres_reparation.fxml");
                OrdresReparationController controller1 = null;
                if (fxml1Url != null) {
                    FXMLLoader loader1 = new FXMLLoader(fxml1Url);
                    tab1.setContent(loader1.load());
                    controller1 = loader1.getController();
                    if (controller1 != null) {
                        controller1.setAtelierService(atelierService);
                    }
                }
                
                // Onglet 2: Détail OR
                Tab tab2 = tabPane.getTabs().get(2);
                java.net.URL fxml2Url = getClass().getResource("/fxml/detail_or.fxml");
                DetailORController controller2 = null;
                if (fxml2Url != null) {
                    FXMLLoader loader2 = new FXMLLoader(fxml2Url);
                    tab2.setContent(loader2.load());
                    controller2 = loader2.getController();
                    if (controller2 != null) {
                        controller2.setAtelierService(atelierService);
                        // Passer la référence au contrôleur des OR pour qu'il puisse notifier les mises à jour
                        if (controller1 != null) {
                            controller1.setDetailORController(controller2);
                        }
                    }
                }
                
                // Onglet 3: Stock pièces
                Tab tab3 = tabPane.getTabs().get(3);
                java.net.URL fxml3Url = getClass().getResource("/fxml/stock_pieces.fxml");
                if (fxml3Url != null) {
                    FXMLLoader loader3 = new FXMLLoader(fxml3Url);
                    tab3.setContent(loader3.load());
                    StockPiecesController controller3 = loader3.getController();
                    if (controller3 != null) {
                        controller3.setAtelierService(atelierService);
                    }
                }
                
                // Onglet 4: Facturation
                Tab tab4 = tabPane.getTabs().get(4);
                java.net.URL fxml4Url = getClass().getResource("/fxml/facturation.fxml");
                if (fxml4Url != null) {
                    FXMLLoader loader4 = new FXMLLoader(fxml4Url);
                    tab4.setContent(loader4.load());
                    FacturationController controller4 = loader4.getController();
                    if (controller4 != null) {
                        controller4.setAtelierService(atelierService);
                    }
                }
            }
            
            primaryStage.setTitle("Gestion d'Atelier Automobile");
            Scene scene = new Scene(root, 1200, 800);
            
            // Charger le CSS
            try {
                java.net.URL cssUrl = getClass().getResource("/css/style.css");
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                }
            } catch (Exception e) {
                System.err.println("Impossible de charger le CSS: " + e.getMessage());
            }
            
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERREUR lors du démarrage de l'application: " + e.getMessage());
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de démarrer l'application");
            alert.setContentText("Erreur: " + e.getMessage() + "\n\nVoir la console pour plus de détails.");
            alert.showAndWait();
        }
    }
    
    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }
    
    public static void main(String[] args) {
        // Vérifier si JavaFX est disponible
        try {
            Class.forName("javafx.application.Application");
            launch(args);
        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR: JavaFX runtime components are missing!");
            System.err.println("Solution: Ajoutez ces VM options dans votre configuration de run:");
            System.err.println("  --add-modules javafx.controls,javafx.fxml");
            System.err.println("");
            System.err.println("Ou utilisez: mvn javafx:run");
            System.exit(1);
        }
    }
}

