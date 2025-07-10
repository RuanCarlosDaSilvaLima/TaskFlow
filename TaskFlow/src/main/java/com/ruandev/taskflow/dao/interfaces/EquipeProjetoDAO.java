package com.ruandev.taskflow.dao.interfaces;

import com.ruandev.taskflow.entities.EquipeProjeto;
import com.ruandev.taskflow.entities.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface EquipeProjetoDAO {
    void insert(EquipeProjeto ep) throws SQLException;
    EquipeProjeto findByIds(int idUsuario, int idProjeto) throws SQLException;
    List<EquipeProjeto> findAll() throws SQLException;
    void update(EquipeProjeto ep) throws SQLException;
    void delete(int idUsuario, int idProjeto) throws SQLException;

    // NOVOS MÉTODOS
    void adicionarMembro(int projetoId, int usuarioId) throws SQLException;
    void removerMembro(int projetoId, int usuarioId) throws SQLException;
    List<Usuario> findUsuariosByProjeto(int projetoId) throws SQLException;

    void deleteByProjetoId(int projetoId) throws SQLException;
}
