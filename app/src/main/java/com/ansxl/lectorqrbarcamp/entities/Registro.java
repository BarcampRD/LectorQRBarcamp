package com.ansxl.lectorqrbarcamp.entities;

public class Registro {
    private int id;
    private String cedula;
    private String nombre;
    private String size;

    public Registro() {
    }

    public Registro(int id, String cedula, String nombre, String size) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.size = size;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
