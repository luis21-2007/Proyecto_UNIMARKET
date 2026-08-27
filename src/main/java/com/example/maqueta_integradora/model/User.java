package com.example.maqueta_integradora.model;

public class User {
    private int id;
    private  String nombre;
    private String apellido;
    private String carrera;
    private String correo;
    private String contrasena;
    private String token;
    private String rol;
    private  Integer activo;
    private int sesionActiva;
    private int esVerificado;

    public int getEsVerificado() {
        return esVerificado;
    }

    public void setEsVerificado(int esVerificado) {
        this.esVerificado = esVerificado;
    }

    public int getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(int sesionActiva) {
        this.sesionActiva = sesionActiva;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }
    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    private long telefono;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(String nombre, int id, String apellido, String carrera, String correo, String contrasena , long telefono, String rol) {
        this.nombre = nombre;
        this.id = id;
        this.apellido = apellido;
        this.carrera = carrera;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.rol = rol;
    }

    public User(){
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}


