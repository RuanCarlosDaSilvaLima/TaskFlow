package com.ruandev.taskflow.dao.interfaces;

import com.ruandev.taskflow.entities.Tarefa;

import java.sql.SQLException;
import java.util.List;

public interface TarefaDAO {
    void insert(Tarefa tarefa) throws SQLException;
    Tarefa findById(int id) throws SQLException;
    List<Tarefa> findAll() throws SQLException;
    void update(Tarefa tarefa) throws SQLException;
    void delete(int id) throws SQLException;
}
