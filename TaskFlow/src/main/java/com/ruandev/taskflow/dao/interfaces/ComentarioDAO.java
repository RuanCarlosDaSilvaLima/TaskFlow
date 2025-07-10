package com.ruandev.taskflow.dao.interfaces;

import com.ruandev.taskflow.entities.Comentario;

import java.sql.SQLException;
import java.util.List;

public interface ComentarioDAO {
    void insert(Comentario comentario) throws SQLException;
    Comentario findById(int id) throws SQLException;
    List<Comentario> findAll() throws SQLException;
    void update(Comentario comentario) throws SQLException;
    void delete(int id) throws SQLException;
    int contarPorTarefa(int idTarefa) throws SQLException;

    void deleteByTarefaId(int tarefaId) throws SQLException;
}
