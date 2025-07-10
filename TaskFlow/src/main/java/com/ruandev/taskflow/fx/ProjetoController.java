package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.MetaDAO;
import com.ruandev.taskflow.entities.Meta;
import com.ruandev.taskflow.entities.Projeto;
import com.ruandev.taskflow.dao.interfaces.AtualizacaoListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ProjetoController {

    @FXML
    private Label projectNameLabel;

    @FXML
    private Button statusButton;

    @FXML
    private HBox metasContainer;

    @FXML
    private MenuItem editarProjetoItem;

    @FXML
    private MenuItem criarMetaItem;

    @FXML
    private MenuItem editarMembrosItem;

    @FXML
    private MenuItem sairItem;

    private Projeto projeto;
    private AtualizacaoListener onAtualizacao;

    private final MetaDAO metaDAO = DAOFactory.getMetaDAO();

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
        if (projeto != null) {
            projectNameLabel.setText(projeto.getNome());
            atualizarStatusButton(projeto.getStatus());
            editarMembrosItem.setOnAction(e -> abrirEditarMembros());
            carregarMetas();
        }
    }

    public void setOnAtualizacao(AtualizacaoListener listener) {
        this.onAtualizacao = listener;
    }

    @FXML
    private void initialize() {
        editarProjetoItem.setOnAction(e -> abrirEditarProjeto());
        criarMetaItem.setOnAction(e -> abrirCriarMeta());
        sairItem.setOnAction(e -> fecharJanela());
    }

    private void atualizarStatusButton(String status) {
        String cor;
        String texto;

        switch (status.toLowerCase()) {
            case "ativo":
                texto = "Ativo";
                cor = "#3A8DFF";
                break;
            case "concluido":
                texto = "Concluído";
                cor = "#66BB6A";
                break;
            case "atrasado":
                texto = "Atrasado";
                cor = "#EF5350";
                break;
            case "cancelado":
                texto = "Cancelado";
                cor = "#3B3B3B";
                break;
            default:
                texto = "Desconhecido";
                cor = "#AAAAAA";
        }

        statusButton.setText(texto);
        statusButton.setStyle("-fx-background-color: " + cor + "; -fx-background-radius: 15;");
    }

    private void carregarMetas() {
        try {
            metasContainer.getChildren().clear();
            List<Meta> metas = metaDAO.findByProjeto(projeto.getId());

            for (Meta meta : metas) {
                FXMLLoader metaLoader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/meta-view.fxml"));
                Parent metaBox = metaLoader.load();

                MetaController metaController = metaLoader.getController();
                metaController.setMeta(meta);

                metasContainer.getChildren().add(metaBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar as metas.");
        }
    }

    private void abrirEditarProjeto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/editar-projeto-view.fxml"));
            Scene scene = new Scene(loader.load());
            EditarProjetoController controller = loader.getController();
            controller.setProjeto(projeto);

            // passa o callback
            controller.setOnAtualizacao(() -> {
                if (onAtualizacao != null) onAtualizacao.onAtualizar();
            });

            Stage stage = new Stage();
            stage.setTitle("Editar Projeto");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarMetas();
            atualizarStatusButton(projeto.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de edição de projeto.");
        }
    }

    private void abrirCriarMeta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/cria-meta-view.fxml"));
            Scene scene = new Scene(loader.load());
            CriaMetaController controller = loader.getController();

            controller.setIdProjeto(projeto.getId());
            controller.carregarMembros();

            // passa o callback
            controller.setOnAtualizacao(() -> {
                if (onAtualizacao != null) onAtualizacao.onAtualizar();
            });

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Criar Meta");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarMetas();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de criação de meta.");
        }
    }

    private void abrirEditarMembros() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/editar-membros-view.fxml"));
            Scene scene = new Scene(loader.load());
            EditarMembrosController controller = loader.getController();
            controller.setProjeto(projeto);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Editar Membros");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarMetas();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de edição de membros.");
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) projectNameLabel.getScene().getWindow();
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
