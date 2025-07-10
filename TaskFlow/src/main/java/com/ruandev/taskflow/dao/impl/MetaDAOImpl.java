package com.ruandev.taskflow.dao.impl;

import com.ruandev.taskflow.dao.interfaces.MetaDAO;
import com.ruandev.taskflow.db.DBConnection;
import com.ruandev.taskflow.entities.Meta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaDAOImpl implements MetaDAO {

    private Meta mapRow(ResultSet rs) throws SQLException {
        Meta meta = new Meta();
        meta.setId(rs.getInt("id_meta"));
        meta.setTitulo(rs.getString("titulo"));  // usa o nome certo do banco
        meta.setDataInicio(rs.getTimestamp("data_criacao").toLocalDateTime().toLocalDate());

        Date prazo = rs.getDate("prazo");
        if (prazo != null) {
            meta.setPrazo(prazo.toLocalDate());
        }

        meta.setIdChefe(rs.getInt("id_chefe"));
        meta.setIdProjeto(rs.getInt("id_projeto"));
        return meta;
    }

    @Override
    public void insert(Meta meta) throws SQLException {
        String sql = "INSERT INTO Meta (titulo, id_chefe, id_projeto, prazo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getTitulo());
            stmt.setInt(2, meta.getIdChefe());
            stmt.setInt(3, meta.getIdProjeto());

            if (meta.getPrazo() != null) {
                stmt.setDate(4, Date.valueOf(meta.getPrazo()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public Meta findById(int id) throws SQLException {
        String sql = "SELECT * FROM Meta WHERE id_meta = ?";
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
    public List<Meta> findAll() throws SQLException {
        String sql = "SELECT * FROM Meta";
        List<Meta> metas = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                metas.add(mapRow(rs));
            }
        }
        return metas;
    }

    @Override
    public void update(Meta meta) throws SQLException {
        String sql = "UPDATE Meta SET titulo = ?, prazo = ?, id_chefe = ?, id_projeto = ? WHERE id_meta = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getTitulo());
            if (meta.getPrazo() != null) {
                stmt.setDate(2, Date.valueOf(meta.getPrazo()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setInt(3, meta.getIdChefe());
            stmt.setInt(4, meta.getIdProjeto());
            stmt.setInt(5, meta.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Meta WHERE id_meta = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Meta> findByProjeto(int projetoId) throws SQLException {
        String sql = "SELECT * FROM Meta WHERE id_projeto = ?";
        List<Meta> metas = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projetoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    metas.add(mapRow(rs));
                }
            }
        }
        return metas;
    }

    @Override
    public List<Meta> findByProjetoId(int projetoId) throws SQLException {
        String sql = "SELECT * FROM Meta WHERE id_projeto = ?";
        List<Meta> metas = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projetoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    metas.add(mapRow(rs));
                }
            }
        }
        return metas;
    }

}
