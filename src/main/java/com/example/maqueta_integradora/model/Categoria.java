package com.example.maqueta_integradora.model;

import java.sql.Timestamp;

public class Categoria {

    private int idCategoria;
    private String nombreCategoria;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;
    private int idAdminCreo;
    private Integer idAdminModifico; // Integer (objeto) por si es NULL al crearse por primera vez
    private boolean estado;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nombreCategoria, Timestamp fechaCreacion, Timestamp fechaModificacion, int idAdminCreo, Integer idAdminModifico , boolean estado){
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.idAdminCreo = idAdminCreo;
        this.idAdminModifico = idAdminModifico;
        this.estado = estado;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public int getIdAdminCreo() {
        return idAdminCreo;
    }

    public void setIdAdminCreo(int idAdminCreo) {
        this.idAdminCreo = idAdminCreo;
    }

    public Integer getIdAdminModifico() {
        return idAdminModifico;
    }

    public void setIdAdminModifico(Integer idAdminModifico) {
        this.idAdminModifico = idAdminModifico;
    }
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}