package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.EquipeProjetoDAO;
import com.ruandev.taskflow.dao.interfaces.MetaDAO;
import com.ruandev.taskflow.dao.interfaces.UsuarioDAO;
import com.ruandev.taskflow.entities.Meta;
import com.ruandev.taskflow.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CriaMetaController {

    @FXML
    private TextField tituloField;

    @FXML
    private MenuButton responsavelMenuButton;

    @FXML
    private DatePicker prazoPicker;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button criarButton;

    private final MetaDAO metaDAO = DAOFactory.getMetaDAO();
    private final UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
    private final EquipeProjetoDAO equipeProjetoDAO = DAOFactory.getEquipeProjetoDAO();

    // ID do projeto em que a meta vai ser criada
    private int idProjeto;

    // Usuário selecionado
    private Usuario responsavelSelecionado;

    // ⚡ Callback para notificar o ProjetoController
    private Runnable onAtualizacao;

    public void setOnAtualizacao(Runnable onAtualizacao) {
        this.onAtualizacao = onAtualizacao;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    @FXML
    private void initialize() {
        // Botões configurados no FXML com onAction
    }

    // Carregar os membros no MenuButton
    public void carregarMembros() {
        try {
            List<Usuario> membros = equipeProjetoDAO.findUsuariosByProjeto(idProjeto);

            responsavelMenuButton.getItems().clear();

            for (Usuario usuario : membros) {
                MenuItem item = new MenuItem(usuario.getNome());
                item.setOnAction(e -> {
                    responsavelSelecionado = usuario;
                    responsavelMenuButton.setText(usuario.getNome());
                });
                responsavelMenuButton.getItems().add(item);
            }

            responsavelMenuButton.setText("Selecione o responsável");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar os membros do projeto.");
        }
    }

    @FXML
    private void handleCriarMeta() {
        String titulo = tituloField.getText();

        if (titulo == null || titulo.isBlank() || responsavelSelecionado == null) {
            showAlert("Campos obrigatórios", "Preencha todos os campos antes de criar.");
            return;
        }

        try {
            Meta meta = new Meta();
            meta.setTitulo(titulo);
            meta.setIdChefe(responsavelSelecionado.getId());
            meta.setIdProjeto(idProjeto);

            if (prazoPicker.getValue() != null) {
                meta.setPrazo(prazoPicker.getValue());
            }

            metaDAO.insert(meta);

            showAlert("Sucesso", "Meta criada com sucesso!");

            // ⚡ Chama o callback para atualizar a tela anterior
            if (onAtualizacao != null) {
                onAtualizacao.run();
            }

            fecharJanela();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível criar a meta.");
        }
    }

    @FXML
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
