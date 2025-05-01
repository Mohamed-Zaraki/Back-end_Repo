package com.example.gestiontp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoggingController implements Initializable {

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private VBox contentVBox;

    // Table Logiciel
    @FXML private TableView<PanneLogiciel> tableLogiciel;
    @FXML private TableColumn<PanneLogiciel, String> dateColumn;
    @FXML private TableColumn<PanneLogiciel, String> salleColumn;
    @FXML private TableColumn<PanneLogiciel, String> detailColumn;
    @FXML private TableColumn<PanneLogiciel, String> degreColumn;
    @FXML private TableColumn<PanneLogiciel, String> maintenanceColumn;

    // Table Matériel
    @FXML private TableView<PanneMateriel> tableMateriel;
    @FXML private TableColumn<PanneMateriel, String> dateMateriel;
    @FXML private TableColumn<PanneMateriel, String> salleMateriel;
    @FXML private TableColumn<PanneMateriel, String> detailMateriel;
    @FXML private TableColumn<PanneMateriel, String> degreMateriel;
    @FXML private TableColumn<PanneMateriel, String> maintenanceMateriel;

    @FXML private ToggleButton ModeToggle;

    // Fixed cell height for calculations
    private static final double CELL_HEIGHT = 30.0;
    private static final double HEADER_HEIGHT = 25.0;
    private static final double TABLE_PADDING = 5.0;

    @FXML
    private void initialize() {
        // Force the content to be larger than the viewport to ensure scrolling works
        contentVBox.setMinHeight(1200);

        // Make sure the ScrollPane can scroll
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setPannable(true);



        // Disable focus traversal for containers
        mainScrollPane.setFocusTraversable(false);
        contentVBox.setFocusTraversable(false);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSettings();
        loadPannes(); // FIRST load the pannes from the database
        initializeTableLogiciel();
        initializeTableMateriel();
        loadPannes();

        // Appliquer le style
        URL styleUrl = getClass().getResource("/com/example/gestiontp/Style.css");
        if (styleUrl != null) {
            String stylePath = styleUrl.toExternalForm();
            tableLogiciel.getStylesheets().add(stylePath);
            tableMateriel.getStylesheets().add(stylePath);

        } else {
            System.err.println("Could not load Style.css");
        }
        setupListeners();
    }

    private void initializeTableLogiciel() {
        // Association des colonnes
        dateColumn.setCellValueFactory(cell -> cell.getValue().date);
        salleColumn.setCellValueFactory(cell -> cell.getValue().salle);
        detailColumn.setCellValueFactory(cell -> cell.getValue().detail);
        degreColumn.setCellValueFactory(cell -> cell.getValue().degre);
        maintenanceColumn.setCellValueFactory(cell -> cell.getValue().maintenance);

        // Édition
        tableLogiciel.setEditable(true);
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        salleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        detailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        degreColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // CheckBox pour maintenance
        maintenanceColumn.setCellFactory(col -> createCheckBoxCellLogiciel());

        // Hyperlink pour impression

        // Sauvegarde des modifications
        dateColumn.setOnEditCommit(e -> {
            e.getRowValue().setDate(e.getNewValue());
            saveOrUpdatePanne(e.getRowValue(), "Logicielle");
            saveLogicielToExcel();
        });
        salleColumn.setOnEditCommit(e -> {
            e.getRowValue().setSalle(e.getNewValue());
            saveOrUpdatePanne(e.getRowValue(), "Logicielle");
            saveLogicielToExcel();
        });
        detailColumn.setOnEditCommit(e -> {
            e.getRowValue().setDetail(e.getNewValue());
            saveOrUpdatePanne(e.getRowValue(), "Logicielle");
            saveLogicielToExcel();
        });
        degreColumn.setOnEditCommit(e -> {
            e.getRowValue().setDegre(e.getNewValue());
            saveOrUpdatePanne(e.getRowValue(), "Logicielle");
            saveLogicielToExcel();
        });


        tableLogiciel.setItems(panneLogicielList);


        tableLogiciel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableLogiciel.setFixedCellSize(CELL_HEIGHT);

        // Update table height based on number of rows
        updateTableHeight(tableLogiciel, panneLogicielList.size());

    }

    private void initializeTableMateriel() {
        dateMateriel.setCellValueFactory(cell -> cell.getValue().date);
        salleMateriel.setCellValueFactory(cell -> cell.getValue().salle);
        detailMateriel.setCellValueFactory(cell -> cell.getValue().detail);
        degreMateriel.setCellValueFactory(cell -> cell.getValue().degre);
        maintenanceMateriel.setCellValueFactory(cell -> cell.getValue().maintenance);

        tableMateriel.setEditable(true);
        dateMateriel.setCellFactory(TextFieldTableCell.forTableColumn());
        salleMateriel.setCellFactory(TextFieldTableCell.forTableColumn());
        detailMateriel.setCellFactory(TextFieldTableCell.forTableColumn());
        degreMateriel.setCellFactory(TextFieldTableCell.forTableColumn());

        maintenanceMateriel.setCellFactory(col -> createCheckBoxCellMateriel());

        // Hyperlink pour impression

        dateMateriel.setOnEditCommit(e -> {
            e.getRowValue().setDate(e.getNewValue());
            saveOrUpdatePanne(e.getRowValue(), "Matérielle");
            saveMaterielToExcel();
        });
        salleMateriel.setOnEditCommit(e -> {
            e.getRowValue().setSalle(e.getNewValue());
            saveOrUpdatePanne(e.getRowValue(), "Matérielle");
            saveMaterielToExcel();
        });
        detailMateriel.setOnEditCommit(e -> {
            e.getRowValue().setDetail(e.getNewValue());
            saveOrUpdatePanne(e.getRowValue(), "Matérielle");
            saveMaterielToExcel();
        });
        degreMateriel.setOnEditCommit(e -> {
            e.getRowValue().setDegre(e.getNewValue());
            saveOrUpdatePanne(e.getRowValue(), "Matérielle");
            saveMaterielToExcel();
        });


        tableMateriel.setItems(panneMaterielList);

        tableMateriel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableMateriel.setFixedCellSize(CELL_HEIGHT);
        // Update table height based on number of rows
        updateTableHeight(tableMateriel, panneMaterielList.size());

    }



    /**
     * Updates the height of a table based on its number of items
     */
    private <T> void updateTableHeight(TableView<T> tableView, int rowCount) {
        // Calculate height based on row count plus header
        double height = HEADER_HEIGHT + (CELL_HEIGHT * rowCount) + TABLE_PADDING;
        tableView.setPrefHeight(height);
        tableView.setMinHeight(height);
        tableView.setMaxHeight(height);
    }

    private TableCell<PanneLogiciel, String> createCheckBoxCellLogiciel() {
        return new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal && getIndex() < getTableView().getItems().size()) {
                        PanneLogiciel item = getTableView().getItems().get(getIndex());
                        deletePanneLogiciel(item);
                        tableLogiciel.getItems().remove(item);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : checkBox);
                if (!empty) checkBox.setSelected("✔".equals(item));
            }
        };
    }


    private TableCell<PanneMateriel, String> createCheckBoxCellMateriel() {
        return new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal && getIndex() < getTableView().getItems().size()) {
                        PanneMateriel item = getTableView().getItems().get(getIndex());
                        deletePanneMateriel(item);
                        tableMateriel.getItems().remove(item);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : checkBox);
                if (!empty) checkBox.setSelected("✔".equals(item));
            }
        };
    }


    private void updateTableHeight(TableView<?> table) {
        double rowHeight = table.getFixedCellSize();
        double headerHeight = 25; // Approximate header height
        int rowCount = table.getItems().size();

        // Calculate new height based on number of rows (plus header)
        double newHeight = headerHeight + (rowCount * rowHeight);

        // Set minimum height to ensure scrolling works
        table.setMinHeight(newHeight);
        table.setPrefHeight(newHeight);

        // Force layout recalculation
        table.requestLayout();

        // Update the content VBox height to ensure scrolling works
        double totalHeight = 50 + // Top padding
                30 + // JOURNAL label
                30 + // Spacing
                tableLogiciel.getPrefHeight() +
                30 + // Spacing
                tableMateriel.getPrefHeight() +
                50 + // Bottom padding
                100; // Extra space

        contentVBox.setMinHeight(Math.max(1200, totalHeight));
    }

    @FXML
    private void ajouterLigneLogiciel() {
        PanneLogiciel newPanne = new PanneLogiciel(
                0,                           // idPanne = 0 because it's a new one
                LocalDate.now().toString(),   // today's date
                "-",                    // default salle
                "Nouvelle panne",             // default detail
                "-",                          // default degre
                ""                            // maintenance
        );

        tableLogiciel.getItems().add(newPanne);
        saveOrUpdatePanne(newPanne, "Logicielle");
        updateTableHeight(tableLogiciel, tableLogiciel.getItems().size());
    }


    @FXML
    private void ajouterLigneMateriel() {
        PanneMateriel newPanne = new PanneMateriel(
                0,                           // idPanne = 0 pck jdida
                LocalDate.now().toString(),
                "-",                    // default salle
                "-",             // default detail
                "1",                          // default degre
                ""                            // maintenance
        );
        tableMateriel.getItems().add(newPanne);
        saveOrUpdatePanne(newPanne, "Matérielle");
        updateTableHeight(tableMateriel, tableMateriel.getItems().size());
    }





    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Suppression impossible");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Navigation
    @FXML private void AfficherAcceuil(ActionEvent event) throws IOException { Main.switchScene("PageAcceuil.fxml"); }
    @FXML private void AfficherEmploiDuTemps(ActionEvent event) throws IOException { Main.switchScene("EmploiDuTemps.fxml"); }
    @FXML private void AfficherStock(ActionEvent event) throws IOException { Main.switchScene("Stock.fxml"); }
    @FXML private void AfficherLogging(ActionEvent event) throws IOException { Main.switchScene("Logging.fxml"); }
    @FXML private void Deconnecter(ActionEvent event) throws IOException { Main.switchScene("Login.fxml"); }

    @FXML private void changertheme() {
        Main.setDarkMode(ModeToggle.isSelected());
    }

    @FXML
    public void openfiltrage(ActionEvent event) throws IOException {
        Stage popupWindow = Main.openPopupWindow2("FiltrageSalle.fxml", "filtrer", 550, 350);
        popupWindow.setX(40);
        popupWindow.setY(160);
    }

    @FXML
    public void effacerLogiciel(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation Suppression");
        confirmation.setHeaderText("Effacer toutes les pannes logicielles ?");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer toutes les pannes logicielles ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    connect = Database.connectDB();

                    String deleteLogiciel = "DELETE FROM Panne WHERE Type_Panne = 'Logicielle'";
                    prepare = connect.prepareStatement(deleteLogiciel);
                    prepare.executeUpdate();

                    tableLogiciel.getItems().clear();
                    System.out.println("Pannes logicielles supprimées avec succès.");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void effacerMateriel(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation Suppression");
        confirmation.setHeaderText("Effacer toutes les pannes matérielles ?");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer toutes les pannes matérielles ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    connect = Database.connectDB();

                    String deleteMateriel = "DELETE FROM Panne WHERE Type_Panne = 'Matérielle'";
                    prepare = connect.prepareStatement(deleteMateriel);
                    prepare.executeUpdate();

                    tableMateriel.getItems().clear();
                    System.out.println("Pannes matérielles supprimées avec succès.");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private File logicielExcelFile = null;
    private File materielExcelFile = null;

    private Properties settings = new Properties();
    private final File settingsFile = new File("settings.properties"); // stocké à la racine du projet

    private void loadSettings() {
        try {
            if (settingsFile.exists()) {
                settings.load(new java.io.FileInputStream(settingsFile));
                String logicielPathStr = settings.getProperty("logicielPath");
                if (logicielPathStr != null) {
                    logicielExcelFile = new File(logicielPathStr);
                }
                String materielPathStr = settings.getProperty("materielPath");
                if (materielPathStr != null) {
                    materielExcelFile = new File(materielPathStr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSettings() {
        try {
            settings.store(new java.io.FileOutputStream(settingsFile), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exporterMaterielVersExcel(ActionEvent event) {
        materielExcelFile = demanderNouveauChemin(materielExcelFile, "MaterielPannes.xlsx", "Choisir un dossier pour Matériel", "materielPath");

        if (materielExcelFile == null) {
            return; // Utilisateur a annulé
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pannes Matérielles");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Date Déclaration");
            headerRow.createCell(2).setCellValue("Salle");
            headerRow.createCell(3).setCellValue("Détails");
            headerRow.createCell(4).setCellValue("Degré Criticité");

            int rowIndex = 1;
            for (PanneMateriel panne : tableMateriel.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(panne.getIdPanne());
                row.createCell(1).setCellValue(panne.getDate());
                row.createCell(2).setCellValue(panne.getSalle());
                row.createCell(3).setCellValue(panne.getDetail());
                row.createCell(4).setCellValue(panne.getDegre());
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(materielExcelFile)) {
                workbook.write(fileOut);
            }

            showSuccessAlert("Le fichier Excel Matériel a été sauvegardé avec succès !", materielExcelFile.getAbsolutePath());
            setupMaterielListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void exporterLogicielVersExcel(ActionEvent event) {
        logicielExcelFile = demanderNouveauChemin(logicielExcelFile, "LogicielPannes.xlsx", "Choisir un dossier pour Logiciel", "logicielPath");

        if (logicielExcelFile == null) {
            return; // Utilisateur a annulé
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pannes Logicielles");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Date Déclaration");
            headerRow.createCell(2).setCellValue("Salle");
            headerRow.createCell(3).setCellValue("Détails");
            headerRow.createCell(4).setCellValue("Degré Criticité");

            int rowIndex = 1;
            for (PanneLogiciel panne : tableLogiciel.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(panne.getIdPanne());
                row.createCell(1).setCellValue(panne.getDate());
                row.createCell(2).setCellValue(panne.getSalle());
                row.createCell(3).setCellValue(panne.getDetail());
                row.createCell(4).setCellValue(panne.getDegre());
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(logicielExcelFile)) {
                workbook.write(fileOut);
            }

            showSuccessAlert("Le fichier Excel Logiciel a été sauvegardé avec succès !", logicielExcelFile.getAbsolutePath());
            setupLogicielListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSuccessAlert(String message, String cheminFichier) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(message);
        alert.setContentText("Chemin du fichier :\n" + cheminFichier);
        alert.showAndWait();
    }




    private void setupLogicielListener() {
        tableLogiciel.getItems().addListener((javafx.collections.ListChangeListener<PanneLogiciel>) change -> {
            saveLogicielToExcel();
        });
    }

    private void setupMaterielListener() {
        tableMateriel.getItems().addListener((javafx.collections.ListChangeListener<PanneMateriel>) change -> {
            saveMaterielToExcel();
        });
    }

    private void saveLogicielToExcel() {
        if (logicielExcelFile == null) return;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pannes Logicielles");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Date Déclaration");
            headerRow.createCell(2).setCellValue("Salle");
            headerRow.createCell(3).setCellValue("Détails");
            headerRow.createCell(4).setCellValue("Degré Criticité");

            int rowIndex = 1;
            for (PanneLogiciel panne : tableLogiciel.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(panne.getIdPanne());
                row.createCell(1).setCellValue(panne.getDate());
                row.createCell(2).setCellValue(panne.getSalle());
                row.createCell(3).setCellValue(panne.getDetail());
                row.createCell(4).setCellValue(panne.getDegre());
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(logicielExcelFile)) {
                workbook.write(fileOut);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void saveMaterielToExcel() {
        if (materielExcelFile == null) return;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pannes Matérielles");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Date Déclaration");
            headerRow.createCell(2).setCellValue("Salle");
            headerRow.createCell(3).setCellValue("Détails");
            headerRow.createCell(4).setCellValue("Degré Criticité");

            int rowIndex = 1;
            for (PanneMateriel panne : tableMateriel.getItems()) { // bien tableMateriel ici
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(panne.getIdPanne());
                row.createCell(1).setCellValue(panne.getDate());
                row.createCell(2).setCellValue(panne.getSalle());
                row.createCell(3).setCellValue(panne.getDetail());
                row.createCell(4).setCellValue(panne.getDegre());
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(materielExcelFile)) {
                workbook.write(fileOut);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean cheminChangeDemande = false;

    private File demanderNouveauChemin(File fichierActuel, String nomFichier, String titreChoix, String settingsKey) {
        cheminChangeDemande = false; // Réinitialiser avant chaque demande

        try {
            if (fichierActuel != null && fichierActuel.exists()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Chemin existant");
                alert.setHeaderText("Un chemin existe déjà !");
                alert.setContentText("Chemin actuel :\n" + fichierActuel.getAbsolutePath() + "\n\nVoulez-vous choisir un NOUVEAU dossier ?");

                ButtonType buttonTypeChange = new ButtonType("Changer");
                ButtonType buttonTypeGarder = new ButtonType("Garder ce chemin");

                alert.getButtonTypes().setAll(buttonTypeChange, buttonTypeGarder);

                var result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == buttonTypeChange) {
                        cheminChangeDemande = true;
                    } else if (result.get() == buttonTypeGarder) {
                        return fichierActuel; // Garder le fichier actuel
                    }
                }
            }

            if (fichierActuel == null || cheminChangeDemande) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle(titreChoix);

                File selectedDirectory = directoryChooser.showDialog(null);
                if (selectedDirectory != null) {
                    fichierActuel = new File(selectedDirectory.getAbsolutePath() + File.separator + nomFichier);
                    settings.setProperty(settingsKey, fichierActuel.getAbsolutePath());
                    saveSettings();
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fichierActuel;
    }


    private void setupListeners() {
        tableLogiciel.getItems().addListener((ListChangeListener<PanneLogiciel>) change -> {
            if (logicielExcelFile != null) {
                saveLogicielToExcel();
            }
        });

        tableMateriel.getItems().addListener((ListChangeListener<PanneMateriel>) change -> {
            if (materielExcelFile != null) {
                saveMaterielToExcel();
            }
        });
    }

    // Classes internes
    public static class PanneLogiciel {
        private final SimpleIntegerProperty idPanne = new SimpleIntegerProperty();
        private final SimpleStringProperty date = new SimpleStringProperty();
        private final SimpleStringProperty salle = new SimpleStringProperty();
        private final SimpleStringProperty detail = new SimpleStringProperty();
        private final SimpleStringProperty degre = new SimpleStringProperty();
        private final SimpleStringProperty maintenance = new SimpleStringProperty();

        public PanneLogiciel(int idPanne,String date, String salle, String detail, String degre, String maintenance) {
            this.idPanne.set(idPanne);
            this.date.set(date);
            this.salle.set(salle);
            this.detail.set(detail);
            this.degre.set(degre);
            this.maintenance.set(maintenance);
        }

        public String getDate() { return date.get(); }
        public void setDate(String val) { date.set(val); }

        public String getSalle() { return salle.get(); }
        public void setSalle(String val) { salle.set(val); }

        public String getDetail() { return detail.get(); }
        public void setDetail(String val) { detail.set(val); }

        public String getDegre() { return degre.get(); }
        public void setDegre(String val) { degre.set(val); }

        public String getMaintenance() { return maintenance.get(); }
        public void setMaintenance(String val) { maintenance.set(val); }

        public int getIdPanne() { return idPanne.get(); }
        public void setIdPanne(int val) { idPanne.set(val); }


    }

    public static class PanneMateriel {
        private final SimpleIntegerProperty idPanne = new SimpleIntegerProperty();
        private final SimpleStringProperty date = new SimpleStringProperty();
        private final SimpleStringProperty salle = new SimpleStringProperty();
        private final SimpleStringProperty detail = new SimpleStringProperty();
        private final SimpleStringProperty degre = new SimpleStringProperty();
        private final SimpleStringProperty maintenance = new SimpleStringProperty();

        public PanneMateriel(int idPanne,String date, String salle, String detail, String degre, String maintenance) {
            this.idPanne.set(idPanne);
            this.date.set(date);
            this.salle.set(salle);
            this.detail.set(detail);
            this.degre.set(degre);
            this.maintenance.set(maintenance);

        }

        public String getDate() { return date.get(); }
        public void setDate(String val) { date.set(val); }

        public String getSalle() { return salle.get(); }
        public void setSalle(String val) { salle.set(val); }

        public String getDetail() { return detail.get(); }
        public void setDetail(String val) { detail.set(val); }

        public String getDegre() { return degre.get(); }
        public void setDegre(String val) { degre.set(val); }

        public String getMaintenance() { return maintenance.get(); }
        public void setMaintenance(String val) { maintenance.set(val); }

        public int getIdPanne() { return idPanne.get(); }
        public void setIdPanne(int val) { idPanne.set(val); }


    }

    //Database Tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private ObservableList<PanneLogiciel> panneLogicielList = FXCollections.observableArrayList();
    private ObservableList<PanneMateriel> panneMaterielList = FXCollections.observableArrayList();

    public void loadPannes() {
        panneLogicielList.clear();
        panneMaterielList.clear();

        connect = Database.connectDB();

        try {
            //System.out.println("Loading pannes..."); // DEBUG

            // Load Logiciel pannes
            String sql = "SELECT Panne.id_Panne, Panne.Date_Déclaration, Panne.Détails, Panne.Degré_Criticité, Subit.Nom_Salle " +
                    "FROM Panne JOIN Subit ON Panne.id_Panne = Subit.id_Panne " +
                    "WHERE Panne.Type_Panne='Logicielle'";
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            int count = 0; // Count how many rows
            while (result.next()) {
                panneLogicielList.add(new PanneLogiciel(
                        result.getInt("id_Panne"),
                        result.getString("Date_Déclaration"),
                        result.getString("Nom_Salle"),
                        result.getString("Détails"),
                        result.getString("Degré_Criticité"),
                        ""
                ));
                // System.out.println("Loaded Logiciel panne: " + result.getString("Détails")); // DEBUG
                //count++;
            }
            //System.out.println("Logiciel pannes found: " + count); // DEBUG

            // Load Materiel pannes
            sql = "SELECT Panne.id_Panne, Panne.Date_Déclaration, Panne.Détails, Panne.Degré_Criticité, Subit.Nom_Salle " +
                    "FROM Panne JOIN Subit ON Panne.id_Panne = Subit.id_Panne " +
                    "WHERE Panne.Type_Panne='Matérielle'";
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            //count = 0; // Reset count
            while (result.next()) {
                panneMaterielList.add(new PanneMateriel(
                        result.getInt("id_Panne"),
                        result.getString("Date_Déclaration"),
                        result.getString("Nom_Salle"),
                        result.getString("Détails"),
                        result.getString("Degré_Criticité"),
                        ""
                ));
                // System.out.println("Loaded Matériel panne: " + result.getString("Détails")); // DEBUG
                // count++;
            }
            //System.out.println("Matériel pannes found: " + count); // DEBUG

            tableLogiciel.setItems(panneLogicielList);
            tableMateriel.setItems(panneMaterielList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void saveOrUpdatePanne(Object panne, String type) {
        try {
            connect = Database.connectDB();

            String date = "";
            String salle = "";
            String detail = "";
            String degre = "";
            int idPanne = -1;

            if (panne instanceof PanneLogiciel pl) {
                date = pl.getDate();
                salle = pl.getSalle();
                detail = pl.getDetail();
                degre = pl.getDegre();
                idPanne = pl.getIdPanne();
            } else if (panne instanceof PanneMateriel pm) {
                date = pm.getDate();
                salle = pm.getSalle();
                detail = pm.getDetail();
                degre = pm.getDegre();
                idPanne = pm.getIdPanne();
            } else {
                System.err.println("Unknown type of panne!");
                return;
            }

            if (idPanne == -1 || idPanne == 0) {
                // INSERT into Panne
                String insertPanneSql = "INSERT INTO Panne (Date_Déclaration, Détails, Degré_Criticité, Type_Panne) VALUES (?, ?, ?, ?)";
                prepare = connect.prepareStatement(insertPanneSql, Statement.RETURN_GENERATED_KEYS);
                prepare.setString(1, date);
                prepare.setString(2, detail);
                prepare.setString(3, degre);
                prepare.setString(4, type);
                prepare.executeUpdate();


                result = prepare.getGeneratedKeys();
                if (result.next()) {
                    idPanne = result.getInt(1);
                    if (panne instanceof PanneLogiciel pl) pl.setIdPanne(idPanne);
                    else if (panne instanceof PanneMateriel pm) pm.setIdPanne(idPanne);
                }

                result.close();
                prepare.close();
            } else {
                // UPDATE Panne
                String updatePanneSql = "UPDATE Panne SET Date_Déclaration=?, Détails=?, Degré_Criticité=?, Type_Panne=? WHERE id_Panne=?";
                prepare = connect.prepareStatement(updatePanneSql);
                prepare.setString(1, date);
                prepare.setString(2, detail);
                prepare.setString(3, degre);
                prepare.setString(4, type);
                prepare.setInt(5, idPanne);
                prepare.executeUpdate();
                prepare.close(); // 🔥 Close after update Panne
            }

            // NOW: Insert or Update into Subit
            String checkSubitSql = "SELECT * FROM Subit WHERE id_Panne = ?";
            prepare = connect.prepareStatement(checkSubitSql);
            prepare.setInt(1, idPanne);
            result = prepare.executeQuery();

            if (result.next()) {
                // Subit already exists, update it
                result.close();
                prepare.close();

                String updateSubitSql = "UPDATE Subit SET Nom_Salle=? WHERE id_Panne=?";
                prepare = connect.prepareStatement(updateSubitSql);
                prepare.setString(1, salle);
                prepare.setInt(2, idPanne);
                prepare.executeUpdate();
            } else {
                // Subit does not exist, insert it
                result.close();
                prepare.close();

                String insertSubitSql = "INSERT INTO Subit (Nom_Salle, id_Panne) VALUES (?, ?)";
                prepare = connect.prepareStatement(insertSubitSql);
                prepare.setString(1, salle);
                prepare.setInt(2, idPanne);
                prepare.executeUpdate();
            }

            prepare.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void deletePanneLogiciel(PanneLogiciel panne) {
        try {
            connect = Database.connectDB();
            String sql = "DELETE FROM Panne WHERE Date_Déclaration=? AND Détails=? AND Degré_Criticité=?";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, panne.getDate());
            prepare.setString(2, panne.getDetail());
            prepare.setString(3, panne.getDegre());
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePanneMateriel(PanneMateriel panne) {
        try {
            connect = Database.connectDB();
            String sql = "DELETE FROM Panne WHERE Date_Déclaration=? AND Détails=? AND Degré_Criticité=?";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, panne.getDate());
            prepare.setString(2, panne.getDetail());
            prepare.setString(3, panne.getDegre());
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
