package com.ruandev.taskflow.dao;

import com.ruandev.taskflow.dao.impl.*;
import com.ruandev.taskflow.dao.interfaces.*;

public class DAOFactory {

    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOImpl();
    }

    public static ProjetoDAO getProjetoDAO() {
        return new ProjetoDAOImpl();
    }

    public static EquipeProjetoDAO getEquipeProjetoDAO() {
        return new EquipeProjetoDAOImpl();
    }

    public static MetaDAO getMetaDAO() {
        return new MetaDAOImpl();
    }

    public static TarefaDAO getTarefaDAO() {
        return new TarefaDAOImpl();
    }

    public static ComentarioDAO getComentarioDAO() {
        return new ComentarioDAOImpl();
    }

    public static LogDAO getLogDAO() {
        return new LogDAOImpl();
    }

}
