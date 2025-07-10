package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.impl.ComentarioDAOImpl;
import com.ruandev.taskflow.dao.impl.UsuarioDAOImpl;
import com.ruandev.taskflow.entities.Comentario;
import com.ruandev.taskflow.entities.Tarefa;
import com.ruandev.taskflow.entities.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class TarefaController {

    @FXML private Label nomeTarefaLabel;
    @FXML private Label statusLabel;
    @FXML private Label dataPrazoLabel;
    @FXML private Label descricaoLabel;
    @FXML private VBox comentariosBox;

    private Tarefa tarefa;

    @FXML
    private void initialize() {
        limparTela();
    }

    private void limparTela() {
        nomeTarefaLabel.setText("");
        statusLabel.setText("");
        dataPrazoLabel.setText("");
        descricaoLabel.setText("");
        comentariosBox.getChildren().clear();
    }

    /**
     * Carrega a tarefa e consulta os comentários no banco.
     */
    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;

        if (tarefa == null) {
            limparTela();
            return;
        }

        nomeTarefaLabel.setText(tarefa.getTitulo() != null ? tarefa.getTitulo() : "");
        statusLabel.setText(tarefa.getStatus() != null ? tarefa.getStatus() : "");
        dataPrazoLabel.setText(tarefa.getPrazo() != null ? tarefa.getPrazo().toString() : "");
        descricaoLabel.setText(tarefa.getDescricao() != null ? tarefa.getDescricao() : "");

        carregarComentariosDoBanco();
    }

    private void carregarComentariosDoBanco() {
        comentariosBox.getChildren().clear();

        if (tarefa == null || tarefa.getId() == null) return;

        try {
            ComentarioDAOImpl comentarioDAO = new ComentarioDAOImpl();
            UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

            List<Comentario> comentarios = comentarioDAO.findByTarefaId(tarefa.getId());

            if (comentarios != null && !comentarios.isEmpty()) {
                for (Comentario c : comentarios) {
                    String nomeUsuario = "Usuário";
                    if (c.getIdUsuario() != null) {
                        Usuario u = usuarioDAO.findById(c.getIdUsuario());
                        if (u != null && u.getNome() != null) nomeUsuario = u.getNome();
                    }

                    comentariosBox.getChildren().add(criarComentario(
                            nomeUsuario,
                            c.getDataHora() != null ? c.getDataHora().toString() : "",
                            c.getTexto() != null ? c.getTexto() : ""
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox criarComentario(String nomeUsuario, String data, String conteudo) {
        VBox caixa = new VBox(5);
        caixa.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");

        HBox header = new HBox(10);
        Label nomeLabel = new Label(nomeUsuario);
        nomeLabel.setStyle("-fx-font-size: 14px;");

        Label dataLabel = new Label(data);
        dataLabel.setStyle("-fx-text-fill: #555;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        header.getChildren().addAll(nomeLabel, spacer, dataLabel);

        Label conteudoLabel = new Label(conteudo);
        conteudoLabel.setWrapText(true);
        conteudoLabel.setStyle("-fx-text-fill: #333;");

        caixa.getChildren().addAll(header, conteudoLabel);

        return caixa;
    }

    @FXML
    private void abrirComentarios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("comentario-view.fxml"));
            Parent root = loader.load();

            ComentarioController controller = loader.getController();
            controller.setIdTarefa(tarefa.getId());

            Stage stage = new Stage();
            stage.setTitle("Novo Comentário");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();  // Espera o fechamento

            // Após adicionar comentário, recarrega lista
            carregarComentariosDoBanco();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fecharTarefa() {
        Stage stage = (Stage) nomeTarefaLabel.getScene().getWindow();
        stage.close();
    }
}
