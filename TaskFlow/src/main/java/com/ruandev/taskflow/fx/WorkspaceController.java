package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.ProjetoDAO;
import com.ruandev.taskflow.dao.interfaces.TarefaDAO;
import com.ruandev.taskflow.entities.Projeto;
import com.ruandev.taskflow.entities.Usuario;
import com.ruandev.taskflow.dao.interfaces.AtualizacaoListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class WorkspaceController {

    @FXML
    private MenuButton flowspaceMenuButton;

    @FXML
    private TextField searchField;

    @FXML
    private FlowPane projectCardsPane;

    @FXML
    private Button addButton;

    @FXML
    private MenuItem configuracoesItem;

    @FXML
    private MenuItem sairItem;

    private final ProjetoDAO projetoDAO = DAOFactory.getProjetoDAO();
    private final TarefaDAO tarefaDAO = DAOFactory.getTarefaDAO();

    // Usado para ser passado para outras telas que precisem avisar mudanças
    private final AtualizacaoListener atualizacaoListener = this::atualizarWorkspace;

    @FXML
    private void initialize() {
        Usuario usuario = Sessao.getUsuario();
        if (usuario == null) {
            System.out.println("Nenhum usuário logado! Retornando para login.");
            voltarParaLogin();
            return;
        }

        atualizarWorkspace();

        sairItem.setOnAction(e -> handleSair());
        configuracoesItem.setOnAction(e -> abrirTelaConfiguracoes());
        addButton.setOnAction(e -> abrirTelaCriarProjeto());
    }

    /**
     * Atualiza todo o workspace com dados mais recentes.
     */
    private void atualizarWorkspace() {
        Usuario usuario = Sessao.getUsuario();
        if (usuario == null) return;
        carregarProjetosDoUsuario(usuario);
    }

    private void carregarProjetosDoUsuario(Usuario usuario) {
        try {
            List<Projeto> projetos = projetoDAO.findByUsuario(usuario.getId());
            flowspaceMenuButton.getItems().clear();
            projectCardsPane.getChildren().clear();

            for (Projeto projeto : projetos) {
                int tarefasEmAndamento = tarefaDAO.contarEmAndamento(projeto.getId());

                // FlowSpace MenuButton
                MenuItem item = new MenuItem(projeto.getNome());
                item.setOnAction(e -> abrirProjeto(projeto));
                flowspaceMenuButton.getItems().add(item);

                // Card no FlowPane
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/projeto-card-view.fxml"));
                Pane card = loader.load();
                ProjetoCardController cardController = loader.getController();
                cardController.setProjeto(projeto, tarefasEmAndamento);
                projectCardsPane.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar os projetos.");
        }
    }

    private void abrirProjeto(Projeto projeto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/projeto-view.fxml"));
            Parent root = loader.load();

            ProjetoController controller = loader.getController();
            controller.setProjeto(projeto);
            controller.setOnAtualizacao(atualizacaoListener);

            Stage stage = new Stage();
            stage.setTitle("Projeto: " + projeto.getNome());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            atualizarWorkspace();  // reforça atualização ao fechar
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir o projeto.");
        }
    }

    private void abrirTelaCriarProjeto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/cria-projeto-view.fxml"));
            Parent root = loader.load();

            CriaProjetoController controller = loader.getController();
            controller.setOnAtualizacao(atualizacaoListener);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Criar Novo Projeto");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            atualizarWorkspace();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de criação de projeto.");
        }
    }

    private void abrirTelaConfiguracoes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/configuracoes-perfil-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Configurações do Perfil");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            atualizarWorkspace();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível abrir a tela de configurações do usuário.");
        }
    }

    private void handleSair() {
        Sessao.encerrar();
        voltarParaLogin();
    }

    private void voltarParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ruandev/taskflow/fx/login-view.fxml"));
            Stage stage = (Stage) projectCardsPane.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Taskflow - Login");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível voltar para a tela de login.");
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
