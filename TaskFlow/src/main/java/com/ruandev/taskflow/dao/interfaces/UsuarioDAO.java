
package com.ruandev.taskflow.dao.interfaces;

import com.ruandev.taskflow.entities.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {
    void insert(Usuario usuario) throws SQLException;
    Usuario findById(int id) throws SQLException;
    List<Usuario> findAll() throws SQLException;
    void update(Usuario usuario) throws SQLException;
    void delete(int id) throws SQLException;
    Usuario findByEmailESenha(String email, String senha) throws SQLException;

    Usuario findByEmailESenhaNoImage(String email, String senha) throws SQLException;
}
