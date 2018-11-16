package com.ansxl.lectorqrbarcamp.entities;

import java.sql.Date;

public class Registro {
    private int id;
    private String cedula;
    private String nombre;
    private String correo;
    private String sizeCamiseta;

    public Registro() {
    }

    public Registro(int id, String cedula, String nombre, String correo, String sizeCamiseta) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.sizeCamiseta = sizeCamiseta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSizeCamiseta() {
        return sizeCamiseta;
    }

    public void setSizeCamiseta(String sizeCamiseta) {
        this.sizeCamiseta = sizeCamiseta;
    }
}
