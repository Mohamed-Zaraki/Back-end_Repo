package com.example.gestiontp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.binding.Bindings;

public class ReserveParController {

    @FXML
    private TableView<Reservation> tablereserv;

    @FXML
    private TableColumn<Reservation, String> date;

    @FXML
    private TableColumn<Reservation, String> heure;

    @FXML
    private TableColumn<Reservation, String> enseignant;

    @FXML
    private Button ajouterLignereserv;

    @FXML
    private Button supprimerLignereserv;

    private final ObservableList<Reservation> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        heure.setCellValueFactory(new PropertyValueFactory<>("heure"));
        enseignant.setCellValueFactory(new PropertyValueFactory<>("enseignant"));

        tablereserv.setEditable(true);
        date.setCellFactory(TextFieldTableCell.forTableColumn());
        heure.setCellFactory(TextFieldTableCell.forTableColumn());
        enseignant.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set selection mode to SINGLE for more reliable selection
        tablereserv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // We'll handle the button disable state manually since supprimerLignereserv might not exist
        try {
            if (supprimerLignereserv != null) {
                supprimerLignereserv.disableProperty().bind(
                        Bindings.isEmpty(tablereserv.getSelectionModel().getSelectedItems())
                );
            }
        } catch (Exception e) {
            System.err.println("Warning: supprimerLignereserv button not found in FXML");
        }

        date.setOnEditCommit(event -> event.getRowValue().setDate(event.getNewValue()));
        heure.setOnEditCommit(event -> event.getRowValue().setHeure(event.getNewValue()));
        enseignant.setOnEditCommit(event -> event.getRowValue().setEnseignant(event.getNewValue()));

        data.addAll(
                new Reservation("", "", ""),
                new Reservation("", "", "")
        );

        tablereserv.setItems(data);
        adjustTableHeight();

        // Add a click handler to ensure selection is working
        tablereserv.setOnMouseClicked(event -> {
            if (tablereserv.getSelectionModel().getSelectedItem() != null) {
                System.out.println("Selected: " + tablereserv.getSelectionModel().getSelectedItem().getDate());
            }
        });
    }

    @FXML
    public void ajouterLigne() {
        data.add(new Reservation("", "", ""));
        adjustTableHeight();

        // Select the newly added row
        tablereserv.getSelectionModel().selectLast();
        tablereserv.scrollTo(data.size() - 1);
    }

    @FXML
    public void supprimerLigne() {
        Reservation selectedItem = tablereserv.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Remove the selected item
            data.remove(selectedItem);
            adjustTableHeight();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Suppression impossible");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ligne à supprimer.");
            alert.showAndWait();
        }
    }

    private void adjustTableHeight() {
        double rowHeight = 30; // Approximate row height (adjust if needed)
        double headerHeight = 35; // Approximate header height
        double totalHeight = headerHeight + (data.size() * rowHeight);
        tablereserv.setPrefHeight(totalHeight);
    }

    public static class Reservation {
        private String date;
        private String heure;
        private String enseignant;

        public Reservation(String date, String heure, String enseignant) {
            this.date = date;
            this.heure = heure;
            this.enseignant = enseignant;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHeure() {
            return heure;
        }

        public void setHeure(String heure) {
            this.heure = heure;
        }

        public String getEnseignant() {
            return enseignant;
        }

        public void setEnseignant(String enseignant) {
            this.enseignant = enseignant;
        }
    }
}