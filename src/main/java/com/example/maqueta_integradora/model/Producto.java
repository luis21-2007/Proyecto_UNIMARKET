package com.example.maqueta_integradora.model;
import java.sql.Timestamp;

public class Producto {
    private Integer idProducto;
    private String nombre;
    private Double precio;
    private String descripcion;
    private String imagenUrl;
    private Timestamp fechaPublicacion;
    boolean estado;
    private Integer idCategoria;
    private Integer idUsuario;


    // Getters y Setters
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Timestamp getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Timestamp fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Producto(Integer idProducto, String nombre, Double precio, String descripcion, String imagenUrl, Timestamp fechaPublicacion, boolean estado, Integer idCategoria, Integer idUsuario) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.fechaPublicacion = fechaPublicacion;
        this.estado = estado;
        this.idCategoria = idCategoria;
        this.idUsuario = idUsuario;
    }

    public Producto() {
    }
}
