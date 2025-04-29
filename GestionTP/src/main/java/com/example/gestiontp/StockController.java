package com.example.gestiontp;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StockController {

    @FXML private TableView<caracteristique> tablecaract;
    @FXML private TableColumn<caracteristique, String> ElementColumn;
    @FXML private TableColumn<caracteristique, String> TypeColumn;
    @FXML private TableColumn<caracteristique, String> NeufColumn;
    @FXML private TableColumn<caracteristique, String> UtiliseColumn;
    @FXML private ToggleButton ModeToggle;

    /*private final ObservableList<caracteristique> dataLogiciel = FXCollections.observableArrayList(
            new caracteristique("Ventillo", "", "", ""),
            new caracteristique("Ecran", "", "", ""),
            new caracteristique("Souris", "", "", ""),
            new caracteristique("RAM", "", "", ""),
            new caracteristique("Clavier", "", "", ""),
            new caracteristique("CPU", "", "", ""),
            new caracteristique("Disque", "", "", "")
    );  */

    private final ObservableList<caracteristique> dataLogiciel = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialisation des colonnes
        ElementColumn.setCellValueFactory(new PropertyValueFactory<>("Element"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        NeufColumn.setCellValueFactory(new PropertyValueFactory<>("Neuf"));
        UtiliseColumn.setCellValueFactory(new PropertyValueFactory<>("Utilise"));

        tablecaract.setEditable(true);
        tablecaract.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablecaract.setItems(dataLogiciel);

        // Configuration des cellules pour l'édition et le style dynamique
        applyCellFactories();

        // Autoriser l'édition directement via TextFieldTableCell
        ElementColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        TypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        NeufColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        UtiliseColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Validation automatique de chaque modification
        ElementColumn.setOnEditCommit(event -> {
            caracteristique row = event.getRowValue();
            row.setElement(event.getNewValue());
            saveToDatabase(); // Enregistrer après modification
        });

        TypeColumn.setOnEditCommit(event -> {
            caracteristique row = event.getRowValue();
            row.setType(event.getNewValue());
            saveToDatabase(); // Enregistrer après modification
        });

        NeufColumn.setOnEditCommit(event -> {
            caracteristique row = event.getRowValue();
            row.setNeuf(event.getNewValue());
            saveToDatabase(); // Enregistrer après modification
        });

        UtiliseColumn.setOnEditCommit(event -> {
            caracteristique row = event.getRowValue();
            row.setUtilise(event.getNewValue());
            saveToDatabase(); // Enregistrer après modification
        });

        // Ajuster la hauteur du tableau
        tablecaract.setFixedCellSize(32);
        tablecaract.prefHeightProperty().bind(
                Bindings.size(tablecaract.getItems()).multiply(tablecaract.getFixedCellSize()).add(28)
        );

        // Appliquer le mode en fonction de l'état initial
        ModeToggle.setSelected(Main.isDarkMode());

        // Load data m database m3a bedya
        loadDataFromDatabase();
    }


    private void loadDataFromDatabase() {
        dataLogiciel.clear();      //3lajel la duplication te3 les données 9dom
        try (Connection conn = Database.connectDB();
             PreparedStatement prepare = conn.prepareStatement("SELECT id_équipement, Nom_équipement, caractéristique, équipement_Neuf, équipement_Utiliser FROM stock");
             ResultSet rs = prepare.executeQuery()) {

            while (rs.next()) {
                dataLogiciel.add(new caracteristique(
                        rs.getInt("id_équipement"),
                        rs.getString("Nom_équipement"),
                        rs.getString("caractéristique"),
                        String.valueOf(rs.getInt("équipement_Neuf")),
                        String.valueOf(rs.getInt("équipement_Utiliser"))
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement de la base de données."); alert.showAndWait();
        }
    }

    @FXML
    private void saveToDatabase() {
        try (Connection conn = Database.connectDB()) {
            for (caracteristique c : dataLogiciel) {
                String neuf = c.getNeuf().trim().isEmpty() ? "0" : c.getNeuf().trim();
                String utilise = c.getUtilise().trim().isEmpty() ? "0" : c.getUtilise().trim();

                // verifie si neuf & utilise sont utilisé donc ils doivent etre des entiers
                if (!isInteger(neuf) || !isInteger(utilise)) {
                    new Alert(Alert.AlertType.ERROR, "Les champs 'Neuf' et 'Utilise' doivent être numériques.").showAndWait();
                    return;
                }

                if (c.getIdEquipement() == 0) {
                    try (PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO stock (Nom_équipement, caractéristique, équipement_Neuf, équipement_Utiliser, id_utilisateur) VALUES (?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS)
                    ) {

                        insert.setString(1, c.getElement());
                        insert.setString(2, c.getType());
                        insert.setInt(3, Integer.parseInt(neuf));
                        insert.setInt(4, Integer.parseInt(utilise));
                        insert.setInt(5, 1); //  id_utilisateur = 1
                        insert.executeUpdate();
                        try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int newId = generatedKeys.getInt(1);
                                c.setIdEquipement(newId);
                            }
                        }
                    }
                } else {
                    c.setNeuf(neuf);
                    c.setUtilise(utilise);
                    updateRowInDatabase(c);
                }
            }

            //new Alert(Alert.AlertType.INFORMATION, "Données enregistrées avec succès !").showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
        }
    }

    private void updateRowInDatabase(caracteristique c) {
        try (Connection conn = Database.connectDB();
             PreparedStatement update = conn.prepareStatement(
                     "UPDATE stock SET Nom_équipement = ?, caractéristique = ?, équipement_Neuf = ?, équipement_Utiliser = ? WHERE id_équipement = ?"
             )) {

            if (!c.getNeuf().isEmpty() && !isInteger(c.getNeuf())) {
                new Alert(Alert.AlertType.ERROR, "Le champ 'Neuf' doit être un entier.").showAndWait();
                return;
            }

            if (!c.getUtilise().isEmpty() && !isInteger(c.getUtilise())) {
                new Alert(Alert.AlertType.ERROR, "Le champ 'Utilisé' doit être un entier.").showAndWait();
                return;
            }

            update.setString(1, c.getElement());
            update.setString(2, c.getType());
            update.setInt(3, c.getNeuf().isEmpty() ? 0 : Integer.parseInt(c.getNeuf()));
            update.setInt(4, c.getUtilise().isEmpty() ? 0 : Integer.parseInt(c.getUtilise()));
            update.setInt(5, c.getIdEquipement());

            update.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur de mise à jour : " + e.getMessage()).showAndWait();
        }
    }



    // Méthode utilitaire pour vérifier si une chaîne peut être convertie en entier
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);  // Essaie de convertir la chaîne en entier
            return true;  // Si la conversion réussit, c'est un entier
        } catch (NumberFormatException e) {
            return false;  // Si une exception est levée, la chaîne n'est pas un entier
        }
    }

    private void applyCellFactories() {
        String baseStyle = "-fx-padding: 5px; -fx-text-alignment: center; -fx-font-family:'Poppins Regular'; -fx-font-size: 13; -fx-translate-y: 2;";
        String lightModeColor = "-fx-fill: derive(#292D32, 0%);";
        String darkModeColor = "-fx-fill: derive(#FFFFFF, 0%);";
        String textColor = Main.isDarkMode() ? darkModeColor : lightModeColor;
        String finalStyle = baseStyle + textColor;

        // Utiliser une lambda pour définir le CellFactory qui crée des TableCell avec style
        javafx.util.Callback<TableColumn<caracteristique, String>, TableCell<caracteristique, String>> cellFactory =
                param -> new TableCell<>() {
                    private final Text text = new Text();

                    {
                        text.wrappingWidthProperty().bind(param.widthProperty());
                        text.setStyle(finalStyle);
                        setGraphic(text);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        text.setText(empty || item == null ? "" : item);
                    }
                };


        // Appliquer la CellFactory pour le style (le TextFieldTableCell gère l'édition)
        // Nous n'appliquons plus notre style directement via setCellFactory ici pour permettre l'édition.
        // Le style est appliqué au Text à l'intérieur du TextFieldTableCell par défaut.
    }

    // === Boutons d'action ===

    @FXML
    private void ajouterLigne() {
        tablecaract.getItems().add(new caracteristique(0, "", "", "", ""));

        // Sélectionner la nouvelle ligne ajoutée pour permettre à l'utilisateur de commencer l'édition directement
        tablecaract.getSelectionModel().selectLast();
        tablecaract.edit(tablecaract.getSelectionModel().getSelectedIndex(), ElementColumn);  // Mettre le focus sur la première colonne de la nouvelle ligne
    }

    @FXML
    private void supprimerLigne() {
        ObservableList<caracteristique> selectedItems = tablecaract.getSelectionModel().getSelectedItems();

        if (!selectedItems.isEmpty()) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer les éléments sélectionnés ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try (Connection conn = Database.connectDB()) {
                        for (caracteristique c : selectedItems) {
                            if (c.getIdEquipement() != 0) {
                                try (PreparedStatement delete = conn.prepareStatement(
                                        "DELETE FROM stock WHERE id_équipement = ?")) {
                                    delete.setInt(1, c.getIdEquipement());
                                    delete.executeUpdate();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression dans la base de données.").showAndWait();
                    }

                    loadDataFromDatabase();
                }
            });

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Suppression impossible");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ou plusieurs lignes à supprimer.");
            alert.showAndWait();
        }
    }


    // === Navigation entre pages ===

    @FXML private void AfficherAcceuil(ActionEvent event) throws IOException {
        Main.switchScene("PageAcceuil.fxml");
    }

    @FXML private void AfficherEmploiDuTemps(ActionEvent event) throws IOException {
        Main.switchScene("EmploiDuTemps.fxml");
    }

    @FXML private void AfficherStock(ActionEvent event) throws IOException {
        Main.switchScene("Stock.fxml");
    }

    @FXML private void AfficherLogging(ActionEvent event) throws IOException {
        Main.switchScene("Logging.fxml");
    }

    @FXML private void Deconnecter(ActionEvent event) throws IOException {
        Main.switchScene("Login.fxml");
    }

    @FXML private void openfiltrage(ActionEvent event) throws IOException {
        Stage popupWindow = Main.openPopupWindow2("FiltrageSalle.fxml", "filtrer", 550, 350);
        popupWindow.setX(40);
        popupWindow.setY(160);
    }

    // === Changement de thème ===

    @FXML
    private void changertheme() {
        Main.setDarkMode(ModeToggle.isSelected());

        applyCellFactories();
    }

    // === Classe interne modèle ===

    public static class caracteristique {
        private int idEquipement;
        private final SimpleStringProperty Nom_équipement;
        private final SimpleStringProperty Caractéristique;
        private final SimpleStringProperty Neuf;
        private final SimpleStringProperty Utilise;


        public caracteristique(int idEquipement, String nom, String caracteristique, String neuf, String utilise) {
            this.idEquipement = idEquipement;
            this.Nom_équipement = new SimpleStringProperty(nom);
            this.Caractéristique = new SimpleStringProperty(caracteristique);
            this.Neuf = new SimpleStringProperty(neuf);
            this.Utilise = new SimpleStringProperty(utilise);


        }

        public String getElement() { return Nom_équipement.get(); }
        public void setElement(String e) { this.Nom_équipement.set(e); }

        public String getType() { return Caractéristique.get(); }
        public void setType(String t) { this.Caractéristique.set(t); }

        public String getNeuf() {return Neuf.get();}
        public void setNeuf(String n) {this.Neuf.set(n);}

        public String getUtilise() {return Utilise.get();}
        public void setUtilise(String u) {this.Utilise.set(u);}

        public int getIdEquipement() { return idEquipement; }
        public void setIdEquipement(int idEquipement) { this.idEquipement=idEquipement; }
    }

}