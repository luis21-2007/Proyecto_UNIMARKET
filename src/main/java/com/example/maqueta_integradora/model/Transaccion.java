package com.example.maqueta_integradora.model;

import java.sql.Timestamp;

public class Transaccion {
    private int idTransaccion;
    private int idProducto;
    private int idComprador;
    private int idVendedor;
    private double monto;
    private Timestamp fechaTransaccion;
    private int estado; // 1 = Completada

    private String nombreProducto;
    private String nombreVendedor;
    private String telefonoVendedor;
    private String nombreComprador;
    private String telefonoComprador;

    public Transaccion(int idTransaccion, int idComprador, int idProducto, int idVendedor, double monto, Timestamp fechaTransaccion, int estado) {
        this.idTransaccion = idTransaccion;
        this.idComprador = idComprador;
        this.idProducto = idProducto;
        this.idVendedor = idVendedor;
        this.monto = monto;
        this.fechaTransaccion = fechaTransaccion;
        this.estado = estado;
    }

    public Transaccion() {
    }

    public int getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(int idTransaccion) { this.idTransaccion = idTransaccion; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getIdComprador() { return idComprador; }
    public void setIdComprador(int idComprador) { this.idComprador = idComprador; }

    public int getIdVendedor() { return idVendedor; }
    public void setIdVendedor(int idVendedor) { this.idVendedor = idVendedor; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public Timestamp getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(Timestamp fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getNombreVendedor() { return nombreVendedor; }
    public void setNombreVendedor(String nombreVendedor) { this.nombreVendedor = nombreVendedor; }

    public String getTelefonoVendedor() { return telefonoVendedor; }
    public void setTelefonoVendedor(String telefonoVendedor) { this.telefonoVendedor = telefonoVendedor; }

    public String getNombreComprador() { return nombreComprador; }
    public void setNombreComprador(String nombreComprador) { this.nombreComprador = nombreComprador; }

    public String getTelefonoComprador() { return telefonoComprador; }
    public void setTelefonoComprador(String telefonoComprador) { this.telefonoComprador = telefonoComprador; }

}