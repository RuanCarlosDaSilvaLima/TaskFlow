package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.EquipeProjetoDAO;
import com.ruandev.taskflow.dao.interfaces.ProjetoDAO;
import com.ruandev.taskflow.entities.EquipeProjeto;
import com.ruandev.taskflow.entities.Projeto;
import com.ruandev.taskflow.dao.interfaces.AtualizacaoListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CriaProjetoController {

    @FXML private TextField nomeField;
    @FXML private DatePicker prazoDatePicker;
    @FXML private Button cancelarButton;
    @FXML private Button criarButton;

    private final ProjetoDAO projetoDAO = DAOFactory.getProjetoDAO();
    private final EquipeProjetoDAO equipeProjetoDAO = DAOFactory.getEquipeProjetoDAO();

    private AtualizacaoListener onAtualizacao;

    public void setOnAtualizacao(AtualizacaoListener listener) {
        this.onAtualizacao = listener;
    }

    @FXML
    private void initialize() {
        cancelarButton.setOnAction(e -> fecharJanela());
        criarButton.setOnAction(e -> handleCriarProjeto());
    }

    private void handleCriarProjeto() {
        String nome = nomeField.getText();
        LocalDate prazo = prazoDatePicker.getValue();

        if (nome == null || nome.isBlank()) {
            showAlert("Validação", "O nome do projeto não pode estar vazio.");
            return;
        }

        if (prazo == null) {
            showAlert("Validação", "Você deve escolher uma data válida para o prazo!");
            return;
        }

        if (!Sessao.isLogado()) {
            showAlert("Erro", "Nenhum usuário logado. Faça login novamente!");
            return;
        }

        try {
            // Cria e salva o projeto
            Projeto projeto = new Projeto();
            projeto.setNome(nome.trim());
            projeto.setPrazo(prazo);
            projeto.setDataInicio(LocalDate.now());
            projeto.setStatus("ativo");

            projetoDAO.insert(projeto);

            // Associa o usuário logado como líder
            EquipeProjeto equipe = new EquipeProjeto();
            equipe.setIdUsuario(Sessao.getUsuario().getId());
            equipe.setIdProjeto(projeto.getId());
            equipe.setPapel("lider");
            equipe.setDataEntrada(LocalDate.now());

            equipeProjetoDAO.insert(equipe);

            showAlert("Sucesso", "Projeto criado com sucesso!");

            // Notifica quem abriu essa janela
            if (onAtualizacao != null) onAtualizacao.onAtualizar();

            fecharJanela();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Erro ao criar o projeto: " + e.getMessage());
        }
    }

    private void fecharJanela() {
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
}
