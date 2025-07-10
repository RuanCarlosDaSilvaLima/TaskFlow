package com.ruandev.taskflow.dao.impl;

import com.ruandev.taskflow.dao.interfaces.ComentarioDAO;
import com.ruandev.taskflow.db.DBConnection;
import com.ruandev.taskflow.entities.Comentario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAOImpl implements ComentarioDAO {

    private Comentario mapRow(ResultSet rs) throws SQLException {
        Comentario comentario = new Comentario();
        comentario.setId(rs.getInt("id_comentario"));
        comentario.setTexto(rs.getString("texto"));
        comentario.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        comentario.setIdUsuario(rs.getInt("id_usuario"));
        comentario.setIdTarefa(rs.getInt("id_tarefa"));
        return comentario;
    }

    @Override
    public void insert(Comentario comentario) throws SQLException {
        String sql = "INSERT INTO Comentario (texto, data_hora, id_usuario, id_tarefa) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, comentario.getTexto());
            stmt.setTimestamp(2, Timestamp.valueOf(comentario.getDataHora()));
            stmt.setInt(3, comentario.getIdUsuario());
            stmt.setInt(4, comentario.getIdTarefa());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) comentario.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public Comentario findById(int id) throws SQLException {
        String sql = "SELECT * FROM Comentario WHERE id_comentario = ?";
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
    public List<Comentario> findAll() throws SQLException {
        String sql = "SELECT * FROM Comentario";
        List<Comentario> comentarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) comentarios.add(mapRow(rs));
        }
        return comentarios;
    }

    @Override
    public void update(Comentario comentario) throws SQLException {
        String sql = "UPDATE Comentario SET texto = ?, data_hora = ?, id_usuario = ?, id_tarefa = ? WHERE id_comentario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, comentario.getTexto());
            stmt.setTimestamp(2, Timestamp.valueOf(comentario.getDataHora()));
            stmt.setInt(3, comentario.getIdUsuario());
            stmt.setInt(4, comentario.getIdTarefa());
            stmt.setInt(5, comentario.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Comentario WHERE id_comentario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public int contarPorTarefa(int idTarefa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Comentario WHERE id_tarefa = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTarefa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * ✅ Novo método para buscar comentários por tarefa
     */
    public List<Comentario> findByTarefaId(int tarefaId) throws SQLException {
        String sql = "SELECT * FROM Comentario WHERE id_tarefa = ?";
        List<Comentario> comentarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tarefaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comentarios.add(mapRow(rs));
                }
            }
        }
        return comentarios;
    }

    @Override
    public void deleteByTarefaId(int tarefaId) throws SQLException {
        String sql = "DELETE FROM Comentario WHERE id_tarefa = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tarefaId);
            stmt.executeUpdate();
        }
    }

}
