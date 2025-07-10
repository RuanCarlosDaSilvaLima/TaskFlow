package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.impl.TarefaDAOImpl;
import com.ruandev.taskflow.dao.impl.ComentarioDAOImpl;
import com.ruandev.taskflow.entities.Comentario;
import com.ruandev.taskflow.entities.Tarefa;
import com.ruandev.taskflow.fx.Sessao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class ComentarioController {

    @FXML private Label tituloLabel;
    @FXML private TextArea comentarioTextArea;
    @FXML private Button cancelarButton;
    @FXML private Button enviarButton;

    private int idTarefa;

    @FXML
    private void initialize() {
        cancelarButton.setOnAction(e -> fechar());
        enviarButton.setOnAction(e -> enviarComentario());
    }

    /**
     * Recebe o ID da tarefa e busca no banco os dados dela
     * para exibir o título na UI.
     */
    public void setIdTarefa(int id) {
        this.idTarefa = id;

        try {
            TarefaDAOImpl tarefaDAO = new TarefaDAOImpl();
            Tarefa tarefa = tarefaDAO.findById(id);

            if (tarefa != null && tarefa.getTitulo() != null && !tarefa.getTitulo().isBlank()) {
                tituloLabel.setText(tarefa.getTitulo());
            } else {
                tituloLabel.setText("Tarefa");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tituloLabel.setText("Tarefa");
        }
    }

    /**
     * Ação do botão enviar: salva o comentário no banco.
     */
    private void enviarComentario() {
        String conteudo = comentarioTextArea.getText();

        if (conteudo == null || conteudo.isBlank()) {
            mostrarAlerta("Aviso", "Digite um comentário antes de enviar!");
            return;
        }

        try {
            if (Sessao.getUsuario() == null) {
                mostrarAlerta("Erro", "Usuário não está logado!");
                return;
            }

            Comentario comentario = new Comentario();
            comentario.setTexto(conteudo.trim());
            comentario.setDataHora(LocalDateTime.now());
            comentario.setIdUsuario(Sessao.getUsuario().getId());
            comentario.setIdTarefa(idTarefa);

            ComentarioDAOImpl comentarioDAO = new ComentarioDAOImpl();
            comentarioDAO.insert(comentario);

            mostrarAlerta("Sucesso", "Comentário salvo com sucesso!");
            fechar();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Falha ao salvar o comentário no banco.");
        }
    }

    private void fechar() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
