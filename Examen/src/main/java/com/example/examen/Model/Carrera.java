package com.example.examen.Model;

public class Carrera {
    int codigoCarrera;
    String nombre;
    String sede;

    public Carrera() {
    }

    public Carrera(int codigoCarrera, String nombre, String sede) {
        this.codigoCarrera = codigoCarrera;
        this.nombre = nombre;
        this.sede = sede;
    }

    public int getCodigoCarrera() {
        return codigoCarrera;
    }

    public void setCodigoCarrera(int codigoCarrera) {
        this.codigoCarrera = codigoCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    @Override
    public String toString() {
        return "Carrera{" +
                "codigoCarrera=" + codigoCarrera +
                ", nombre='" + nombre + '\'' +
                ", sede='" + sede + '\'' +
                '}';
    }
}
