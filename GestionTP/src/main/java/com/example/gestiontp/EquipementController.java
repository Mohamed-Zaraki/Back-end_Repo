package com.example.gestiontp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EquipementController implements Initializable {

    @FXML private TableView<Equipement> EquipementTable;
    @FXML private TableColumn<Equipement, String> codeColumn;
    @FXML private TableColumn<Equipement, String> marqueColumn;
    @FXML private TableColumn<Equipement, String> ramColumn;
    @FXML private TableColumn<Equipement, String> seColumn;
    @FXML private TableColumn<Equipement, String> disqueColumn;
    @FXML private TableColumn<Equipement, String> cpuColumn;
    private String salleId;
    private  ObservableList<Equipement> equipementList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns with cell value factories
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        marqueColumn.setCellValueFactory(cellData -> cellData.getValue().marqueProperty());
        ramColumn.setCellValueFactory(cellData -> cellData.getValue().ramProperty());
        seColumn.setCellValueFactory(cellData -> cellData.getValue().seProperty());
        disqueColumn.setCellValueFactory(cellData -> cellData.getValue().disqueProperty());
        cpuColumn.setCellValueFactory(cellData -> cellData.getValue().cpuProperty());

        // Enable editing with TextFieldTableCell
        codeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        marqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ramColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        seColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        disqueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cpuColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Handle edit commit events
        codeColumn.setOnEditCommit(event -> event.getRowValue().setCode(event.getNewValue()));
        marqueColumn.setOnEditCommit(event -> event.getRowValue().setMarque(event.getNewValue()));
        ramColumn.setOnEditCommit(event -> event.getRowValue().setRam(event.getNewValue()));
        seColumn.setOnEditCommit(event -> event.getRowValue().setSe(event.getNewValue()));
        disqueColumn.setOnEditCommit(event -> event.getRowValue().setDisque(event.getNewValue()));
        cpuColumn.setOnEditCommit(event -> event.getRowValue().setCpu(event.getNewValue()));

        codeColumn.setOnEditCommit(event -> {
            if(event.getSource() == codeColumn) {

            }
            Equipement equipement = event.getRowValue();
            equipement.setCode(event.getNewValue());
            insertEquipement(equipement);
        });

        marqueColumn.setOnEditCommit(event -> {
            Equipement equipement = event.getRowValue();
            equipement.setMarque(event.getNewValue());
            updateEquipement(equipement);
        });

        ramColumn.setOnEditCommit(event -> {
            Equipement equipement = event.getRowValue();
            equipement.setRam(event.getNewValue());
            updateEquipement(equipement);
        });

        seColumn.setOnEditCommit(event -> {
            Equipement equipement = event.getRowValue();
            equipement.setSe(event.getNewValue());
            updateEquipement(equipement);
        });

        disqueColumn.setOnEditCommit(event -> {
            Equipement equipement = event.getRowValue();
            equipement.setDisque(event.getNewValue());
            updateEquipement(equipement);
        });

        cpuColumn.setOnEditCommit(event -> {
            Equipement equipement = event.getRowValue();
            equipement.setCpu(event.getNewValue());
            updateEquipement(equipement);
        });
        // Add initial empty rows
        for (int i = 0; i < 20; i++) {
            equipementList.add(new Equipement());
        }

        EquipementTable.setItems(equipementList);
        EquipementTable.setEditable(true);
    }

    public void loadEquipements(String salleId) {
        this.salleId = salleId;
        equipementList.clear();
        equipementList=loadEquipementsBySalle(salleId);
        EquipementTable.setItems(equipementList);
    }

    @FXML
    private void handleAddRow() {
        Equipement newEquipement = new Equipement();
        newEquipement.setSalleId(salleId);
        equipementList.add(newEquipement);

    }

    @FXML
    private void handleDeleteRow(ActionEvent event) {
        Equipement selected = EquipementTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            deleteEquipement(selected.codeProperty().get());
            equipementList.remove(selected);
        }
    }




        public  ObservableList<Equipement> loadEquipementsBySalle(String salleId) {
            ObservableList<Equipement> equipements = FXCollections.observableArrayList();
            try {
                Connection conn = Database.connectDB();
                String sql = "SELECT * FROM ordinateur WHERE Nom_Salle = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, salleId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Equipement equipement = new Equipement();
                    equipement.setCode(rs.getString("Code_pc"));
                    equipement.setMarque(rs.getString("Marque"));
                    equipement.setRam(rs.getString("Ram"));
                    equipement.setSe(rs.getString("Type_SE"));
                    equipement.setDisque(rs.getString("Disque_Dur"));
                    equipement.setCpu(rs.getString("Processeur"));
                    equipement.setSalleId(rs.getString("Nom_Salle"));
                    equipements.add(equipement);
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return equipements;
        }

        public  void insertEquipement(Equipement equipement) {

            if (equipement.codeProperty().get().isEmpty() ||
                    equipement.marqueProperty().get().isEmpty() ||
                    equipement.ramProperty().get().isEmpty() ||
                    equipement.seProperty().get().isEmpty() ||
                    equipement.disqueProperty().get().isEmpty() ||
                    equipement.cpuProperty().get().isEmpty() ||
                    equipement.salleIdProperty().get().isEmpty()) {

                System.out.println("⚠️ All fields are required.");
                return; // Stop the insert
            }

            try {
                Connection conn = Database.connectDB();

                    String sql = "INSERT INTO ordinateur (Code_pc, Marque, Ram, Type_SE, Disque_Dur, Processeur, Nom_Salle) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, equipement.codeProperty().get());
                    stmt.setString(2, equipement.marqueProperty().get());
                    stmt.setString(3, equipement.ramProperty().get());
                    stmt.setString(4, equipement.seProperty().get());
                    stmt.setString(5, equipement.disqueProperty().get());
                    stmt.setString(6, equipement.cpuProperty().get());
                    stmt.setString(7, equipement.salleIdProperty().get());

                    stmt.executeUpdate();


                    String SQL = "UPDATE Salle_Tp SET Nombre_Poste = Nombre_Poste + 1 WHERE Nom_Salle = ?";
                    PreparedStatement Update = conn.prepareStatement(SQL);
                    Update.setString(1, equipement.salleIdProperty().get());

                    Update.executeUpdate();

                conn.close();


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        public  void deleteEquipement(String code) {
            try {
                Connection conn = Database.connectDB();
                String sql = "DELETE FROM ordinateur WHERE Code_pc = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, code);
                stmt.executeUpdate();

                String sql1 = "UPDATE Salle_Tp SET Nombre_Poste = Nombre_Poste -1  WHERE Nom_Salle = ?";
                PreparedStatement Update = conn.prepareStatement(sql1);
                Update.setString(1, PageAcceuilController.NameSalle);
                Update.executeUpdate();

                conn.close();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    private void autoRefreshTable(String salle , Label posts) {
        loadEquipements(salle);
        posts.setText(String.valueOf(equipementList.size()));
    }

        public static void updateEquipement(Equipement equipement) {
            try {
                Connection conn = Database.connectDB();
                String sql = "UPDATE ordinateur SET Marque = ?, Ram = ?, Type_SE = ?, Disque_Dur = ?, Processeur = ? WHERE Code_pc = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, equipement.marqueProperty().get());
                stmt.setString(2, equipement.ramProperty().get());
                stmt.setString(3, equipement.seProperty().get());
                stmt.setString(4, equipement.disqueProperty().get());
                stmt.setString(5, equipement.cpuProperty().get());
                stmt.setString(6, equipement.codeProperty().get());
                stmt.executeUpdate();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    @FXML
    private void closewindow(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}


    /*+ Back end enregistrer les informations entrees et lier a la bdd*/
