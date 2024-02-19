package com.example.examen.Model;

public class Alumno {
    String dni;
    String nombre;
    String telefono;
    String  email;

    int codigoCarrera;

    public Alumno() {
    }

    public Alumno(String nombre, String telefono, String email, int codigoCarrera) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.codigoCarrera = codigoCarrera;
    }

    public Alumno(String dni, String nombre, String telefono, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public Alumno(String dni, String nombre, String telefono, String email, int codigoCarrera) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.codigoCarrera = codigoCarrera;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCodigoCarrera() {
        return codigoCarrera;
    }

    public void setCodigoCarrera(int codigoCarrera) {
        this.codigoCarrera = codigoCarrera;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "dni=" + dni +
                ", nombre='" + nombre + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                ", codigoCarrera=" + codigoCarrera +
                '}';
    }
}
