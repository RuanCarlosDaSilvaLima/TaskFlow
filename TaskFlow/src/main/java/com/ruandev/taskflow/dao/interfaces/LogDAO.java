package com.ruandev.taskflow.dao.interfaces;

import com.ruandev.taskflow.entities.Log;

import java.sql.SQLException;
import java.util.List;

public interface LogDAO {
    void insert(Log log) throws SQLException;
    Log findById(int id) throws SQLException;
    List<Log> findAll() throws SQLException;
    void delete(int id) throws SQLException;
}
