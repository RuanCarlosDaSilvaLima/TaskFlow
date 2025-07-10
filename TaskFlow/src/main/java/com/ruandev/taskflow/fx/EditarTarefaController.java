package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.EquipeProjetoDAO;
import com.ruandev.taskflow.dao.interfaces.TarefaDAO;
import com.ruandev.taskflow.dao.interfaces.UsuarioDAO;
import com.ruandev.taskflow.entities.Tarefa;
import com.ruandev.taskflow.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class EditarTarefaController {

    @FXML
    private MenuButton tarefaSelecionadaMenu;

    @FXML
    private TextField tituloField;

    @FXML
    private MenuButton responsavelMenu;

    @FXML
    private DatePicker prazoPicker;

    @FXML
    private TextArea descricaoArea;

    @FXML
    private Button deletarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button salvarButton;

    private final UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
    private final EquipeProjetoDAO equipeProjetoDAO = DAOFactory.getEquipeProjetoDAO();
    private final TarefaDAO tarefaDAO = DAOFactory.getTarefaDAO();

    private Tarefa tarefaSelecionada;
    private int idProjeto;
    private int idMeta;
    private Usuario responsavelSelecionado;

    // ===================================================
    // PARA SER CHAMADO NA ABERTURA
    // ===================================================
    public void configurar(int idProjeto, int idMeta) {
        this.idProjeto = idProjeto;
        this.idMeta = idMeta;
        carregarTarefasDaMeta();
        carregarMembros();
    }

    @FXML
    private void initialize() {
        cancelarButton.setOnAction(e -> fecharJanela());
        salvarButton.setOnAction(e -> handleSalvar());
        deletarButton.setOnAction(e -> handleDeletar());
    }

    // ===================================================
    // Carregar Tarefas dessa Meta
    // ===================================================
    private void carregarTarefasDaMeta() {
        try {
            tarefaSelecionadaMenu.getItems().clear();

            List<Tarefa> tarefas = tarefaDAO.findByMeta(idMeta);
            for (Tarefa t : tarefas) {
                MenuItem item = new MenuItem(t.getTitulo());
                item.setOnAction(e -> {
                    tarefaSelecionada = t;
                    tarefaSelecionadaMenu.setText(t.getTitulo());
                    preencherCamposComTarefa(t);
                });
                tarefaSelecionadaMenu.getItems().add(item);
            }

            tarefaSelecionadaMenu.setText("Selecione a tarefa");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar as tarefas da meta.");
        }
    }

    // ===================================================
    // Preencher Campos ao Selecionar Tarefa
    // ===================================================
    private void preencherCamposComTarefa(Tarefa tarefa) {
        try {
            tituloField.setText(tarefa.getTitulo());
            descricaoArea.setText(tarefa.getDescricao());
            prazoPicker.setValue(tarefa.getPrazo());

            if (tarefa.getIdResponsavel() != null) {
                Usuario responsavel = usuarioDAO.findById(tarefa.getIdResponsavel());
                if (responsavel != null) {
                    responsavelSelecionado = responsavel;
                    responsavelMenu.setText(responsavel.getNome());
                } else {
                    responsavelMenu.setText("Selecionar responsável");
                }
            } else {
                responsavelSelecionado = null;
                responsavelMenu.setText("Selecionar responsável");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar os dados da tarefa.");
        }
    }

    // ===================================================
    // Carregar Membros do Projeto
    // ===================================================
    public void carregarMembros() {
        try {
            responsavelMenu.getItems().clear();
            List<Usuario> membros = equipeProjetoDAO.findUsuariosByProjeto(idProjeto);

            for (Usuario membro : membros) {
                MenuItem item = new MenuItem(membro.getNome());
                item.setOnAction(e -> {
                    responsavelSelecionado = membro;
                    responsavelMenu.setText(membro.getNome());
                });
                responsavelMenu.getItems().add(item);
            }

            responsavelMenu.setText("Selecionar responsável");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar os membros do projeto.");
        }
    }

    // ===================================================
    // Salvar Alterações
    // ===================================================
    private void handleSalvar() {
        if (tarefaSelecionada == null) {
            showAlert("Validação", "Selecione uma tarefa para editar!");
            return;
        }

        String titulo = tituloField.getText();
        String descricao = descricaoArea.getText();
        LocalDate prazo = prazoPicker.getValue();

        if (titulo == null || titulo.isBlank()) {
            showAlert("Validação", "O nome da tarefa não pode ficar vazio!");
            return;
        }

        try {
            tarefaSelecionada.setTitulo(titulo);
            tarefaSelecionada.setDescricao(descricao);
            tarefaSelecionada.setPrazo(prazo);

            if (responsavelSelecionado != null) {
                tarefaSelecionada.setIdResponsavel(responsavelSelecionado.getId());
            } else {
                tarefaSelecionada.setIdResponsavel(null);
            }

            tarefaDAO.update(tarefaSelecionada);
            fecharJanela();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível salvar as alterações.");
        }
    }

    // ===================================================
    // Deletar Tarefa
    // ===================================================
    private void handleDeletar() {
        if (tarefaSelecionada == null) {
            showAlert("Erro", "Selecione uma tarefa para deletar!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmação");
        confirm.setHeaderText("Deletar Tarefa");
        confirm.setContentText("Tem certeza que deseja deletar esta tarefa?");

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    tarefaDAO.delete(tarefaSelecionada.getId());
                    fecharJanela();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Erro", "Não foi possível deletar a tarefa.");
                }
            }
        });
    }

    // ===================================================
    // Fechar Janela
    // ===================================================
    private void fecharJanela() {
        Stage stage = (Stage) tituloField.getScene().getWindow();
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
