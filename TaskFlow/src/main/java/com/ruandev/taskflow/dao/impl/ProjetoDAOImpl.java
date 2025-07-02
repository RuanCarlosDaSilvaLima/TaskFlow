package com.ruandev.taskflow.dao.impl;

import com.ruandev.taskflow.dao.interfaces.ProjetoDAO;
import com.ruandev.taskflow.db.DBConnection;
import com.ruandev.taskflow.entities.Projeto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAOImpl implements ProjetoDAO {

    private Projeto mapRow(ResultSet rs) throws SQLException {
        Projeto projeto = new Projeto();
        projeto.setId(rs.getInt("id_projeto"));
        projeto.setNome(rs.getString("nome"));
        projeto.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        Date prazo = rs.getDate("prazo");
        if (prazo != null) {
            projeto.setPrazo(prazo.toLocalDate());
        }
        projeto.setStatus(rs.getString("status_projeto"));
        return projeto;
    }

    @Override
    public void insert(Projeto projeto) throws SQLException {
        String sql = "INSERT INTO Projeto (nome, data_inicio, prazo, status_projeto) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, projeto.getNome());
            stmt.setDate(2, Date.valueOf(projeto.getDataInicio()));
            if (projeto.getPrazo() != null) {
                stmt.setDate(3, Date.valueOf(projeto.getPrazo()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, projeto.getStatus());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    projeto.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Projeto findById(int id) throws SQLException {
        String sql = "SELECT * FROM Projeto WHERE id_projeto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Projeto> findAll() throws SQLException {
        String sql = "SELECT * FROM Projeto";
        List<Projeto> projetos = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                projetos.add(mapRow(rs));
            }
        }
        return projetos;
    }

    @Override
    public void update(Projeto projeto) throws SQLException {
        String sql = "UPDATE Projeto SET nome = ?, data_inicio = ?, prazo = ?, status_projeto = ? WHERE id_projeto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setDate(2, Date.valueOf(projeto.getDataInicio()));
            if (projeto.getPrazo() != null) {
                stmt.setDate(3, Date.valueOf(projeto.getPrazo()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, projeto.getStatus());
            stmt.setInt(5, projeto.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Projeto WHERE id_projeto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
