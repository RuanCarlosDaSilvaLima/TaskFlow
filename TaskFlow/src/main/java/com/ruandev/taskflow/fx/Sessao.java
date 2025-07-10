package com.ruandev.taskflow.fx;

import com.ruandev.taskflow.entities.Usuario;

public class Sessao {
    private static Usuario usuarioLogado;

    public static void setUsuario(Usuario usuario) {
        Sessao.usuarioLogado = usuario;
    }

    public static Usuario getUsuario() {
        return usuarioLogado;
    }

    public static void encerrar() {
        usuarioLogado = null;
    }

    public static boolean isLogado() {
        return usuarioLogado != null;
    }
}
