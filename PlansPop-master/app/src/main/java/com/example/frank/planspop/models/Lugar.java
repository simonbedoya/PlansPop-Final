package com.example.frank.planspop.models;

import com.parse.ParseGeoPoint;

/**
 * Created by Frank on 13/10/2015.
 */
public class Lugar {

    private String nombre,direccion;
    private ParseGeoPoint ubicacion;

    public Lugar() {
    }

    public Lugar(String nombre, String direccion, ParseGeoPoint ubicacion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public ParseGeoPoint getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(ParseGeoPoint ubicacion) {
        this.ubicacion = ubicacion;
    }

}
