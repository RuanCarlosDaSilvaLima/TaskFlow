package com.ruandev.taskflow.dao.interfaces;

import com.ruandev.taskflow.entities.EquipeProjeto;

import java.sql.SQLException;
import java.util.List;

public interface EquipeProjetoDAO {
    void insert(EquipeProjeto equipeProjeto) throws SQLException;
    EquipeProjeto findByIds(int idUsuario, int idProjeto) throws SQLException;
    List<EquipeProjeto> findAll() throws SQLException;
    void update(EquipeProjeto equipeProjeto) throws SQLException;
    void delete(int idUsuario, int idProjeto) throws SQLException;
}
