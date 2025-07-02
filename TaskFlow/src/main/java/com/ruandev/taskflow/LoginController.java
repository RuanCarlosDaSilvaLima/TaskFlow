package com.ruandev.taskflow;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.UsuarioDAO;
import com.ruandev.taskflow.entities.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private Button togglePasswordButton;

    @FXML
    private ImageView eyeIcon;

    @FXML
    private Button entrarButton;

    @FXML
    private Label cadastroLabel;

    private boolean passwordVisible = false;

    private final UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();

    private final Image openEyeIcon = new Image(getClass().getResource("/com/ruandev/taskflow/imagens/senha-visivel.png").toExternalForm());
    private final Image closedEyeIcon = new Image(getClass().getResource("/com/ruandev/taskflow/imagens/senha-invisivel.png").toExternalForm());

    @FXML
    private void initialize() {
        eyeIcon.setImage(closedEyeIcon);
        visiblePasswordField.setVisible(false);
    }

    @FXML
    private void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            visiblePasswordField.setVisible(false);
            eyeIcon.setImage(closedEyeIcon);
        } else {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            passwordField.setVisible(false);
            eyeIcon.setImage(openEyeIcon);
        }
        passwordVisible = !passwordVisible;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String senha = passwordVisible ? visiblePasswordField.getText() : passwordField.getText();


        if (email.isEmpty() || senha.isEmpty()) {
            showAlert("Erro de Login", "Por favor, preencha email e senha.");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.findByEmailESenha(email, senha);

            if (usuario != null) {
                abrirTelaPrincipal();
            } else {
                showAlert("Login inválido", "Email ou senha incorretos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Ocorreu um erro ao tentar fazer login.");
        }
    }

    @FXML
    private void goToCadastro(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/register-view.fxml"));
            Stage stage = (Stage) cadastroLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de cadastro.");
        }
    }

    private void abrirTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/workspace-view.fxml"));
            Stage stage = (Stage) entrarButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela principal.");
        }
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
