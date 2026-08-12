package com.example.maqueta_integradora.model;

import java.sql.Timestamp;

public class Reporte {
    private int idReporte;
    private int idReportador;
    private int idReportado;
    private String motivo;
    private String descripcion;
    private int estado; // 0 = Pendiente, 1 = En revisión, 2 = Resuelto
    private Timestamp fechaReporte;

    private String nombreReportador;
    private String correoReportador;
    private String nombreReportado;
    private String correoReportado;
    private Integer idTransaccion; // Tipo Integer para permitir nulos

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Reporte() {
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public int getIdReportado() {
        return idReportado;
    }

    public void setIdReportado(int idReportado) {
        this.idReportado = idReportado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Timestamp getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(Timestamp fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public int getIdReportador() {
        return idReportador;
    }

    public void setIdReportador(int idReportador) {
        this.idReportador = idReportador;
    }
    public String getNombreReportador(){
        return nombreReportador; }
    public void setNombreReportador(String nombreReportador){
        this.nombreReportador = nombreReportador; }

    public String getCorreoReportador(){
        return correoReportador; }
    public void setCorreoReportador(String correoReportador){
        this.correoReportador = correoReportador; }

    public String getNombreReportado(){
        return nombreReportado; }
    public void setNombreReportado(String nombreReportado){
        this.nombreReportado = nombreReportado; }

    public String getCorreoReportado(){
        return correoReportado; }
    public void setCorreoReportado(String correoReportado){
        this.correoReportado = correoReportado; }

    public Reporte(int idReportador, int idReportado, String motivo, String descripcion) {
        this.idReportador = idReportador;
        this.idReportado = idReportado;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.estado = 0;
    }

}