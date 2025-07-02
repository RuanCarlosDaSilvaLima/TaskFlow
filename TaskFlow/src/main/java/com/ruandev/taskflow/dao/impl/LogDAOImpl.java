package com.ruandev.taskflow.dao.impl;

import com.ruandev.taskflow.dao.interfaces.LogDAO;
import com.ruandev.taskflow.db.DBConnection;
import com.ruandev.taskflow.entities.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAOImpl implements LogDAO {

    private Log mapRow(ResultSet rs) throws SQLException {
        Log log = new Log();
        log.setId(rs.getInt("id_log"));
        log.setAcao(rs.getString("acao"));
        log.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        log.setIdMeta(rs.getInt("id_meta"));
        log.setIdUsuario(rs.getInt("id_usuario"));
        return log;
    }

    @Override
    public void insert(Log log) throws SQLException {
        String sql = "INSERT INTO Log (acao, data_hora, id_meta, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, log.getAcao());
            stmt.setTimestamp(2, Timestamp.valueOf(log.getDataHora()));
            stmt.setInt(3, log.getIdMeta());
            stmt.setInt(4, log.getIdUsuario());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) log.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public Log findById(int id) throws SQLException {
        String sql = "SELECT * FROM Log WHERE id_log = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<Log> findAll() throws SQLException {
        String sql = "SELECT * FROM Log";
        List<Log> logs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) logs.add(mapRow(rs));
        }
        return logs;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Log WHERE id_log = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
