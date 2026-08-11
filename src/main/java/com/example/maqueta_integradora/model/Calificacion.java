package com.example.maqueta_integradora.model;

import java.sql.Timestamp;

public class Calificacion {
    private int idCalificacion;
    private int idComprador;
    private int idVendedor;
    private int idOferta;
    private int puntuacion;
    private String comentario;
    private Timestamp fechaCalificacion;
    private String nombreComprador;
    private String nombreProducto;

    public String getNombreComprador() { return nombreComprador; }
    public void setNombreComprador(String nombreComprador) { this.nombreComprador = nombreComprador; }

    public String getNombreProducto(){
        return nombreProducto; }
    public void setNombreProducto(String nombreProducto){
        this.nombreProducto = nombreProducto; }

    public Calificacion() {
    }

    public int getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(int idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public int getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(int idComprador) {
        this.idComprador = idComprador;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(Timestamp fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public Calificacion(int idComprador, int idVendedor, int idOferta, int puntuacion, String comentario) {
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
        this.idOferta = idOferta;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

}