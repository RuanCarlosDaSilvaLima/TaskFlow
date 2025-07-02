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

public class RegisterController {

    @FXML
    private TextField usuarioField;

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
    private Button cadastrarButton;

    @FXML
    private Label tenhoCadastroLabel;

    private boolean passwordVisible = false;

    private final UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();

    private final Image openEyeIcon = new Image(getClass().getResource("/com/ruandev/taskflow/imagens/senha-visivel.png").toExternalForm());
    private final Image closeEyeIcon = new Image(getClass().getResource("/com/ruandev/taskflow/imagens/senha-invisivel.png").toExternalForm());

    @FXML
    private void initialize() {
        eyeIcon.setImage(closeEyeIcon);
        visiblePasswordField.setVisible(false);
    }

    @FXML
    private void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            visiblePasswordField.setVisible(false);
            eyeIcon.setImage(closeEyeIcon);
        } else {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            passwordField.setVisible(false);
            eyeIcon.setImage(openEyeIcon);
        }
        passwordVisible = !passwordVisible;
    }

    @FXML
    private void handleCadastrar(ActionEvent event) {
        String nome = usuarioField.getText().trim();
        String email = emailField.getText().trim();
        String senha = passwordVisible ? visiblePasswordField.getText() : passwordField.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            showAlert("Campos obrigatórios", "Preencha todos os campos antes de continuar!");
            return;
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuario.setFotoPerfil(null);  // forçando DAO a colocar a padrão

            usuarioDAO.insert(usuario);

            showAlert("Cadastro realizado", "Usuário cadastrado com sucesso!");
            voltarParaLogin();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Ocorreu um erro ao cadastrar usuário.");
        }
    }

    @FXML
    private void voltarParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/login-view.fxml"));
            Stage stage = (Stage) tenhoCadastroLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de login.");
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
