package com.example.gestiontp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;




public class CaracteristiquesSalleController {

    @FXML
    private Button matlabbutton;
    @FXML
    private Button javaJdkbutton;
    @FXML
    private Button devcbutton;
    @FXML
    private Button logiciel4button;
    @FXML
    private Button logiciel5button;
    @FXML
    private Button logiciel6button;
    @FXML
    private Button logiciel7button;
    @FXML
    private Button logiciel8button;
    @FXML
    private Button logiciel9button; // Added button
    @FXML
    private Button logiciel10button;// Added button
    @FXML private Label TitleA;
    @FXML private Label total;
    @FXML private Label posts;
    @FXML private Label groupe;
    @FXML private Label professeur;
    @FXML private Label module;
    @FXML private Label tables;



    private final Set<Button> logicielButtons = new HashSet<>();
    private final Color defaultColor = Color.TRANSPARENT; // Or any default color you have
    private final Color clickedColor = Color.LIGHTBLUE; // Or any color you want on click
    private  final Set<Button> selectedLogicielButtons = new HashSet<>();

    @FXML
    public void initialize() {
        // Add all the logiciel buttons to the set
        logicielButtons.add(matlabbutton);
        logicielButtons.add(javaJdkbutton);
        logicielButtons.add(devcbutton);
        logicielButtons.add(logiciel4button);
        logicielButtons.add(logiciel5button);
        logicielButtons.add(logiciel6button);
        logicielButtons.add(logiciel7button);
        logicielButtons.add(logiciel8button);
        logicielButtons.add(logiciel9button);
        logicielButtons.add(logiciel10button);


        // Initialize the background color of the buttons
        for (Button button : logicielButtons) {
            button.setStyle(String.format("-fx-background-color: #C3CED3;",
                    (int) (defaultColor.getRed() * 255),
                    (int) (defaultColor.getGreen() * 255),
                    (int) (defaultColor.getBlue() * 255)));
        }

    }

