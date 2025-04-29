package com.example.gestiontp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Stage mainStage;
    private static boolean darkMode = false; // Add a static variable to track dark mode
    private static FXMLLoader loader;
    public static FXMLLoader getLoader() {
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {


        // Load fonts

        try {
            URL fontsDirUrl = getClass().getResource("/Fonts/");
            if (fontsDirUrl != null) {
                File fontsDir = new File(fontsDirUrl.toURI());
                File[] fontFiles = fontsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".ttf"));

                if (fontFiles != null) {
                    for (File fontFile : fontFiles) {
                        Font font = Font.loadFont(fontFile.toURI().toURL().toExternalForm(), 12);
                        if (font != null) {
                            System.out.println("Font loaded: " + font.getName());
                        } else {
                            System.out.println("Failed to load font: " + fontFile.getName());
                        }
                    }
                } else {
                    System.out.println("No .ttf font files found in /Fonts/");
                }
            } else {
                System.out.println("Fonts directory not found!");
            }
        } catch (Exception e) {
            System.out.println("Error loading fonts: " + e.getMessage());
            e.printStackTrace();
        }

        mainStage = primaryStage;

        switchScene("/com/example/gestiontp/Login.fxml");
        mainStage.setTitle("Système de gestion des salles TP");

        mainStage.show();
    }

    public static void switchScene(String fxmlFile) throws IOException {
        if (mainStage == null) {
            System.out.println("Error: mainStage is not initialized!");
            return;
        }

        File file = resolveFXMLFile(fxmlFile);
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        Scene scene = new Scene(loader.load(), 1000, 1000);

        mainStage.setScene(scene);
        mainStage.setWidth(1920);
        mainStage.setHeight(1080);

        mainStage.setMaximized(true);



        loadCSS(scene);
        applyDarkMode(scene); // Apply dark mode if enabled
    }

    // Basic popup window (without auto-close on focus loss)
    public static Stage openPopupWindow(String fxmlFile, String title, double width, double height) throws IOException {
        return createPopupStage(fxmlFile, title, width, height, false);
    }

    // Popup window that closes when clicking outside or when main closes
    public static Stage openPopupWindow2(String fxmlFile, String title, double width, double height) throws IOException {
        return createPopupStage(fxmlFile, title, width, height, true);
    }

    // Shared logic for creating a popup
    private static Stage createPopupStage(String fxmlFile, String title, double width, double height, boolean autoCloseOnFocusLoss) throws IOException {
        File file = resolveFXMLFile(fxmlFile);
         loader = new FXMLLoader(file.toURI().toURL());
        Parent root = loader.load();

        Stage popupStage = new Stage();
        Scene scene = new Scene(root, width, height);
        popupStage.setScene(scene);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.setTitle(title);
        popupStage.initOwner(mainStage);
        popupStage.initModality(Modality.NONE);

        if (autoCloseOnFocusLoss) {
            popupStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    popupStage.close();
                }
            });

            mainStage.setOnCloseRequest(event -> {
                if (popupStage.isShowing()) {
                    popupStage.close();
                }
            });
        }

        // Optional: Make the popup draggable
        final double[] xOffset = {0};
        final double[] yOffset = {0};
        root.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            popupStage.setX(event.getScreenX() - xOffset[0]);
            popupStage.setY(event.getScreenY() - yOffset[0]);
        });

        loadCSS(scene);
        applyDarkMode(scene); // Apply dark mode if enabled
        popupStage.show();
        return popupStage;
    }

    // Helper to load CSS
    private static void loadCSS(Scene scene) {
        try {
            File cssFile = new File("src/main/resources/CSS/Style.css");
            if (cssFile.exists()) {
                String cssUrl = cssFile.toURI().toURL().toExternalForm();
                scene.getStylesheets().add(cssUrl);
                System.out.println("CSS added to scene successfully");
            } else {
                System.out.println("Warning: style.css not found!");
            }
        } catch (Exception e) {
            System.out.println("Error loading CSS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper to resolve FXML file path
    private static File resolveFXMLFile(String fxmlFile) throws IOException {
        File file = new File("src/main/resources" + fxmlFile);
        if (!file.exists()) {
            file = new File("src/main/resources/com/example/gestiontp/" + fxmlFile);
            if (!file.exists()) {
                System.out.println("FXML file not found at: " + file.getAbsolutePath());
                throw new IOException("Could not find FXML file in any expected location");
            }
        }
        return file;
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setDarkMode(boolean darkMode) {
        Main.darkMode = darkMode;
        if (mainStage != null && mainStage.getScene() != null) {
            applyDarkMode(mainStage.getScene());
        }
    }

    private static void applyDarkMode(Scene scene) {
        if (darkMode) {
            scene.getRoot().getStyleClass().add("Dark-mode");
        } else {
            scene.getRoot().getStyleClass().remove("Dark-mode");
        }
    }

    public static boolean isDarkMode() {
        return darkMode;
    }
}