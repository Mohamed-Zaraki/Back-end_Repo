package com.example.gestiontp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FiltrageSalleController implements Initializable {

    @FXML private ChoiceBox<String> heurestartchoice;
    @FXML private ChoiceBox<String> heureendchoice;
    @FXML private ChoiceBox<String> datejourchoice;
    @FXML private ChoiceBox<String> datemoischoice;

    @FXML private TableView<FiltrageCaracteristiques> filtreTable;
    @FXML private TableColumn<FiltrageCaracteristiques, String> ramColumn2;
    @FXML private TableColumn<FiltrageCaracteristiques, String> logicielsColumn2;
    @FXML private TableColumn<FiltrageCaracteristiques, String> capaciteColumn2;

    @FXML private Button samedibutton;
    @FXML private Button dimanchebutton;
    @FXML private Button lundibutton;
    @FXML private Button mardibutton;
    @FXML private Button mercredibutton;
    @FXML private Button jeudibutton;

    private Button selectedDayButton = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate time choice boxes
        ObservableList<String> heures = FXCollections.observableArrayList("08:00", "09:30", "11:00", "12:30", "14:00", "15:30", "17:00");
        heurestartchoice.setItems(heures);
        heureendchoice.setItems(heures);
        heurestartchoice.setValue("08:00");
        heureendchoice.setValue(" 09:30");

        // Populate day choice box
        ObservableList<String> jours = FXCollections.observableArrayList();
        for (int i = 1; i <= 31; i++) {
            jours.add(String.valueOf(i));
        }
        datejourchoice.setItems(jours);
        datejourchoice.setValue("  27");

        // Populate month choice box
        ObservableList<String> mois = FXCollections.observableArrayList("01", "02", "03", "04", "05", "09", "10", "11", "12");
        datemoischoice.setItems(mois);
        datemoischoice.setValue("   05");

        // Configure the table view
        ramColumn2.setCellValueFactory(new PropertyValueFactory<>("ram"));
        logicielsColumn2.setCellValueFactory(new PropertyValueFactory<>("logiciels"));
        capaciteColumn2.setCellValueFactory(new PropertyValueFactory<>("capaciteSalle"));

        filtreTable.setEditable(true);
        ramColumn2.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        logicielsColumn2.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        capaciteColumn2.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        ramColumn2.setOnEditCommit(event -> event.getRowValue().setRam(event.getNewValue()));
        logicielsColumn2.setOnEditCommit(event -> event.getRowValue().setLogiciels(event.getNewValue()));
        capaciteColumn2.setOnEditCommit(event -> event.getRowValue().setCapaciteSalle(event.getNewValue()));

        ObservableList<FiltrageCaracteristiques> data = FXCollections.observableArrayList(new FiltrageCaracteristiques("", "", "", "", ""));
        filtreTable.setItems(data);
        filtreTable.setFixedCellSize(40);
        filtreTable.setPrefHeight(40 * data.size() + 30);
        filtreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        filtreTable.setPlaceholder(new Label(""));
    }

    @FXML
    private void ChoisirJourButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (selectedDayButton != null && selectedDayButton != clickedButton) {
            selectedDayButton.setStyle("-fx-background-color: #C3CED3;");
        }

        if (clickedButton == selectedDayButton) {
            clickedButton.setStyle("-fx-background-color: #C3DED3;");
            selectedDayButton = null;
        } else {
            clickedButton.setStyle("-fx-background-color: #012A4A;");
            selectedDayButton = clickedButton;
        }
    }

    @FXML
    public void ConfirmerFiltrage(ActionEvent event) throws IOException {

        /*Back-end */

        Stage popupWindow = Main.openPopupWindow2("Reservation.fxml", "Reservation", 450, 300);
        popupWindow.setX(40);
        popupWindow.setY(160);
    }


}