package com.ruandev.taskflow.dao.impl;

import com.ruandev.taskflow.dao.interfaces.EquipeProjetoDAO;
import com.ruandev.taskflow.db.DBConnection;
import com.ruandev.taskflow.entities.EquipeProjeto;
import com.ruandev.taskflow.entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeProjetoDAOImpl implements EquipeProjetoDAO {

    private EquipeProjeto mapRow(ResultSet rs) throws SQLException {
        EquipeProjeto ep = new EquipeProjeto();
        ep.setIdUsuario(rs.getInt("id_usuario"));
        ep.setIdProjeto(rs.getInt("id_projeto"));
        ep.setPapel(rs.getString("papel"));
        ep.setDataEntrada(rs.getDate("data_entrada").toLocalDate());
        return ep;
    }

    @Override
    public void insert(EquipeProjeto ep) throws SQLException {
        String sql = "INSERT INTO EquipeProjeto (id_usuario, id_projeto, papel, data_entrada) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ep.getIdUsuario());
            stmt.setInt(2, ep.getIdProjeto());
            stmt.setString(3, ep.getPapel());
            stmt.setDate(4, Date.valueOf(ep.getDataEntrada()));
            stmt.executeUpdate();
        }
    }

    @Override
    public EquipeProjeto findByIds(int idUsuario, int idProjeto) throws SQLException {
        String sql = "SELECT * FROM EquipeProjeto WHERE id_usuario = ? AND id_projeto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idProjeto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<EquipeProjeto> findAll() throws SQLException {
        String sql = "SELECT * FROM EquipeProjeto";
        List<EquipeProjeto> equipes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) equipes.add(mapRow(rs));
        }
        return equipes;
    }

    @Override
    public void update(EquipeProjeto ep) throws SQLException {
        String sql = "UPDATE EquipeProjeto SET papel = ?, data_entrada = ? WHERE id_usuario = ? AND id_projeto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ep.getPapel());
            stmt.setDate(2, Date.valueOf(ep.getDataEntrada()));
            stmt.setInt(3, ep.getIdUsuario());
            stmt.setInt(4, ep.getIdProjeto());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int idUsuario, int idProjeto) throws SQLException {
        String sql = "DELETE FROM EquipeProjeto WHERE id_usuario = ? AND id_projeto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idProjeto);
            stmt.executeUpdate();
        }
    }

    // -------------------------------
    // NOVOS MÉTODOS PARA EDITAR MEMBROS
    // -------------------------------

    @Override
    public void adicionarMembro(int projetoId, int usuarioId) throws SQLException {
        String sql = "INSERT INTO EquipeProjeto (id_usuario, id_projeto, papel, data_entrada) VALUES (?, ?, 'membro', CURRENT_DATE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, projetoId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void removerMembro(int projetoId, int usuarioId) throws SQLException {
        String sql = "DELETE FROM EquipeProjeto WHERE id_usuario = ? AND id_projeto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, projetoId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Usuario> findUsuariosByProjeto(int projetoId) throws SQLException {
        String sql = """
            SELECT u.* FROM Usuario u
            JOIN EquipeProjeto ep ON u.id_usuario = ep.id_usuario
            WHERE ep.id_projeto = ?
        """;

        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projetoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    usuarios.add(u);
                }
            }
        }

        return usuarios;
    }

    @Override
    public void deleteByProjetoId(int projetoId) throws SQLException {
        String sql = "DELETE FROM EquipeProjeto WHERE id_projeto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projetoId);
            stmt.executeUpdate();
        }
    }

}
