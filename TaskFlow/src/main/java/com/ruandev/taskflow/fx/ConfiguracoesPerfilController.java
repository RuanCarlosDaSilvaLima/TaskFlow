package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.fx.Sessao;
import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.UsuarioDAO;
import com.ruandev.taskflow.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfiguracoesPerfilController {

    @FXML
    private Label nomeAtualLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField nomeNovoField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private TextField senhaVisibleField;

    @FXML
    private ImageView fotoImageView;

    @FXML
    private Button togglePasswordButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button salvarButton;

    private final UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
    private boolean passwordVisible = false;
    private byte[] novaFotoBytes = null;

    private final Image openEyeIcon = new Image(getClass().getResource("/com/ruandev/taskflow/imagens/senha-visivel.png").toExternalForm());
    private final Image closedEyeIcon = new Image(getClass().getResource("/com/ruandev/taskflow/imagens/senha-invisivel.png").toExternalForm());
    private final ImageView eyeImageView = new ImageView();  // FIXO para evitar sumir

    @FXML
    private void initialize() {
        Usuario usuario = Sessao.getUsuario();
        if (usuario == null) {
            showAlert("Erro", "Nenhum usuário logado!");
            fecharJanela();
            return;
        }

        nomeAtualLabel.setText(usuario.getNome());
        emailLabel.setText(usuario.getEmail());
        nomeNovoField.setText(usuario.getNome());

        // Carregar foto
        if (usuario.getFotoPerfil() != null) {
            fotoImageView.setImage(new Image(new ByteArrayInputStream(usuario.getFotoPerfil())));
        } else {
            fotoImageView.setImage(new Image(getClass().getResource("/com/ruandev/taskflow/imagens/icone-usuario.png").toExternalForm()));
        }

        senhaVisibleField.setVisible(false);
        senhaField.setText("");
        senhaVisibleField.setText("");

        // Configurar ImageView fixo
        eyeImageView.setFitWidth(24);
        eyeImageView.setFitHeight(24);
        eyeImageView.setPreserveRatio(true);
        eyeImageView.setImage(closedEyeIcon);
        togglePasswordButton.setGraphic(eyeImageView);
    }

    @FXML
    private void togglePasswordVisibility() {
        if (passwordVisible) {
            senhaField.setText(senhaVisibleField.getText());
            senhaField.setVisible(true);
            senhaVisibleField.setVisible(false);
            eyeImageView.setImage(closedEyeIcon);
        } else {
            senhaVisibleField.setText(senhaField.getText());
            senhaVisibleField.setVisible(true);
            senhaField.setVisible(false);
            eyeImageView.setImage(openEyeIcon);
        }
        passwordVisible = !passwordVisible;
    }

    @FXML
    private void handleAlterarFoto(javafx.scene.input.MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher nova foto de perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(fotoImageView.getScene().getWindow());
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                novaFotoBytes = fis.readAllBytes();
                fotoImageView.setImage(new Image(new ByteArrayInputStream(novaFotoBytes)));
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erro", "Não foi possível carregar a imagem.");
            }
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    @FXML
    private void handleSalvar() {
        Usuario usuario = Sessao.getUsuario();
        if (usuario == null) {
            showAlert("Erro", "Nenhum usuário logado!");
            return;
        }

        String novoNome = nomeNovoField.getText().trim();
        String novaSenha = passwordVisible ? senhaVisibleField.getText().trim() : senhaField.getText().trim();

        if (novoNome.isEmpty()) {
            showAlert("Validação", "O nome não pode ser vazio.");
            return;
        }

        usuario.setNome(novoNome);

        if (!novaSenha.isEmpty()) {
            usuario.setSenha(novaSenha);
        }

        if (novaFotoBytes != null) {
            usuario.setFotoPerfil(novaFotoBytes);
        }

        try {
            usuarioDAO.update(usuario);
            Sessao.setUsuario(usuario);
            showAlert("Sucesso", "Dados atualizados com sucesso!");
            fecharJanela();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível atualizar os dados.");
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
