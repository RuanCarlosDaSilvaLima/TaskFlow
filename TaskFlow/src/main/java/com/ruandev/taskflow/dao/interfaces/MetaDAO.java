package com.ruandev.taskflow.dao.interfaces;

import com.ruandev.taskflow.entities.Meta;

import java.sql.SQLException;
import java.util.List;

public interface MetaDAO {
    void insert(Meta meta) throws SQLException;
    Meta findById(int id) throws SQLException;
    List<Meta> findAll() throws SQLException;
    void update(Meta meta) throws SQLException;
    void delete(int id) throws SQLException;
}
