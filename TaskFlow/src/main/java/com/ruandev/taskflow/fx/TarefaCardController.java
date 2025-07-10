package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.impl.ComentarioDAOImpl;
import com.ruandev.taskflow.entities.Tarefa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TarefaCardController {

    @FXML private Label nomeLabel;
    @FXML private Label statusTextLabel;
    @FXML private Label comentariosLabel;
    @FXML private AnchorPane cardRoot;

    private Tarefa tarefa;

    /**
     * Inicializa os dados do card e busca a contagem de comentários no banco.
     */
    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;

        if (tarefa != null) {
            nomeLabel.setText(tarefa.getTitulo() != null ? tarefa.getTitulo() : "");
            statusTextLabel.setText(tarefa.getStatus() != null ? tarefa.getStatus() : "");

            atualizarContagemComentarios();
        } else {
            limparCampos();
        }
    }

    private void limparCampos() {
        nomeLabel.setText("");
        statusTextLabel.setText("");
        comentariosLabel.setText("0");
    }

    private void atualizarContagemComentarios() {
        try {
            int quantidadeComentarios = 0;
            if (tarefa.getId() != null) {
                ComentarioDAOImpl comentarioDAO = new ComentarioDAOImpl();
                quantidadeComentarios = comentarioDAO.contarPorTarefa(tarefa.getId());
            }
            comentariosLabel.setText(String.valueOf(quantidadeComentarios));
        } catch (Exception e) {
            System.err.println("Erro ao contar comentários: " + e.getMessage());
            comentariosLabel.setText("0");
        }
    }

    /**
     * Abre a tela de detalhes da tarefa.
     */
    @FXML
    private void abrirDetalhesTarefa(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tarefa-view.fxml"));
            Parent root = loader.load();

            TarefaController controller = loader.getController();
            controller.setTarefa(tarefa);

            Stage stage = new Stage();
            stage.setTitle("Detalhes da Tarefa");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();  // espera a janela fechar antes de continuar

            // Após fechar, recarrega a contagem de comentários atualizada
            atualizarContagemComentarios();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
