package com.example.gestiontp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.util.List;

public class ReservationController {

    @FXML
    private FlowPane sallesPane;

    @FXML
    private TextField nomEnseignant;

    private List<String> sallesDisponibles;
    private StackPane salleSelectionnee = null;

    public void initialize() {
        setSallesDisponibles(List.of("A21", "A32", "B12")); // test data
    }

    public void setSallesDisponibles(List<String> salles) {
        this.sallesDisponibles = salles;
        afficherSalles();
    }

    private void afficherSalles() {
        sallesPane.getChildren().clear();

        if (sallesDisponibles == null || sallesDisponibles.isEmpty()) {
            Label label = new Label("Aucune salle disponible pour le moment.");
            label.setStyle("-fx-text-fill: red; -fx-font-style: italic;");
            sallesPane.getChildren().add(label);
            return;
        }

        for (String salle : sallesDisponibles) {
            StackPane stack = new StackPane();
            stack.setPrefSize(70, 40);
            stack.getStyleClass().add("stack-salle");

            Label nomSalle = new Label(salle);
            nomSalle.getStyleClass().add("label-salle");
            stack.getChildren().add(nomSalle);

            stack.setOnMouseClicked(event -> {
                // Deselect the previously selected salle if any
                if (salleSelectionnee != null) {
                    salleSelectionnee.getStyleClass().removeAll("stack-salle-selected");
                    salleSelectionnee.getStyleClass().add("stack-salle");
                    salleSelectionnee.setUserData(false);
                }

                // Select the clicked salle
                stack.getStyleClass().removeAll("stack-salle");
                stack.getStyleClass().add("stack-salle-selected");
                stack.setUserData(true);
                salleSelectionnee = stack;
            });

            stack.setUserData(false); // initially not selected
            sallesPane.getChildren().add(stack);
        }
    }

    @FXML
    private void handleReservation() {
        if (salleSelectionnee != null && (Boolean) salleSelectionnee.getUserData()) {
            String nomSalle = ((Label) salleSelectionnee.getChildren().get(0)).getText();
            String enseignant = nomEnseignant.getText();

            if (enseignant != null && !enseignant.trim().isEmpty()) {
                showAlert("Réservation", "Salle " + nomSalle + " réservée par " + enseignant + ".");
                // Here you would typically add the logic to save the reservation
            } else {
                showAlert("Erreur", "Veuillez entrer le nom de l'enseignant.");
            }
        } else {
            showAlert("Avertissement", "Veuillez sélectionner une salle pour la réservation.");
        }
    }

    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(contenu);
        alert.showAndWait();
    }



}