package com.example.frank.planspop.models;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Frank on 13/10/2015.
 */
public class Plan {

    private String id;
    private String nombre;
    private String descripcion;
    private String fecha;
    private String imagen;
    private String imgPath;
    private ParseGeoPoint lugar;
    private String direccion;
    private ParseObject user;

    private Date createdAt;


    public Plan() {
    }

    public Plan(String id, String nombre, String descripcion, String fecha, String imagen, ParseGeoPoint lugar, ParseObject user) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.imagen = imagen;
        this.lugar = lugar;
        this.user = user;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ParseGeoPoint getLugar() {
        return lugar;
    }

    public void setLugar(ParseGeoPoint lugar) {
        this.lugar = lugar;
    }

    public ParseObject getUser() {
        return user;
    }

    public void setUser(ParseObject user) {
        this.user = user;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date creatAt) {
        this.createdAt = creatAt;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
