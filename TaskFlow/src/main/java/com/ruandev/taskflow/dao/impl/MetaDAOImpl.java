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
        meta.setNome(rs.getString("nome"));
        meta.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        Date prazo = rs.getDate("prazo");
        if (prazo != null) meta.setPrazo(prazo.toLocalDate());
        meta.setIdChefe(rs.getInt("id_chefe"));
        meta.setIdProjeto(rs.getInt("id_projeto"));
        return meta;
    }

    @Override
    public void insert(Meta meta) throws SQLException {
        String sql = "INSERT INTO Meta (nome, data_inicio, prazo, id_chefe, id_projeto) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, meta.getNome());
            stmt.setDate(2, Date.valueOf(meta.getDataInicio()));
            if (meta.getPrazo() != null) stmt.setDate(3, Date.valueOf(meta.getPrazo()));
            else stmt.setNull(3, Types.DATE);
            stmt.setInt(4, meta.getIdChefe());
            stmt.setInt(5, meta.getIdProjeto());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) meta.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public Meta findById(int id) throws SQLException {
        String sql = "SELECT * FROM Meta WHERE id_meta = ?";
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
    public List<Meta> findAll() throws SQLException {
        String sql = "SELECT * FROM Meta";
        List<Meta> metas = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) metas.add(mapRow(rs));
        }
        return metas;
    }

    @Override
    public void update(Meta meta) throws SQLException {
        String sql = "UPDATE Meta SET nome = ?, data_inicio = ?, prazo = ?, id_chefe = ?, id_projeto = ? WHERE id_meta = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, meta.getNome());
            stmt.setDate(2, Date.valueOf(meta.getDataInicio()));
            if (meta.getPrazo() != null) stmt.setDate(3, Date.valueOf(meta.getPrazo()));
            else stmt.setNull(3, Types.DATE);
            stmt.setInt(4, meta.getIdChefe());
            stmt.setInt(5, meta.getIdProjeto());
            stmt.setInt(6, meta.getId());
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
}
