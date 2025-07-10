package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.impl.ComentarioDAOImpl;
import com.ruandev.taskflow.dao.interfaces.EquipeProjetoDAO;
import com.ruandev.taskflow.dao.interfaces.MetaDAO;
import com.ruandev.taskflow.dao.interfaces.ProjetoDAO;
import com.ruandev.taskflow.dao.interfaces.TarefaDAO;
import com.ruandev.taskflow.entities.Meta;
import com.ruandev.taskflow.entities.Projeto;
import com.ruandev.taskflow.entities.Tarefa;
import com.ruandev.taskflow.dao.interfaces.AtualizacaoListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class EditarProjetoController {

    @FXML
    private TextField nomeField;

    @FXML
    private DatePicker prazoPicker;

    @FXML
    private MenuButton statusMenuButton;

    @FXML
    private MenuItem ativoItem;

    @FXML
    private MenuItem arquivadoItem;

    @FXML
    private MenuItem concluidoItem;

    @FXML
    private MenuItem canceladoItem;

    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button deletarButton;

    private Projeto projeto;
    private String statusSelecionado;

    private AtualizacaoListener onAtualizacao;

    private final ProjetoDAO projetoDAO = DAOFactory.getProjetoDAO();

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
        if (projeto != null) {
            nomeField.setText(projeto.getNome());
            prazoPicker.setValue(projeto.getPrazo());
            statusSelecionado = projeto.getStatus();
            statusMenuButton.setText(capitalizar(statusSelecionado));
        }
    }

    public void setOnAtualizacao(AtualizacaoListener listener) {
        this.onAtualizacao = listener;
    }

    @FXML
    private void initialize() {
        ativoItem.setOnAction(e -> selecionarStatus("ativo"));
        arquivadoItem.setOnAction(e -> selecionarStatus("arquivado"));
        concluidoItem.setOnAction(e -> selecionarStatus("concluido"));
        canceladoItem.setOnAction(e -> selecionarStatus("cancelado"));

        salvarButton.setOnAction(e -> handleSalvar());
        cancelarButton.setOnAction(e -> handleCancelar());
        deletarButton.setOnAction(e -> handleDeletar());
    }

    private void selecionarStatus(String status) {
        this.statusSelecionado = status;
        statusMenuButton.setText(capitalizar(status));
    }

    private void handleSalvar() {
        if (projeto == null) {
            showAlert("Erro", "Nenhum projeto carregado para editar.");
            return;
        }

        String nome = nomeField.getText();
        LocalDate prazo = prazoPicker.getValue();

        if (nome == null || nome.isBlank()) {
            showAlert("Validação", "O nome do projeto não pode ficar vazio!");
            return;
        }

        if (statusSelecionado == null || statusSelecionado.isBlank()) {
            showAlert("Validação", "Escolha um status para o projeto!");
            return;
        }

        try {
            projeto.setNome(nome);
            projeto.setPrazo(prazo);
            projeto.setStatus(statusSelecionado);

            projetoDAO.update(projeto);

            if (onAtualizacao != null) onAtualizacao.onAtualizar();

            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível salvar as alterações.");
        }
    }

    private void handleDeletar() {
        if (projeto == null) {
            showAlert("Erro", "Nenhum projeto carregado para deletar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmação");
        confirm.setHeaderText("Deletar Projeto");
        confirm.setContentText("Tem certeza que deseja deletar este projeto? Todas as tarefas, metas, equipes e comentários associados também serão removidos.");

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    MetaDAO metaDAO = DAOFactory.getMetaDAO();
                    TarefaDAO tarefaDAO = DAOFactory.getTarefaDAO();
                    ComentarioDAOImpl comentarioDAO = new ComentarioDAOImpl();
                    EquipeProjetoDAO equipeProjetoDAO = DAOFactory.getEquipeProjetoDAO();

                    // 1. Todas as metas do projeto
                    List<Meta> metas = metaDAO.findByProjetoId(projeto.getId());
                    for (Meta meta : metas) {
                        // 1a. Todas as tarefas dessa meta
                        List<Tarefa> tarefas = tarefaDAO.findByMeta(meta.getId());
                        for (Tarefa tarefa : tarefas) {
                            // 1a.1 Deleta comentários da tarefa
                            comentarioDAO.deleteByTarefaId(tarefa.getId());
                            // 1a.2 Deleta tarefa
                            tarefaDAO.delete(tarefa.getId());
                        }
                        // 1b. Deleta meta
                        metaDAO.delete(meta.getId());
                    }

                    // 2. Deleta ligações na tabela EquipeProjeto
                    equipeProjetoDAO.deleteByProjetoId(projeto.getId());

                    // 3. Deleta o projeto
                    projetoDAO.delete(projeto.getId());

                    showAlert("Sucesso", "Projeto e todas as ligações foram deletados com sucesso!");

                    if (onAtualizacao != null) onAtualizacao.onAtualizar();

                    closeWindow();

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Erro", "Não foi possível deletar o projeto por completo.");
                }
            }
        });
    }

    private void handleCancelar() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return "";
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
}
