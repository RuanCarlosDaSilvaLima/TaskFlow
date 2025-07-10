package com.ruandev.taskflow.entities;

import java.time.LocalDate;

public class Meta {
    private Integer id;
    private String titulo;     // ✅ corrigido!
    private LocalDate dataInicio;
    private LocalDate prazo;
    private Integer idChefe;
    private Integer idProjeto;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }         // ✅ corrigido
    public void setTitulo(String titulo) { this.titulo = titulo; } // ✅ corrigido

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

    public Integer getIdChefe() { return idChefe; }
    public void setIdChefe(Integer idChefe) { this.idChefe = idChefe; }

    public Integer getIdProjeto() { return idProjeto; }
    public void setIdProjeto(Integer idProjeto) { this.idProjeto = idProjeto; }
}
