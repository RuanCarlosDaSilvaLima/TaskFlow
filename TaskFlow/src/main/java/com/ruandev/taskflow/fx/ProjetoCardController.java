package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.entities.Projeto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjetoCardController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private HBox statusBox;

    @FXML
    private Label quantidadeLabel;

    private Projeto projeto;

    public void setProjeto(Projeto projeto, int numeroTarefasEmAndamento) {
        this.projeto = projeto;

        nameLabel.setText(projeto.getNome());

        String status = projeto.getStatus();
        statusLabel.setText(status);

        String cor;
        switch (status.toLowerCase()) {
            case "ativo":
                cor = "#3A8DFF";
                break;
            case "concluido":
                cor = "#66BB6A";
                break;
            case "atrasado":
                cor = "#EF5350";
                break;
            case "cancelado":
                cor = "#3B3B3B";
                break;
            default:
                cor = "#3a3a3a";
        }
        statusBox.setStyle("-fx-background-color: " + cor + "; -fx-background-radius: 10; -fx-padding: 4 8;");

        quantidadeLabel.setText(String.valueOf(numeroTarefasEmAndamento));
    }

    @FXML
    private void handleOpenProjeto(MouseEvent event) {
        if (projeto == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/projeto-view.fxml"));
            Scene scene = new Scene(loader.load());

            ProjetoController controller = loader.getController();
            controller.setProjeto(projeto);

            Stage stage = new Stage();
            stage.setTitle("Projeto - " + projeto.getNome());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
