package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.TarefaDAO;
import com.ruandev.taskflow.entities.Meta;
import com.ruandev.taskflow.entities.Tarefa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class MetaController {

    @FXML
    private Label tituloLabel;

    @FXML
    private Label prazoLabel;

    @FXML
    private VBox tarefasContainer;

    @FXML
    private MenuItem editarMetaItem;

    @FXML
    private MenuItem criarTarefaItem;

    @FXML
    private MenuItem editarTarefaItem;

    private final TarefaDAO tarefaDAO = DAOFactory.getTarefaDAO();

    private Meta meta;

    public void setMeta(Meta meta) {
        this.meta = meta;

        if (meta != null) {
            tituloLabel.setText(meta.getTitulo());
            if (meta.getPrazo() != null) {
                prazoLabel.setText("Prazo: " + meta.getPrazo().toString());
            } else {
                prazoLabel.setText("Prazo: -");
            }
            carregarTarefas();
        }
    }

    @FXML
    private void initialize() {
        editarMetaItem.setOnAction(e -> abrirEditarMeta());
        criarTarefaItem.setOnAction(e -> abrirCriarTarefa());
        editarTarefaItem.setOnAction(e -> abrirEditarTarefa());
    }

    private void carregarTarefas() {
        try {
            tarefasContainer.getChildren().clear();

            List<Tarefa> tarefas = tarefaDAO.findByMeta(meta.getId());
            for (Tarefa tarefa : tarefas) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/tarefa-card-view.fxml"));
                AnchorPane card = loader.load();

                TarefaCardController controller = loader.getController();
                controller.setTarefa(tarefa);

                tarefasContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirEditarTarefa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/editar-tarefa-view.fxml"));
            Scene scene = new Scene(loader.load());

            EditarTarefaController controller = loader.getController();
            controller.configurar(meta.getIdProjeto(), meta.getId());

            Stage stage = new Stage();
            stage.setTitle("Editar Tarefas da Meta");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarTarefas();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de edição de tarefa.");
        }
    }

    private void abrirEditarMeta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/editar-meta-view.fxml"));
            Scene scene = new Scene(loader.load());

            EditarMetaController controller = loader.getController();
            controller.setMeta(meta);
            controller.carregarMembros();

            Stage stage = new Stage();
            stage.setTitle("Editar Meta");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            tituloLabel.setText(meta.getTitulo());
            if (meta.getPrazo() != null) {
                prazoLabel.setText("Prazo: " + meta.getPrazo().toString());
            } else {
                prazoLabel.setText("Prazo: -");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de edição de meta.");
        }
    }

    private void abrirCriarTarefa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/criar-tarefa-view.fxml"));
            Scene scene = new Scene(loader.load());

            CriaTarefaController controller = loader.getController();
            controller.setIdMeta(meta.getId());
            controller.setIdProjeto(meta.getIdProjeto());
            controller.carregarMembros();

            Stage stage = new Stage();
            stage.setTitle("Criar Tarefa");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarTarefas();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de criação de tarefa.");
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
