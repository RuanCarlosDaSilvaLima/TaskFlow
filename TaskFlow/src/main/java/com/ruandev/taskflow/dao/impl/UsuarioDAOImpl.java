package com.ruandev.taskflow.dao.impl;

import com.ruandev.taskflow.dao.interfaces.UsuarioDAO;
import com.ruandev.taskflow.db.DBConnection;
import com.ruandev.taskflow.entities.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void insert(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, foto_perfil) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, usuario.getNome());
            st.setString(2, usuario.getEmail());
            st.setString(3, usuario.getSenha());

            // Foto: se for null, carrega icone-usuario.png
            byte[] fotoBytes = usuario.getFotoPerfil();

            if (fotoBytes == null) {
                try (InputStream is = getClass().getResourceAsStream("/com/ruandev/taskflow/imagens/icone-usuario.png")) {
                    if (is != null) {
                        fotoBytes = is.readAllBytes();
                    } else {
                        throw new RuntimeException("Imagem padrão não encontrada!");
                    }
                } catch (IOException e) {
                    throw new SQLException("Erro ao carregar imagem padrão", e);
                }
            }

            st.setBytes(4, fotoBytes);

            st.executeUpdate();
        }
    }


    @Override
    public Usuario findById(int id) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE id_usuario = ?";
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
    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT * FROM Usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapRow(rs));
            }
        }
        return usuarios;
    }

    @Override
    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET nome = ?, email = ?, senha = ?, foto_perfil = ? WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setBytes(4, usuario.getFotoPerfil());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setFotoPerfil(rs.getBytes("foto_perfil"));
        return usuario;
    }

    @Override
    public Usuario findByEmailESenha(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setString(1, email);
             stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setFotoPerfil(rs.getBytes("foto_perfil"));
                    return usuario;
                }
            }
        }
        return null;
    }
    @Override
    public Usuario findByEmailESenhaNoImage(String email, String senha) throws SQLException {
        String sql = "SELECT  id_usuario, nome, email ,senha FROM usuario WHERE email = ? AND senha = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    return usuario;
                }
            }
        }
        return null;
    }
    @Override
    public Usuario findByName(String nome) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE nome = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getBytes("foto_perfil")
                    );
                }
            }
        }
        return null;
    }

}
