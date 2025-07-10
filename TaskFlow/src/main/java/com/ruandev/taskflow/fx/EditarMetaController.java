package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.dao.DAOFactory;
import com.ruandev.taskflow.dao.interfaces.EquipeProjetoDAO;
import com.ruandev.taskflow.dao.interfaces.MetaDAO;
import com.ruandev.taskflow.entities.Meta;
import com.ruandev.taskflow.entities.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class EditarMetaController {

    @FXML
    private TextField tituloField;

    @FXML
    private MenuButton responsavelMenuButton;

    @FXML
    private DatePicker prazoPicker;

    @FXML
    private Button deletarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button salvarButton;

    private final MetaDAO metaDAO = DAOFactory.getMetaDAO();
    private final EquipeProjetoDAO equipeProjetoDAO = DAOFactory.getEquipeProjetoDAO();

    private Meta meta;
    private int idProjeto;
    private Usuario responsavelSelecionado;

    public void setMeta(Meta meta) {
        this.meta = meta;
        this.idProjeto = meta.getIdProjeto();

        if (meta != null) {
            tituloField.setText(meta.getTitulo());
            prazoPicker.setValue(meta.getPrazo());
        }

        carregarMembros();
    }

    public void carregarMembros() {
        try {
            List<Usuario> membros = equipeProjetoDAO.findUsuariosByProjeto(idProjeto);

            responsavelMenuButton.getItems().clear();

            for (Usuario usuario : membros) {
                MenuItem item = new MenuItem(usuario.getNome());
                item.setOnAction(e -> {
                    responsavelSelecionado = usuario;
                    responsavelMenuButton.setText(usuario.getNome());
                });
                responsavelMenuButton.getItems().add(item);

                // Pré-seleciona se for o atual
                if (meta != null && usuario.getId().equals(meta.getIdChefe())) {
                    responsavelSelecionado = usuario;
                    responsavelMenuButton.setText(usuario.getNome());
                }
            }

            if (responsavelSelecionado == null) {
                responsavelMenuButton.setText("Selecione o responsável");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar os membros do projeto.");
        }
    }

    @FXML
    public void handleSalvar() {
        if (tituloField.getText() == null || tituloField.getText().isBlank() || responsavelSelecionado == null) {
            showAlert("Campos obrigatórios", "Preencha todos os campos antes de salvar.");
            return;
        }

        try {
            meta.setTitulo(tituloField.getText());
            meta.setIdChefe(responsavelSelecionado.getId());
            meta.setPrazo(prazoPicker.getValue());

            metaDAO.update(meta);

            showAlert("Sucesso", "Meta atualizada com sucesso!");
            fecharJanela();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível salvar as alterações.");
        }
    }

    @FXML
    public void handleDeletar() {
        try {
            metaDAO.delete(meta.getId());
            showAlert("Sucesso", "Meta deletada com sucesso!");
            fecharJanela();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível deletar a meta.");
        }
    }

    @FXML
    public void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
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
