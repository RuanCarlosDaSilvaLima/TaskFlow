package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.UsuarioDAO;
import com.ruandev.taskflow.dao.interfaces.EquipeProjetoDAO;
import com.ruandev.taskflow.entities.Projeto;
import com.ruandev.taskflow.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class EditarMembrosController {

    @FXML
    private ListView<String> membrosList;

    @FXML
    private MenuButton usuariosMenu;

    @FXML
    private Button adicionarButton;

    @FXML
    private Button removerButton;

    @FXML
    private Button cancelarButton;

    private Projeto projeto;

    private final UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
    private final EquipeProjetoDAO equipeProjetoDAO = DAOFactory.getEquipeProjetoDAO();

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
        carregarMembros();
        carregarUsuarios();
    }

    private void carregarMembros() {
        try {
            membrosList.getItems().clear();
            List<Usuario> membros = equipeProjetoDAO.findUsuariosByProjeto(projeto.getId());
            for (Usuario u : membros) {
                membrosList.getItems().add(u.getNome() + " (id=" + u.getId() + ")");
            }
        } catch (Exception e) {
            showAlert("Erro", "Não foi possível carregar os membros.");
            e.printStackTrace();
        }
    }

    private void carregarUsuarios() {
        try {
            usuariosMenu.getItems().clear();
            List<Usuario> usuarios = usuarioDAO.findAll();
            for (Usuario u : usuarios) {
                MenuItem item = new MenuItem(u.getNome() + " (id=" + u.getId() + ")");
                item.setOnAction(e -> usuariosMenu.setText(item.getText()));
                usuariosMenu.getItems().add(item);
            }
        } catch (Exception e) {
            showAlert("Erro", "Não foi possível carregar os usuários.");
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        adicionarButton.setOnAction(e -> handleAdicionar());
        removerButton.setOnAction(e -> handleRemover());
        cancelarButton.setOnAction(e -> fechar());
    }

    private void handleAdicionar() {
        try {
            String texto = usuariosMenu.getText();
            if (texto == null || texto.equals("Selecionar usuário")) {
                showAlert("Validação", "Selecione um usuário para adicionar.");
                return;
            }

            int idUsuario = extrairIdDoTexto(texto);
            equipeProjetoDAO.adicionarMembro(projeto.getId(), idUsuario);

            showAlert("Sucesso", "Membro adicionado com sucesso!");
            carregarMembros();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível adicionar o membro.");
        }
    }

    private void handleRemover() {
        try {
            String selecionado = membrosList.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                showAlert("Validação", "Selecione um membro para remover.");
                return;
            }

            int idUsuario = extrairIdDoTexto(selecionado);
            equipeProjetoDAO.removerMembro(projeto.getId(), idUsuario);

            showAlert("Sucesso", "Membro removido com sucesso!");
            carregarMembros();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível remover o membro.");
        }
    }

    private int extrairIdDoTexto(String texto) {
        // Espera formato "Nome (id=1)"
        int start = texto.indexOf("id=") + 3;
        int end = texto.indexOf(")", start);
        return Integer.parseInt(texto.substring(start, end));
    }

    private void fechar() {
        ((Stage) cancelarButton.getScene().getWindow()).close();
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
