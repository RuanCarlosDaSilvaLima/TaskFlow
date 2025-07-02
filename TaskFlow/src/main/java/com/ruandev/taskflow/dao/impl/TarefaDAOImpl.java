package com.ruandev.taskflow.dao.impl;

import com.ruandev.taskflow.dao.interfaces.TarefaDAO;
import com.ruandev.taskflow.db.DBConnection;
import com.ruandev.taskflow.entities.Tarefa;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAOImpl implements TarefaDAO {

    private Tarefa mapRow(ResultSet rs) throws SQLException {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(rs.getInt("id_tarefa"));
        tarefa.setTitulo(rs.getString("titulo"));
        tarefa.setStatus(rs.getString("status"));
        tarefa.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        Date prazo = rs.getDate("prazo");
        if (prazo != null) tarefa.setPrazo(prazo.toLocalDate());
        tarefa.setIdResponsavel(rs.getInt("id_responsavel"));
        tarefa.setIdMeta(rs.getInt("id_meta"));
        return tarefa;
    }

    @Override
    public void insert(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO Tarefa (titulo, status, data_criacao, prazo, id_responsavel, id_meta) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getStatus());
            stmt.setTimestamp(3, Timestamp.valueOf(tarefa.getDataCriacao()));
            if (tarefa.getPrazo() != null) stmt.setDate(4, Date.valueOf(tarefa.getPrazo()));
            else stmt.setNull(4, Types.DATE);
            if (tarefa.getIdResponsavel() != null) stmt.setInt(5, tarefa.getIdResponsavel());
            else stmt.setNull(5, Types.INTEGER);
            stmt.setInt(6, tarefa.getIdMeta());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) tarefa.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public Tarefa findById(int id) throws SQLException {
        String sql = "SELECT * FROM Tarefa WHERE id_tarefa = ?";
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
    public List<Tarefa> findAll() throws SQLException {
        String sql = "SELECT * FROM Tarefa";
        List<Tarefa> tarefas = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) tarefas.add(mapRow(rs));
        }
        return tarefas;
    }

    @Override
    public void update(Tarefa tarefa) throws SQLException {
        String sql = "UPDATE Tarefa SET titulo = ?, status = ?, data_criacao = ?, prazo = ?, id_responsavel = ?, id_meta = ? WHERE id_tarefa = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getStatus());
            stmt.setTimestamp(3, Timestamp.valueOf(tarefa.getDataCriacao()));
            if (tarefa.getPrazo() != null) stmt.setDate(4, Date.valueOf(tarefa.getPrazo()));
            else stmt.setNull(4, Types.DATE);
            if (tarefa.getIdResponsavel() != null) stmt.setInt(5, tarefa.getIdResponsavel());
            else stmt.setNull(5, Types.INTEGER);
            stmt.setInt(6, tarefa.getIdMeta());
            stmt.setInt(7, tarefa.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Tarefa WHERE id_tarefa = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
