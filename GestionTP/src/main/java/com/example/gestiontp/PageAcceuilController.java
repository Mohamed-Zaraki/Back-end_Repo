package com.example.gestiontp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

 
public class PageAcceuilController {

    @FXML private AnchorPane Dashboard;
    @FXML private Hyperlink A21Link;
    @FXML private Hyperlink A22Link;
    @FXML private Hyperlink A23Link;
    @FXML private Hyperlink A24Link;
    @FXML private Hyperlink A25Link;
    @FXML private Hyperlink A31Link;
    @FXML private Hyperlink A32Link;
    @FXML private Hyperlink A33Link;
    @FXML private Hyperlink A34Link;
    @FXML private Hyperlink UnixLink;
    @FXML private Hyperlink A41Link;
    @FXML private Hyperlink A42Link;
    @FXML private Hyperlink A43Link;

    @FXML private StackPane A21Box;
    @FXML private StackPane A22Box;
    @FXML private StackPane A23Box;
    @FXML private StackPane A24Box;
    @FXML private StackPane A25Box;
    @FXML private StackPane A31Box;
    @FXML private StackPane A32Box;
    @FXML private StackPane A33Box;
    @FXML private StackPane A34Box;
    @FXML private StackPane A41Box;
    @FXML private StackPane A42Box;
    @FXML private StackPane A43Box;
    @FXML private StackPane UnixBox;

    @FXML private Text exclamation21;
    @FXML private Text exclamation22;
    @FXML private Text exclamation23;
    @FXML private Text exclamation24;
    @FXML private Text exclamation25;
    @FXML private Text exclamation31;
    @FXML private Text exclamation32;
    @FXML private Text exclamation33;
    @FXML private Text exclamation34;
    @FXML private Text exclamationUnix;
    @FXML private Text exclamation41;
    @FXML private Text exclamation42;
    @FXML private Text exclamation43;

    @FXML private Hyperlink emploidutempsLink;
    @FXML private Hyperlink stockLink;
    @FXML private Hyperlink loggingLink;
    @FXML private Hyperlink deconnecterLink;
    @FXML private ToggleButton ModeToggle;
    @FXML
    private Label module;
    @FXML
    private Label professeur;
    @FXML
    private Label groupe;
static  String NameSalle ;



    @FXML
    public void initialize() {
        CodeCouleur(null);

    }

    @FXML
    private void CodeCouleur(ActionEvent event) {
        Map<String, StackPane> boxMap = new HashMap<>();
        boxMap.putAll(Map.of(
                "A21", A21Box, "A22", A22Box, "A23", A23Box, "A24", A24Box, "A25", A25Box,
                "A31", A31Box, "A32", A32Box, "A33", A33Box
        ));
        boxMap.putAll(Map.of(
                "A34", A34Box, "Unix", UnixBox, "A41", A41Box, "A42", A42Box, "A43", A43Box
        ));

        Map<String, Hyperlink> linkMap = new HashMap<>();
        linkMap.putAll(Map.of(
                "A21", A21Link, "A22", A22Link, "A23", A23Link, "A24", A24Link, "A25", A25Link,
                "A31", A31Link, "A32", A32Link, "A33", A33Link
        ));
        linkMap.putAll(Map.of(
                "A34", A34Link, "Unix", UnixLink, "A41", A41Link, "A42", A42Link, "A43", A43Link
        ));

        for (String roomName : boxMap.keySet()) {
            if (isRoomOccupied(roomName)) {
                boxMap.get(roomName).setStyle("-fx-background-color: #f6cacc; -fx-border-color: white");
                linkMap.get(roomName).setStyle("-fx-text-fill: #bd1f21;");
            } else {
                boxMap.get(roomName).setStyle("-fx-background-color: #bee6ce; -fx-border-color: white");
                linkMap.get(roomName).setStyle("-fx-text-fill: #399e5a;");
            }
        }
    }

    private boolean isRoomOccupied(String roomName) {
        boolean occupied = false;
        String sql = "SELECT COUNT(*) FROM emploi_du_temps WHERE Nom_Salle = ? AND jour = ? AND  CURRENT_TIME BETWEEN heure_debut AND heure_fin  AND Module IS NOT NULL";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomName);
            stmt.setString(2, CurrentDayInFrensh.getCurrentDayInFrench());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                occupied = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return occupied;
    }

    @FXML
    private void panne() {
        exclamation21.setVisible(true);
        exclamation22.setVisible(true);
        exclamation23.setVisible(true);
        exclamation24.setVisible(true);
        exclamation25.setVisible(true);
        exclamation31.setVisible(true);
        exclamation32.setVisible(true);
        exclamation33.setVisible(true);
        exclamation34.setVisible(true);
        exclamation41.setVisible(true);
        exclamation42.setVisible(true);
        exclamation43.setVisible(true);
        exclamationUnix.setVisible(true);
    }

    @FXML
    private void ouvrirSalle(ActionEvent event) throws IOException {
        Hyperlink clickedLink = (Hyperlink) event.getSource();
         NameSalle = clickedLink.getText().replace("SALLE\n", "").trim();

        // Open the popup and get the stage reference
        Stage popupStage = Main.openPopupWindow("/com/example/gestiontp/CaracteristiquesSalle.fxml",
                "Details", 500, 600);
        popupStage.setX(100);
        popupStage.setY(40);

        //hna ghadi ysralk probleme rouh llmain tsib fccreatepup loader rodah static wdir getter bach tkhdm bih hneya
        CaracteristiquesSalleController controller = Main.getLoader().getController();
        if (controller != null) {
            controller.loadSalleData(NameSalle);
        } else {
            System.out.println("Error: Could not get controller for CaracteristiquesSalle");
        }


    }

    @FXML
    private void AfficherAcceuil(ActionEvent event) throws IOException {
        Main.switchScene("Acceuil.fxml");
    }

    @FXML
    private void AfficherEmploiDuTemps(ActionEvent event) throws IOException {
        Main.switchScene("EmploiDuTemps.fxml");
    }

    @FXML
    private void AfficherStock(ActionEvent event) throws IOException {
        Main.switchScene("Stock.fxml");
    }

    @FXML
    private void AfficherLogging(ActionEvent event) throws IOException {
        Main.switchScene("Logging.fxml");
    }

    @FXML
    private void Deconnecter(ActionEvent event) throws IOException {
        Main.switchScene("Login.fxml");
    }

    @FXML
    private void changertheme() {
        Main.setDarkMode(ModeToggle.isSelected());
    }

    @FXML
    public void openfiltrage(ActionEvent event) throws IOException {
        Stage popupWindow = Main.openPopupWindow2("FiltrageSalle.fxml", "filtrer", 550, 350);
        popupWindow.setX(40);
        popupWindow.setY(160);
    }



}