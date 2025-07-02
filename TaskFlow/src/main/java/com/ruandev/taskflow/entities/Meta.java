package com.ruandev.taskflow.entities;

import java.time.LocalDate;

public class Meta {
    private Integer id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate prazo;
    private Integer idChefe;    // id_usuario do chefe responsável
    private Integer idProjeto;  // Foreign key para projeto

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

    public Integer getIdChefe() { return idChefe; }
    public void setIdChefe(Integer idChefe) { this.idChefe = idChefe; }

    public Integer getIdProjeto() { return idProjeto; }
    public void setIdProjeto(Integer idProjeto) { this.idProjeto = idProjeto; }
}
