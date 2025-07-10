package com.ruandev.taskflow.dao.interfaces;

import com.ruandev.taskflow.entities.Projeto;

import java.sql.SQLException;
import java.util.List;

public interface ProjetoDAO {
    void insert(Projeto projeto) throws SQLException;
    Projeto findById(int id) throws SQLException;
    List<Projeto> findAll() throws SQLException;
    void update(Projeto projeto) throws SQLException;
    void delete(int id) throws SQLException;
    List<Projeto> findByUsuario(int usuarioId) throws SQLException;

}