    public void loadSalleData(String salleName) {
        // Set the room title
        TitleA.setText(salleName);


        try {
            Connection connection = Database.connectDB();

            // Load room characteristics
            String charSql = "SELECT Capacité, Nombre_poste, Nombre_tables FROM Salle_Tp WHERE Nom_Salle = ?";
            PreparedStatement charStmt = connection.prepareStatement(charSql);
            charStmt.setString(1, salleName);
            ResultSet charRs = charStmt.executeQuery();

            if (charRs.next()) {
                total.setText(String.valueOf(charRs.getInt("Capacité")));
                posts.setText(String.valueOf(charRs.getInt("Nombre_Poste")));
                tables.setText(String.valueOf(charRs.getInt("Nombre_tables")));
            }


            String occSql = "SELECT Module, Nom_Enseignant, Groupe " +
                    "FROM emploi_du_temps " +
                    "WHERE Nom_Salle = ? AND jour = ? AND ? BETWEEN Heure_Debut AND Heure_fin";

            PreparedStatement occStmt = connection.prepareStatement(occSql);
            occStmt.setString(1, salleName);
            occStmt.setString(2, CurrentDayInFrensh.getCurrentDayInFrench());

            LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
            occStmt.setTime(3, java.sql.Time.valueOf(currentTime));

            ResultSet occRs = occStmt.executeQuery();

            if (occRs.next()) {
                module.setText(occRs.getString("Module"));
                professeur.setText(occRs.getString("Nom_Enseignant"));
                groupe.setText(occRs.getString("Groupe"));
            } else {
                module.setText("--");
                professeur.setText("--");
                groupe.setText("--");
            }
            for (Button button : logicielButtons) {
                button.setStyle("-fx-background-color: #C3CED3;");
            }


            String logiSql = "SELECT logiciel.Nom_Logiciel " +
                 "FROM installés " +
                 "JOIN logiciel ON installés.id_logiciel = logiciel.id_logiciel " +
                 "WHERE installés.Nom_Salle = ?";
PreparedStatement logiStmt = connection.prepareStatement(logiSql);
logiStmt.setString(1, salleName);
ResultSet logiRs = logiStmt.executeQuery();

            while (logiRs.next()) {
                String logicielName = logiRs.getString("Nom_Logiciel");

                for (Button button : logicielButtons) {

                    HBox parentHBox = (HBox) button.getParent();

                    String associatedLogicielName = null;
                    for (Node node : parentHBox.getChildren()) {
                        if (node instanceof TextField) {
                            TextField textField = (TextField) node;
                            associatedLogicielName = textField.getText();
                            break;
                        }
                    }
                    if (associatedLogicielName != null && associatedLogicielName.equalsIgnoreCase(logicielName)) {
                        selectedLogicielButtons.add(button);
                        button.setStyle("-fx-background-color: #012A4A;");
                    }
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


        private void insertIntoInstalles(String logicielName) {
            String insertSQL = "INSERT INTO installés (id_logiciel, Nom_Salle) " +
                    "VALUES ((SELECT id_logiciel FROM logiciel WHERE Nom_Logiciel = ? ), ?)";
            try (Connection connection = Database.connectDB();
                 PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

                pstmt.setString(1, logicielName);
                pstmt.setString(2, PageAcceuilController.NameSalle);

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
                e.printStackTrace();
            }
        }




    @FXML
    private void ChoisirlogicielButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();


        HBox parentHBox = (HBox) clickedButton.getParent();

        // Find the associated TextField
        String logicielName = null;
        for (Node node : parentHBox.getChildren()) {
            if (node instanceof TextField) {
                TextField associatedTextField = (TextField) node;
                logicielName = associatedTextField.getText();
                break; // We found it, no need to continue
            }
        }

        if (logicielName == null) {
            System.out.println("No associated TextField found!");
            return;
        }

        System.out.println("Logiciel selected: " + logicielName);

        if (selectedLogicielButtons.contains(clickedButton)) {
            // If the button is already selected, deselect it
            selectedLogicielButtons.remove(clickedButton);
            deletelogiciel(logicielName);
            clickedButton.setStyle(String.format("-fx-background-color: #C3CED3;",
                    (int) (defaultColor.getRed() * 255),
                    (int) (defaultColor.getGreen() * 255),
                    (int) (defaultColor.getBlue() * 255)));
        } else {
            // If the button is not selected, select it
            selectedLogicielButtons.add(clickedButton);
            insertIntoInstalles(logicielName);
            clickedButton.setStyle(String.format("-fx-background-color: #012A4A;",
                    (int) (clickedColor.getRed() * 255),
                    (int) (clickedColor.getGreen() * 255),
                    (int) (clickedColor.getBlue() * 255)));
        }

        System.out.println("Selected logiciels: " +
                selectedLogicielButtons.stream()
                        .map(button -> {
                            HBox hbox = (HBox) button.getParent();
                            for (Node node : hbox.getChildren()) {
                                if (node instanceof TextField) {
                                    return ((TextField) node).getText();
                                }
                            }
                            return button.getText(); // fallback
                        })
                        .toList());
    }



    @FXML
    private void closewindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openEquipement(ActionEvent actionEvent) throws IOException {
        Stage popupStage=Main.openPopupWindow("EquipementSalle.fxml","Caracteristiques",600,640);
        popupStage.setX(650);
        popupStage.setY(20);

        EquipementController equipementController = Main.getLoader().getController();
        equipementController.loadEquipements(PageAcceuilController.NameSalle);

    }



    @FXML
    private void openReservePar(ActionEvent actionEvent) throws IOException {
        Stage popupStage = Main.openPopupWindow2("ReservePar.fxml", "Caracteristiques", 400, 350);
        popupStage.setX(650);
        popupStage.setY(50);

    }

    private void deletelogiciel(String logicielName) {
        String SQL = "DELETE FROM installés " +
                "WHERE id_logiciel = (SELECT id_logiciel FROM logiciel WHERE Nom_Logiciel = ?) " +
                "AND Nom_Salle = ?";

        try (Connection connect = Database.connectDB();
             PreparedStatement deletestat = connect.prepareStatement(SQL)) {

            deletestat.setString(1, logicielName);
            deletestat.setString(2, PageAcceuilController.NameSalle);
             int delete = deletestat.executeUpdate();

            if (delete > 0) {
                System.out.println(logicielName + " deleted successfully from installés.");
            } else {
                System.out.println("No entry found to delete for " + logicielName);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}