package com.ruandev.taskflow.entities;

import java.time.LocalDateTime;

public class Log {
    private Integer id;
    private String acao;
    private LocalDateTime dataHora;
    private Integer idMeta;
    private Integer idUsuario;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Integer getIdMeta() { return idMeta; }
    public void setIdMeta(Integer idMeta) { this.idMeta = idMeta; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
}
