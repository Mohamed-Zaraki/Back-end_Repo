package com.example.gestiontp;



import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;



public class LoginController {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button connecterbutton;

    @FXML
    private Text errorMessage;

    //Database Tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    @FXML
    private void handleLogin() throws IOException {
        String usernameText = username.getText();
        String passwordText = password.getText();

        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            errorMessage.setText("Veuillez remplir tous les champs !");
        }

        else {
            String sql="SELECT * FROM utilisateur WHERE Nom_utilisateur = ? ";
            connect=Database.connectDB();

            try{
                prepare=connect.prepareStatement(sql);
                prepare.setString(1,username.getText());

                result= prepare.executeQuery();

                if(result.next())
                {
                    String storedHashedPassword  = result.getString("Mot_de_passe");
                    // Compare the stored hashed password with the entered password
                    if (BCrypt.checkpw(passwordText, storedHashedPassword)) {
                        System.out.println("Login successful");
                        errorMessage.setText("Connexion réussie !");
                        Main.switchScene("PageAcceuil.fxml");
                    } else {
                        System.out.println("Mot de passe incorrect !");
                        errorMessage.setText("Mot de passe incorrect !");
                        password.clear();
                    }

                }
                else
                {
                    System.out.println("user incorrect");
                    errorMessage.setText("Utilisateur introuvable !");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @FXML
    private void openSignUp() throws IOException {
        Main.switchScene("signup.fxml");
    }
}
