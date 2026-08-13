package com.example.maqueta_integradora.model;

import java.sql.Timestamp;

public class Oferta {
    private Integer idOferta;
    private Double montoOferta;
    private Integer estado; // 0: Pendiente, 1: Aceptada, 2: Rechazada
    private Timestamp fechaOferta;
    private Integer idUsuario;
    private Integer idProducto;

    private String nombreProducto;
    private String nombreVendedor;
    private String telefonoVendedor;

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    private String nombreComprador;

    public Oferta() {

    }

    public Oferta(Double montoOferta, Integer idUsuario, Integer idProducto) {
        this.montoOferta = montoOferta;
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
        this.estado = 0; // Por defecto inicia pendiente
    }

    // Getters y Setters

    public Integer getIdOferta() { return idOferta; }
    public void setIdOferta(Integer idOferta) { this.idOferta = idOferta; }

    public Double getMontoOferta() { return montoOferta; }
    public void setMontoOferta(Double montoOferta) { this.montoOferta = montoOferta; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public Timestamp getFechaOferta() { return fechaOferta; }
    public void setFechaOferta(Timestamp fechaOferta) { this.fechaOferta = fechaOferta; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getTelefonoVendedor() {
        return telefonoVendedor;
    }

    public void setTelefonoVendedor(String telefonoVendedor) {
        this.telefonoVendedor = telefonoVendedor;
    }
}
