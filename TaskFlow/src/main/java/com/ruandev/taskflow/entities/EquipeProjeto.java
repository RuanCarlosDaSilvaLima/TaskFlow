package com.ruandev.taskflow.entities;

import java.time.LocalDate;

public class EquipeProjeto {
    private Integer idUsuario;
    private Integer idProjeto;
    private String papel; // membro ou lider
    private LocalDate dataEntrada;

    // Getters e Setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdProjeto() { return idProjeto; }
    public void setIdProjeto(Integer idProjeto) { this.idProjeto = idProjeto; }

    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }

    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }
}
