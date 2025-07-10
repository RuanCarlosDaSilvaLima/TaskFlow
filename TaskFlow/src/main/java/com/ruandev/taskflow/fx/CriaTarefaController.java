package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.EquipeProjetoDAO;
import com.ruandev.taskflow.dao.interfaces.TarefaDAO;
import com.ruandev.taskflow.entities.Tarefa;
import com.ruandev.taskflow.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class CriaTarefaController {

    @FXML
    private TextField tituloField;

    @FXML
    private MenuButton responsavelMenuButton;

    @FXML
    private DatePicker prazoPicker;

    @FXML
    private TextArea descricaoArea;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button salvarButton;

    private final TarefaDAO tarefaDAO = DAOFactory.getTarefaDAO();
    private final EquipeProjetoDAO equipeProjetoDAO = DAOFactory.getEquipeProjetoDAO();

    private int idMeta;
    private int idProjeto;
    private Usuario responsavelSelecionado;

    public void setIdMeta(int idMeta) {
        this.idMeta = idMeta;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    @FXML
    private void initialize() {
        // Botões já estão configurados por onAction no FXML
    }

    public void carregarMembros() {
        if (idProjeto <= 0) {
            showAlert("Erro", "ID do projeto não foi definido!");
            return;
        }

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
    private void handleSalvar() {
        String titulo = tituloField.getText();
        String descricao = descricaoArea.getText();

        if (titulo == null || titulo.isBlank() || responsavelSelecionado == null) {
            showAlert("Campos obrigatórios", "Preencha todos os campos antes de salvar.");
            return;
        }

        try {
            Tarefa tarefa = new Tarefa();
            tarefa.setTitulo(titulo);
            tarefa.setDescricao(descricao);
            tarefa.setDataCriacao(LocalDateTime.now());
            tarefa.setIdMeta(idMeta);
            tarefa.setIdResponsavel(responsavelSelecionado.getId());
            tarefa.setStatus("pendente");

            if (prazoPicker.getValue() != null) {
                tarefa.setPrazo(prazoPicker.getValue());
            }

            tarefaDAO.insert(tarefa);
            showAlert("Sucesso", "Tarefa criada com sucesso!");
            fecharJanela();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível criar a tarefa.");
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
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
