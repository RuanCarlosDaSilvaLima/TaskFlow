package com.ruandev.taskflow.entities;

import java.time.LocalDateTime;

public class Comentario {
    private Integer id;
    private String texto;
    private LocalDateTime dataHora;
    private Integer idUsuario;
    private Integer idTarefa;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdTarefa() { return idTarefa; }
    public void setIdTarefa(Integer idTarefa) { this.idTarefa = idTarefa; }
}
