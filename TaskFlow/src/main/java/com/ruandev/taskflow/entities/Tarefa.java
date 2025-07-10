package com.ruandev.taskflow.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Tarefa {
    private Integer id;
    private String titulo;
    private String descricao;    // <-- Adicionado
    private String status;       // pendente, em_andamento, concluida, cancelada
    private LocalDateTime dataCriacao;
    private LocalDate prazo;
    private Integer idResponsavel;
    private Integer idMeta;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

    public Integer getIdResponsavel() { return idResponsavel; }
    public void setIdResponsavel(Integer idResponsavel) { this.idResponsavel = idResponsavel; }

    public Integer getIdMeta() { return idMeta; }
    public void setIdMeta(Integer idMeta) { this.idMeta = idMeta; }
}
